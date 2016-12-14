package com.jeffcunningham.twitterlistviewer_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {



    private static final String TAG = "LoginActivity";

//    @BindView(R.id.twitter_login_button)
    TwitterLoginButton loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        loginButton = (TwitterLoginButton)findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {

                TwitterSession session = result.data;
                String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                Log.i(TAG, "success: msg = " + msg);
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();





                Intent listsIntent = new Intent(LoginActivity.this, ListsActivity.class);
                startActivity(listsIntent);

            }
            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Make sure that the loginButton hears the result from any
        // Activity that it triggered.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }



    @OnClick(R.id.buttonSubmit)
    public void login(){
        //login against Backendless
        Log.i(TAG, "login: started");

        //login against BE succeeds - now Oauth:



    }




}
