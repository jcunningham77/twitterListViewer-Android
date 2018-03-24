package com.jeffcunningham.lv4t_android.util;

/**
 * Created by jeffcunningham on 3/24/18.
 */

public class AppSettings {
    public String API_URL;
    public String ABOUT_WEBVIEW_URL;


    public AppSettings() {
    }

    @Override
    public String toString() {
        return "AppSettings{" +
                "API_URL='" + API_URL + '\'' +
                ", ABOUT_WEBVIEW_URL='" + ABOUT_WEBVIEW_URL + '\'' +
                '}';
    }
}
