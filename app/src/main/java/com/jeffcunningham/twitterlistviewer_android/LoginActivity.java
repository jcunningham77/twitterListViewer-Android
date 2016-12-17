package com.jeffcunningham.twitterlistviewer_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessUser;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {



    private static final String TAG = "LoginActivity";



    TwitterLoginButton twitterLoginButton;

    @BindView(R.id.editEmail)
    EditText editTextUsername;
    @BindView(R.id.editPassword)
    EditText editTextPassword;

    @BindView(R.id.tvError)
    TextView textViewError;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        editTextUsername.requestFocus();



        twitterLoginButton = (TwitterLoginButton)findViewById(R.id.twitter_login_button);
        //dont activate until logged in w TLV
        twitterLoginButton.setEnabled(false);

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

        Backendless.UserService.login(editTextUsername.getText().toString(), editTextPassword.getText().toString(),
            new AsyncCallback<BackendlessUser>() {

                @Override
                public void handleResponse(BackendlessUser response) {
                    Toast.makeText(LoginActivity.this, "Login Successful for " + response.getEmail(), Toast.LENGTH_LONG).show();
                    twitterLoginButton.setEnabled(true);

                }

                @Override
                public void handleFault(BackendlessFault fault) {
                    textViewError.setVisibility(View.VISIBLE);
                    textViewError.setText("Login failed due to " + fault.getMessage());

                    Log.e(TAG, "handleFault: " + fault.getMessage());
                }
            } );


    }

}
