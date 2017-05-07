package com.jeffcunningham.lv4t_android.login;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jeffcunningham.lv4t_android.MainActivity;
import com.jeffcunningham.lv4t_android.R;
import com.jeffcunningham.lv4t_android.events.LoginSucccessEventFromLandscape;
import com.jeffcunningham.lv4t_android.twitterCoreAPIExtensions.button.DefaultListViewLoginButton;
import com.jeffcunningham.lv4t_android.util.Constants;
import com.jeffcunningham.lv4t_android.util.Logger;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeffcunningham on 1/18/17.
 */

public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";

//    TwitterLoginButton defaultListViewLoginButton;

    DefaultListViewLoginButton defaultListViewLoginButton;
    @BindView(R.id.tvTwitterError)
    TextView textViewTwitterError;
    @BindView(R.id.twitter_logout_button)
    Button btnTwitterLogout;

    TwitterSession twitterSession;

    @Inject
    LoginPresenter loginPresenter;
    @Inject
    Logger logger;

    TabLayout tabLayout;

    public String selectedConfiguration;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ((MainActivity)this.getActivity()).component().inject(this);

        this.selectedConfiguration = getString(R.string.selected_configuration);
        logger.info(TAG, "onCreate: selectedConfiguration = " + selectedConfiguration);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        ButterKnife.bind(this,view);
        tabLayout = (TabLayout)getActivity().findViewById(R.id.tabLayout);
        logger.info(TAG,"onCreateView: ");
        return view;

    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        twitterSession = Twitter.getSessionManager().getActiveSession();



        if (twitterSession!=null){
            logger.info(TAG,"onViewCreated: there is an active twitter session");
            showLogoutButton();
        } else {
            logger.info(TAG,"onViewCreated: there is not an active twitter session");
            showLoginButton();
        }

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

    private void showLoginButton(){

        btnTwitterLogout.setVisibility(View.GONE);

        if (defaultListViewLoginButton==null){
            defaultListViewLoginButton = (DefaultListViewLoginButton) getActivity().findViewById(R.id.twitter_login_button);
        }

        defaultListViewLoginButton.setEnabled(true);
        defaultListViewLoginButton.setVisibility(View.VISIBLE);
        defaultListViewLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {

                twitterSession = result.data;
                String msg = "@" + twitterSession.getUserName() + " logged in! (#" + twitterSession.getUserId() + ")";
                logger.info(TAG, "success: msg = " + msg);

                loginPresenter.clearSharedPreferencesData();

                //todo - should we be using event bus to communicate between fragments and their parent activities?
                if (!LoginFragment.this.selectedConfiguration.equalsIgnoreCase(Constants.LAYOUT)) {
                    EventBus.getDefault().post(new LoginSucccessEventFromLandscape());
                }else{
                    TabLayout.Tab tab = tabLayout.getTabAt(1);
                    tab.select();
                }
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



        logger.info(TAG," showLogoutButton: there is an active twitter session");
        if (defaultListViewLoginButton!=null) {
            defaultListViewLoginButton.setEnabled(false);
            defaultListViewLoginButton.setVisibility(View.GONE);
        }
        if (btnTwitterLogout!=null) {
            btnTwitterLogout.setVisibility(View.VISIBLE);
            btnTwitterLogout.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        logger.info(TAG, "onClick: logging user " + twitterSession.getUserName() + "out of Twitter");
                                                        loginPresenter.logoutOfTwitter();
                                                        loginPresenter.clearSharedPreferencesData();
                                                        if (selectedConfiguration.equalsIgnoreCase("layout")){
                                                            TabLayout.Tab tab = tabLayout.getTabAt(0);
                                                            tab.setText(R.string.tabLogin);
                                                        }
                                                        showLoginButton();
                                                    }
                                                }
            );
        }



    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        defaultListViewLoginButton.onActivityResult(requestCode, resultCode, data);
    }



}
