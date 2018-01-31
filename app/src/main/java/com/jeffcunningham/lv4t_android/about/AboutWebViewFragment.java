package com.jeffcunningham.lv4t_android.about;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.jeffcunningham.lv4t_android.MainActivity;
import com.jeffcunningham.lv4t_android.R;
import com.jeffcunningham.lv4t_android.util.RemoteConfigUtil;

import javax.inject.Inject;

import static com.jeffcunningham.lv4t_android.util.Constants.API_URL;

/**
 * Created by jeffcunningham on 3/15/17.
 */

public class AboutWebViewFragment extends Fragment {

    public WebView mWebView;

    private static FirebaseRemoteConfig firebaseRemoteConfig;

    @Inject
    RemoteConfigUtil remoteConfigUtil;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((MainActivity) getActivity()).component().inject(this);

        View view = inflater.inflate(R.layout.fragment_about, container, false);

        mWebView = (WebView) view.findViewById(R.id.webview);

        WebSettings ws = mWebView.getSettings();

        ws.setJavaScriptEnabled(true);
        ws.setAllowFileAccess(true);

        firebaseRemoteConfig = remoteConfigUtil.initializeFirebaseRemoteConfig();


        mWebView.loadUrl(firebaseRemoteConfig.getString(API_URL)+"#/About");



        return view;
    }

}
