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

import com.crashlytics.android.Crashlytics;
import com.jeffcunningham.lv4t_android.BaseApplication;
import com.jeffcunningham.lv4t_android.R;
import com.jeffcunningham.lv4t_android.di.DaggerLoginComponent;
import com.jeffcunningham.lv4t_android.di.LoginComponent;
import com.jeffcunningham.lv4t_android.di.LoginModule;
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


        Drawable accountDrawable = ContextCompat.getDrawable(this, R.drawable.ic_account_box_white_24dp);
        Drawable listsDrawable = ContextCompat.getDrawable(this, R.drawable.ic_toc_white_24dp);
        accountDrawable = DrawableCompat.wrap(accountDrawable);
        listsDrawable = DrawableCompat.wrap(listsDrawable);
        int color = Color.parseColor("#607D8B");
        DrawableCompat.setTint(accountDrawable, color);
        DrawableCompat.setTint(listsDrawable, color);



        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Sign In/Out").setIcon(R.drawable.ic_account_box_white_24dp));
        tabLayout.addTab(tabLayout.newTab().setText("Lists").setIcon(R.drawable.ic_toc_white_24dp));
        tabLayout.addOnTabSelectedListener(listener);

        tabLayout.setTabTextColors(
                color,
                color
        );

        tabLayout.setSelectedTabIndicatorColor(color);








        session = Twitter.getSessionManager().getActiveSession();




        //
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
            ListsFragment listsFragment = new ListsFragment();
            ft = fm.beginTransaction();
            ft.replace(R.id.fragment_container, listsFragment, "ListsFragment");
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
                    ListsFragment listsFragment = new ListsFragment();
                    ft.replace(R.id.fragment_container, listsFragment, "ListsFragment");
                    ft.commit();
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



}
