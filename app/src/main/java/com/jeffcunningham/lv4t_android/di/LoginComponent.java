package com.jeffcunningham.lv4t_android.di;

import com.jeffcunningham.lv4t_android.lists.ListsFragment;
import com.jeffcunningham.lv4t_android.login.LoginActivity;
import com.jeffcunningham.lv4t_android.login.LoginFragment;

import dagger.Component;

/**
 * Created by jeffcunningham on 1/23/17.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class,modules = LoginModule.class)
public interface LoginComponent {

    void inject(LoginActivity activity);
    void inject(LoginFragment fragment);
    void inject(ListsFragment fragment);
}
