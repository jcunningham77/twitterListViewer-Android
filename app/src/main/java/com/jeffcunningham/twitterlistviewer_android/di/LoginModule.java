package com.jeffcunningham.twitterlistviewer_android.di;

import com.jeffcunningham.twitterlistviewer_android.login.LoginPresenter;
import com.jeffcunningham.twitterlistviewer_android.login.LoginPresenterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jeffcunningham on 1/23/17.
 */
@Module
public class LoginModule {

    @Provides
    LoginPresenter provideLoginPresenter(LoginPresenterImpl impl){
        return impl;
    }

}
