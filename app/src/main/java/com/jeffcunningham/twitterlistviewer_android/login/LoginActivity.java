package com.jeffcunningham.twitterlistviewer_android.login;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.jeffcunningham.twitterlistviewer_android.BaseApplication;
import com.jeffcunningham.twitterlistviewer_android.R;
import com.jeffcunningham.twitterlistviewer_android.di.DaggerLoginComponent;
import com.jeffcunningham.twitterlistviewer_android.di.LoginComponent;
import com.jeffcunningham.twitterlistviewer_android.di.LoginModule;
import com.jeffcunningham.twitterlistviewer_android.lists.ListsActivity;
import com.jeffcunningham.twitterlistviewer_android.util.Logger;
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

    LoginComponent component() {
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


        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        session = Twitter.getSessionManager().getActiveSession();

        if (session!=null){
            logger.info(TAG, "onCreate: TwitterSession is not null - there is an active session open for " + session.getUserName());
            logger.info(TAG, "onCreate: forwarding direct to Lists page");
            //forward direct to Lists page
            Intent listsIntent = new Intent(LoginActivity.this, ListsActivity.class);
            startActivity(listsIntent);
        }

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        LoginFragment loginFragment = new LoginFragment();
        ft.add(R.id.fragment_container, loginFragment, "LoginFragment");
        ft.commit();

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

}