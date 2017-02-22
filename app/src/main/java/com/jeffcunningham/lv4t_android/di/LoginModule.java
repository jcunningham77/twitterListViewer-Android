package com.jeffcunningham.lv4t_android.di;

import com.jeffcunningham.lv4t_android.login.LoginPresenter;
import com.jeffcunningham.lv4t_android.login.LoginPresenterImpl;

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
