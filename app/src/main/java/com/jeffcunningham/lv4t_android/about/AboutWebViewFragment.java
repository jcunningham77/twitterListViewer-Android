package com.jeffcunningham.lv4t_android.about;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.jeffcunningham.lv4t_android.R;

/**
 * Created by jeffcunningham on 3/15/17.
 */

public class AboutWebViewFragment extends Fragment {

    public WebView mWebView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_about, container, false);

        mWebView = (WebView) view.findViewById(R.id.webview);

        WebSettings ws = mWebView.getSettings();

        ws.setJavaScriptEnabled(true);
        ws.setAllowFileAccess(true);


        mWebView.loadUrl("http://twitterlistviewer.herokuapp.com/#/About");



        return view;
    }

}
