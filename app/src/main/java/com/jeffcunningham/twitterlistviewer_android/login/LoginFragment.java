package com.jeffcunningham.twitterlistviewer_android.login;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jeffcunningham.twitterlistviewer_android.R;
import com.jeffcunningham.twitterlistviewer_android.lists.ListsActivity;
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

    TwitterLoginButton twitterLoginButton;

    @BindView(R.id.tvTwitterError)
    TextView textViewTwitterError;

    private static final String TAG = "LoginFragment";

    TwitterSession twitterSession;

    @Inject
    LoginPresenter loginPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this,view);
        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((LoginActivity)this.getActivity()).component().inject(this);

        twitterLoginButton = (TwitterLoginButton) getActivity().findViewById(R.id.twitter_login_button);
        //don't activate until logged in w TLV
        twitterLoginButton.setEnabled(true);
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {

                twitterSession = result.data;
                String msg = "@" + twitterSession.getUserName() + " logged in! (#" + twitterSession.getUserId() + ")";
                Log.i(TAG, "success: msg = " + msg);
                loginPresenter.clearSharedPreferencesData();
                Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_LONG).show();
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }

}
