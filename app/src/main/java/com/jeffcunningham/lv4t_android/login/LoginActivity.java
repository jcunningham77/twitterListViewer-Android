package com.jeffcunningham.lv4t_android.login;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.jeffcunningham.lv4t_android.BaseApplication;
import com.jeffcunningham.lv4t_android.R;
import com.jeffcunningham.lv4t_android.di.DaggerLoginComponent;
import com.jeffcunningham.lv4t_android.di.LoginComponent;
import com.jeffcunningham.lv4t_android.di.LoginModule;
import com.jeffcunningham.lv4t_android.list.TwitterListFragment;
import com.jeffcunningham.lv4t_android.lists.ListsFragment;
import com.jeffcunningham.lv4t_android.util.Logger;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterSession;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private LoginComponent component;
    TwitterSession session;
    TabLayout tabLayout;
    private String selectedConfiguration;

    @Inject
    Logger logger;

    public LoginComponent component() {
        if (component == null) {
            component = DaggerLoginComponent.builder()
                    .applicationComponent(((BaseApplication) getApplication()).getApplicationComponent())
                    .loginModule(new LoginModule())
                    .build();


        }
        return component;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        component().inject(this);
        logger.info(TAG,"onCreate: ");

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        selectedConfiguration = getString(R.string.selected_configuration);

        session = Twitter.getSessionManager().getActiveSession();

        if (selectedConfiguration.equalsIgnoreCase("layout")){
            initializeNormalLayout();
        } else {
            initializeLandscapeOrLargeLayout();
        }

    }

    private void initializeLandscapeOrLargeLayout(){
        logger.info(TAG, "initializeLandscapeOrLargeLayout: ");
        View activityView = findViewById(R.id.activity_login);

        if (session!=null){
            setContentView(R.layout.activity_lists);
            logger.info(TAG, "initializeLandscapeOrLargeLayout: session!=null - setContentView to activity_lists layout");
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ListsFragment listsFragment = (ListsFragment) fm.findFragmentByTag("ListsFragment");

            if(listsFragment==null){

                listsFragment = new ListsFragment();
            }
            listsFragment.setRetainInstance(false);

            if(!listsFragment.isAdded()) {
//                ft.add(R.id.lists_fragment_container, listsFragment, "ListsFragment");
                ft.replace(R.id.lists_fragment_container, listsFragment, "ListsFragment");
                ft.commit();
            } else {//this listFragment had been added already, probably to the "fragment_container" from the portrait layout
                //so lets remove that fragment and re-add it - since it is changing containers
                ft = fm.beginTransaction();
                ft.remove(listsFragment);
                ft.commit();
                fm.executePendingTransactions();
                ft = fm.beginTransaction();
                ft.add(R.id.lists_fragment_container, listsFragment);
                ft.commit();

            }

            TwitterListFragment twitterListFragment = new TwitterListFragment();
//            ft.add(R.id.twitter_list_fragment_container,twitterListFragment);
            ft = fm.beginTransaction();
            ft.replace(R.id.twitter_list_fragment_container,twitterListFragment,"TwitterListFragment");

            ft.commit();
        } else {
            //show the login fragment
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            LoginFragment loginFragment = new LoginFragment();
            ft.replace(R.id.fragment_container, loginFragment, "LoginFragment");
            ft.commit();
        }



    }

    private void initializeNormalLayout() {

        initializeTabLayout();

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        LoginFragment loginFragment = new LoginFragment();
        ft.replace(R.id.fragment_container, loginFragment, "LoginFragment");
        ft.commit();

        //the below code block could conceivably be pushed to a presenter layer, but since it is performing business logic,
        //and is not referencing any UI, it could likely stay here
        if (session!=null){
            logger.info(TAG, "onCreate: TwitterSession is not null - there is an active session open for " + session.getUserName());

            Crashlytics.setUserName(session.getUserName());
            logger.info(TAG, "onCreate: forwarding direct to Lists page");

            TabLayout.Tab tab = tabLayout.getTabAt(1);
            tab.select();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FragmentManager fm = getFragmentManager();
        if (fm != null) {
            fm.findFragmentByTag("LoginFragment").onActivityResult(requestCode, resultCode, data);

        }
        else logger.debug("Twitter", "fragment is null");
    }

    TabLayout.OnTabSelectedListener listener = new TabLayout.OnTabSelectedListener() {


        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            Log.i(TAG, "onTabSelected: ");
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            switch (tab.getPosition()){
                case 0:
                    LoginFragment loginFragment = new LoginFragment();
                    ft.replace(R.id.fragment_container, loginFragment, "LoginFragment");
                    ft.commit();
                    break;
                case 1:
                    if (Twitter.getSessionManager().getActiveSession()!=null) {
                        ListsFragment listsFragment = new ListsFragment();
                        ft.replace(R.id.fragment_container, listsFragment, "ListsFragment");
                        ft.commit();
                    } else {
                        Toast.makeText(getApplicationContext(),R.string.pleaseLoginMessage,Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

            Log.i(TAG, "onTabUnselected: ");

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

            Log.i(TAG, "onTabReselected: ");

        }
    };


    private void initializeTabLayout(){

        Drawable accountDrawable = ContextCompat.getDrawable(this, R.drawable.ic_account_box_white_24dp);
        Drawable listsDrawable = ContextCompat.getDrawable(this, R.drawable.ic_toc_white_24dp);
        accountDrawable = DrawableCompat.wrap(accountDrawable);
        listsDrawable = DrawableCompat.wrap(listsDrawable);
        int color = Color.parseColor("#607D8B");
        DrawableCompat.setTint(accountDrawable, color);
        DrawableCompat.setTint(listsDrawable, color);



        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Sign In/Out").setIcon(R.drawable.ic_account_box_white_24dp));
        tabLayout.addTab(tabLayout.newTab().setText("Lists").setIcon(R.drawable.ic_toc_white_24dp));
        tabLayout.addOnTabSelectedListener(listener);

        tabLayout.setTabTextColors(
                color,
                color
        );

        tabLayout.setSelectedTabIndicatorColor(color);

    }



}
