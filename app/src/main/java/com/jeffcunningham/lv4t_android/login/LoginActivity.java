package com.jeffcunningham.lv4t_android.login;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

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


        ViewPager viewPager = (ViewPager)findViewById(R.id.pager);
        PagerAdapter pagerAdapter = new NavigationPagerAdapter(getFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);




        session = Twitter.getSessionManager().getActiveSession();




//        //
//        FragmentManager fm = getFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        LoginFragment loginFragment = new LoginFragment();
//        ft.replace(R.id.fragment_container, loginFragment, "LoginFragment");
//        ft.commit();
//
//        //the below code block could conceivably be pushed to a presenter layer, but since it is performing business logic,
//        //and is not referencing any UI, it could likely stay here
//        if (session!=null){
//            logger.info(TAG, "onCreate: TwitterSession is not null - there is an active session open for " + session.getUserName());
//
//            Crashlytics.setUserName(session.getUserName());
//            logger.info(TAG, "onCreate: forwarding direct to Lists page");
//            //forward direct to Lists page
//            //load ListsFragment instead of Activity
////            Intent listsIntent = new Intent(LoginActivity.this, ListsActivity.class);
////            startActivity(listsIntent);
////            ft = fm.beginTransaction();
////
////            ListsFragment listsFragment = new ListsFragment();
//
//        }

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


    public class NavigationPagerAdapter extends FragmentPagerAdapter {
        public NavigationPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {

            Fragment fragment= new Fragment();

            switch (i){
                case 0:
                    fragment = new LoginFragment();
                    break;
                case 1:
                    fragment = new ListsFragment();
                    break;
            }

            return fragment;

        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            String title = new String();

            switch (position) {
                case 0:
                    title = "SignIn/Out";
                    break;
                case 1:
                    title = "Lists";
                    break;
            }

            return  title;

        }
    }

}
