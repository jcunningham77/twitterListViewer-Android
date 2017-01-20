package com.jeffcunningham.twitterlistviewer_android.di;

import android.content.SharedPreferences;

import dagger.Component;

/**
 * Created by jeffcunningham on 1/19/17.
 */
@Component(modules = SharedPreferencesModule.class)
public interface SharedPreferencesComponent {
    SharedPreferences maker();
}
