package com.jeffcunningham.lv4t_android.twitterCoreAPIExtensions.button;

import android.content.Context;
import android.util.AttributeSet;

import com.jeffcunningham.lv4t_android.R;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

/**
 * Created by jeffcunningham on 4/1/17.
 */

public class DefaultListViewLoginButton extends TwitterLoginButton {
    public DefaultListViewLoginButton(Context context) {
        super(context);
        init();
    }

    public DefaultListViewLoginButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DefaultListViewLoginButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {


        setBackgroundResource(R.drawable.twitter_login_button);

    }
}
