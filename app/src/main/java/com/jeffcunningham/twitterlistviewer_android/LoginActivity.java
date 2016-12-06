package com.jeffcunningham.twitterlistviewer_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.webshell.oauth.OAuth;
import com.webshell.oauth.OAuthCallback;
import com.webshell.oauth.OAuthData;
import com.webshell.oauth.OAuthException;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.buttonSubmit)
    public void login(){
        //login against Backendless
        Log.i(TAG, "login: started");

        //login against BE succeeds - now Oauth:
        final OAuth oauth = new OAuth();
        //TODO move key to config file
        oauth.initialize("_7jIv5Jjoi4hrfHtVwTiuTZULwQ");

//        oauth.popup();
        try {
            oauth.popup("twitter", new OAuthCallback() {
                @Override
                public void authentificationFinished(OAuthData oAuthData) {

                    Log.i(TAG, "authentificationFinished: oAuthData" + oAuthData.toString());
                }
            },null,getApplicationContext());
        } catch (OAuthException e) {
            Log.e(TAG, "login: "+  e.getMessage());
        }


        Intent intent = new Intent();
//        intent
    }




}
