package com.jeffcunningham.lv4t_android.di;

import com.jeffcunningham.lv4t_android.di.annotations.PerActivity;
import com.jeffcunningham.lv4t_android.list.TwitterListFragment;
import com.jeffcunningham.lv4t_android.lists.ListsFragment;
import com.jeffcunningham.lv4t_android.MainActivity;
import com.jeffcunningham.lv4t_android.login.LoginFragment;

import dagger.Component;

/**
 * Created by jeffcunningham on 1/23/17.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class,modules = LoginModule.class)
public interface LoginComponent {

    void inject(MainActivity activity);
    void inject(LoginFragment fragment);
    void inject(ListsFragment fragment);
    void inject(TwitterListFragment fragment);
}
