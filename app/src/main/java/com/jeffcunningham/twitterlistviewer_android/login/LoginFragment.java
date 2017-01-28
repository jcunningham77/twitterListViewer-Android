package com.jeffcunningham.twitterlistviewer_android.login;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jeffcunningham.twitterlistviewer_android.R;
import com.jeffcunningham.twitterlistviewer_android.lists.ListsActivity;
import com.jeffcunningham.twitterlistviewer_android.util.Logger;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeffcunningham on 1/18/17.
 */

public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";

    TwitterLoginButton twitterLoginButton;
    @BindView(R.id.tvTwitterError)
    TextView textViewTwitterError;
    @BindView(R.id.twitter_logout_button)
    Button btnTwitterLogout;

    TwitterSession twitterSession;

    @Inject
    LoginPresenter loginPresenter;
    @Inject
    Logger logger;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this,view);
        return view;

    }

    @Override
    public void onResume(){
        twitterSession = Twitter.getSessionManager().getActiveSession();
        super.onResume();

        if (twitterSession!=null){
            logger.info(TAG," onResume: there is an active twitter session");
            showLogoutButton();
        } else {

            logger.info(TAG," onResume:  there is not an active twitter session");
            showLoginButton();
        }

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        twitterSession = Twitter.getSessionManager().getActiveSession();

        ((LoginActivity)this.getActivity()).component().inject(this);

        if (twitterSession!=null){
            logger.info(TAG,"onViewCreated: there is an active twitter session");
            showLogoutButton();
        } else {
            logger.info(TAG,"onViewCreated: there is not an active twitter session");
            showLoginButton();
        }

    }

    private void showLoginButton(){

        btnTwitterLogout.setVisibility(View.GONE);

        twitterLoginButton = (TwitterLoginButton) getActivity().findViewById(R.id.twitter_login_button);
        twitterLoginButton.setEnabled(true);
        twitterLoginButton.setVisibility(View.VISIBLE);
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {

                twitterSession = result.data;
                String msg = "@" + twitterSession.getUserName() + " logged in! (#" + twitterSession.getUserId() + ")";
                Log.i(TAG, "success: msg = " + msg);
                loginPresenter.clearSharedPreferencesData();
                Intent listsIntent = new Intent(getActivity(), ListsActivity.class);
                startActivity(listsIntent);

            }
            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
                loginPresenter.clearSharedPreferencesData();
                textViewTwitterError.setVisibility(View.VISIBLE);
                textViewTwitterError.setText("Twitter Login failed due to " + exception.getMessage());
            }
        });



    }

    private void showLogoutButton(){

        logger.info(TAG," onResume: there is an active twitter session");
        twitterLoginButton.setEnabled(false);
        twitterLoginButton.setVisibility(View.GONE);
        btnTwitterLogout.setVisibility(View.VISIBLE);
        btnTwitterLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    logger.info(TAG, "onClick: logging user " + twitterSession.getUserName() + "out of Twitter");
                    loginPresenter.logoutOfTwitter();
                    showLoginButton();
                }
            }
        );


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }



}
