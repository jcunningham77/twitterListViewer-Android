package com.jeffcunningham.lv4t_android;


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
import com.jeffcunningham.lv4t_android.di.DaggerLoginComponent;
import com.jeffcunningham.lv4t_android.di.LoginComponent;
import com.jeffcunningham.lv4t_android.di.LoginModule;
import com.jeffcunningham.lv4t_android.events.LoginSucccessEventFromLandscape;
import com.jeffcunningham.lv4t_android.events.ShowSignOutSignInScreenEvent;
import com.jeffcunningham.lv4t_android.list.TwitterListFragment;
import com.jeffcunningham.lv4t_android.lists.ListsFragment;
import com.jeffcunningham.lv4t_android.login.LoginFragment;
import com.jeffcunningham.lv4t_android.util.Constants;
import com.jeffcunningham.lv4t_android.util.Logger;
import com.jeffcunningham.lv4t_android.util.SharedPreferencesRepository;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterSession;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private LoginComponent component;
    TwitterSession session;
    TabLayout tabLayout;
    private String selectedConfiguration;

    @Inject
    Logger logger;

    @Inject
    SharedPreferencesRepository sharedPreferencesRepository;

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

        logger.info(TAG,"onCreate: ");
        //dagger inject fields
        component().inject(this);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        selectedConfiguration = getString(R.string.selected_configuration);

        if (selectedConfiguration.equalsIgnoreCase(Constants.LAYOUT)){
            initializeNormalLayout();
        } else {
            initializeLandscapeOrLargeLayout();
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ShowSignOutSignInScreenEvent event){
        setContentView(R.layout.activity_login);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        LoginFragment loginFragment = new LoginFragment();
        ft.replace(R.id.fragment_container, loginFragment, "LoginFragment");
        ft.commit();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LoginSucccessEventFromLandscape event){
        initializeLandscapeOrLargeLayout();
    }

    private void initializeLandscapeOrLargeLayout(){
        logger.info(TAG, "initializeLandscapeOrLargeLayout: ");
        View activityView = findViewById(R.id.activity_login);

        session = Twitter.getSessionManager().getActiveSession();

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



        //we have a default list set - load the TwitterListFragment
        if (!StringUtils.isBlank(sharedPreferencesRepository.getDefaultListSlug())){

            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            TwitterListFragment twitterListFragment = new TwitterListFragment();
            ft.replace(R.id.fragment_container, twitterListFragment, "twitterListFragment");
            ft.commit();

        } else if (session!=null){
            logger.info(TAG, "onCreate: TwitterSession is not null - there is an active session open for " + session.getUserName());

            Crashlytics.setUserName(session.getUserName());
            logger.info(TAG, "onCreate: forwarding direct to Lists page");

            TabLayout.Tab tab = tabLayout.getTabAt(1);
            tab.select();
        } else {

            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            LoginFragment loginFragment = new LoginFragment();
            ft.replace(R.id.fragment_container, loginFragment, "LoginFragment");
            ft.commit();
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