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
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.jeffcunningham.lv4t_android.about.AboutWebViewFragment;
import com.jeffcunningham.lv4t_android.di.DaggerMainComponent;
import com.jeffcunningham.lv4t_android.di.MainComponent;
import com.jeffcunningham.lv4t_android.di.MainModule;
import com.jeffcunningham.lv4t_android.events.LoginSucccessEventFromLandscape;
import com.jeffcunningham.lv4t_android.events.ShowAboutWebViewFragmentLandscapeEvent;
import com.jeffcunningham.lv4t_android.events.ShowSignOutSignInScreenEvent;
import com.jeffcunningham.lv4t_android.events.ViewListEvent;
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
    private MainComponent component;
    TwitterSession session;
    public TabLayout tabLayout;
    int color = Color.parseColor("#607D8B");
    private String selectedConfiguration;

    @Inject
    Logger logger;

    @Inject
    SharedPreferencesRepository sharedPreferencesRepository;

    public MainComponent component() {
        if (component == null) {
            component = DaggerMainComponent.builder()
                    .applicationComponent(((BaseApplication) getApplication()).getApplicationComponent())
                    .mainModule(new MainModule())
                    .build();
        }
        return component;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //dagger inject fields
        component().inject(this);
        logger.info(TAG,"onCreate: ");
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        selectedConfiguration = getString(R.string.selected_configuration);

        logger.info(TAG, "onCreate: selectedConfiguration = " + selectedConfiguration);

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
    public void onMessageEvent(ShowAboutWebViewFragmentLandscapeEvent event){
        logger.info(TAG, "onMessageEvent: ShowAboutWebViewFragmentLandscapeEvent");
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        AboutWebViewFragment aboutWebViewFragment = new AboutWebViewFragment();
        ft.replace(R.id.twitter_list_fragment_container,aboutWebViewFragment,"AboutWebviewFragment");
        ft.commit();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LoginSucccessEventFromLandscape event){
        initializeLandscapeOrLargeLayout();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ViewListEvent event){
        logger.info(TAG, "onMessageEvent: ViewListEvent, selectedConfiguration = " + selectedConfiguration);
        //its possible that, in landscape or large device orientation, the "About this app" webview will be shown
        //in the space usually occupied by the TwitterListFragment (i.e. activity_lists.twitter_list_fragment_container)
        //In this case, the TwitterListFragment will have been removed by the AboutWebView fragment.
        //We check for that condition, and if it exists, we load the TwitterListFragment
        //otherwise we take no action here and assume TwitterListFragment will refresh itself
        if (!selectedConfiguration.equalsIgnoreCase(Constants.LAYOUT)) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft;
            TwitterListFragment twitterListFragment = (TwitterListFragment) fm.findFragmentByTag("twitterListFragment");
            if (twitterListFragment == null) {
                logger.info(TAG, "onMessageEvent: ViewListEvent - twitterListFragment is null, instantiate a new one and add to it's container");
                twitterListFragment = new TwitterListFragment();
                Bundle bundle = new Bundle();
                bundle.putString("slug",event.getSlug());
                bundle.putString("listName",event.getListName());
                twitterListFragment.setArguments(bundle);
                ft = fm.beginTransaction();
                ft.replace(R.id.twitter_list_fragment_container,twitterListFragment,"TwitterListFragment");
                ft.commit();
            }
        }

        
    }

    private void initializeLandscapeOrLargeLayout(){
        logger.info(TAG, "initializeLandscapeOrLargeLayout: ");


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

            tabLayout.setSelectedTabIndicatorColor(color);
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
                case 2:
                    AboutWebViewFragment aboutWebViewFragment = new AboutWebViewFragment();
                    ft.replace(R.id.fragment_container, aboutWebViewFragment, "LoginFragment");
                    ft.addToBackStack(null);
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
            Log.i(TAG, "onTabSelected: ");
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            tabLayout.setSelectedTabIndicatorColor(color);
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
                case 2:
                    AboutWebViewFragment aboutWebViewFragment = new AboutWebViewFragment();
                    ft.replace(R.id.fragment_container, aboutWebViewFragment, "AboutWebviewFragment");
                    ft.addToBackStack(null);
                    ft.commit();
                    break;

            }

        }
    };


    private void initializeTabLayout(){

        Drawable accountDrawable = ContextCompat.getDrawable(this, R.drawable.ic_account_box_white_24dp);
        Drawable listsDrawable = ContextCompat.getDrawable(this, R.drawable.ic_toc_white_24dp);
        Drawable aboutDrawable = ContextCompat.getDrawable(this, R.drawable.ic_info_white_24dp);
        accountDrawable = DrawableCompat.wrap(accountDrawable);
        listsDrawable = DrawableCompat.wrap(listsDrawable);
        aboutDrawable = DrawableCompat.wrap(aboutDrawable);
        int color = ContextCompat.getColor(this, R.color.twitterLogoBlue);
        DrawableCompat.setTint(accountDrawable, color);
        DrawableCompat.setTint(listsDrawable, color);
        DrawableCompat.setTint(aboutDrawable, color);

        tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        if (session!=null){
            tabLayout.addTab(tabLayout.newTab().setText(R.string.tabLogout).setIcon(R.drawable.ic_account_box_white_24dp));
        } else {
            tabLayout.addTab(tabLayout.newTab().setText(R.string.tabLogout).setIcon(R.drawable.ic_account_box_white_24dp));
        }

        tabLayout.addTab(tabLayout.newTab().setText(R.string.tabLists).setIcon(R.drawable.ic_toc_white_24dp));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tabAbout).setIcon(R.drawable.ic_info_white_24dp));
        tabLayout.addOnTabSelectedListener(listener);

        tabLayout.setTabTextColors(
                color,
                color
        );

        tabLayout.setSelectedTabIndicatorColor(Color.TRANSPARENT);

    }



}
