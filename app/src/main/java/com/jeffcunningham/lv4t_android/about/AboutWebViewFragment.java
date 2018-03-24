package com.jeffcunningham.lv4t_android.about;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.jeffcunningham.lv4t_android.MainActivity;
import com.jeffcunningham.lv4t_android.R;
import com.jeffcunningham.lv4t_android.util.AppSettings;
import com.jeffcunningham.lv4t_android.util.Logger;
import com.jeffcunningham.lv4t_android.util.Storage;

import javax.inject.Inject;

/**
 * Created by jeffcunningham on 3/15/17.
 */

public class AboutWebViewFragment extends Fragment {

    public WebView mWebView;
    private static final String TAG = "AboutWebViewFragment";

    @Inject
    Storage<AppSettings> appSettingsStorage;

    @Inject
    Logger logger;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        ((MainActivity) getActivity()).component().inject(this);
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        mWebView = (WebView) view.findViewById(R.id.webview);

        WebSettings ws = mWebView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setAllowFileAccess(true);


        String aboutStringURL = appSettingsStorage.retrieve().ABOUT_WEBVIEW_URL;
        logger.info(TAG, "onCreateView: aboutStringURL = " + aboutStringURL);
        mWebView.loadUrl(aboutStringURL);

        return view;
    }

}
