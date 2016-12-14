package com.jeffcunningham.twitterlistviewer_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.jeffcunningham.twitterlistviewer_android.api.APIManager;
import com.jeffcunningham.twitterlistviewer_android.api.dto.BELoginUserRequest;
import com.jeffcunningham.twitterlistviewer_android.api.dto.BELoginUserResponse;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

public class LoginActivity extends AppCompatActivity {



    private static final String TAG = "LoginActivity";
    private static APIManager apiManager;


    TwitterLoginButton twitterLoginButton;

    @BindView(R.id.editEmail)
    EditText username;
    @BindView(R.id.editPassword)
    EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        twitterLoginButton = (TwitterLoginButton)findViewById(R.id.twitter_login_button);
        //dont activate until logged in w TLV
        twitterLoginButton.setActivated(false);
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
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
        // Make sure that the twitterLoginButton hears the result from any
        // Activity that it triggered.
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.buttonSubmit)
    public void login(){
        //login against Backendless
        Log.i(TAG, "login: started");
        //login against BE succeeds - now Oauth:

        BELoginUserRequest beLoginUserRequest = new BELoginUserRequest();
        beLoginUserRequest.setLogin(username.getText().toString());
        beLoginUserRequest.setPassword(password.getText().toString());

        Call<BELoginUserResponse> backEndlessUserCall = apiManager.getBackEndlessService().login(beLoginUserRequest);

        backEndlessUserCall.enqueue(new Callback<BELoginUserResponse>() {
            @Override
            public void success(Result<BELoginUserResponse> result) {
                Log.i(TAG, "success: result = " + result.data);
                twitterLoginButton.setActivated(true);
            }

            @Override
            public void failure(TwitterException exception) {
                Log.i(TAG, "failure: exception" + exception.getMessage());
            }
        });
    }

}
