package com.jeffcunningham.lv4t_android.di;

import com.jeffcunningham.lv4t_android.list.TwitterListActivity;
import com.jeffcunningham.lv4t_android.list.TwitterListFragment;

import dagger.Component;

/**
 * Created by jeffcunningham on 1/23/17.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ListModule.class)
public interface ListComponent {

    void inject(TwitterListActivity activity);

    void inject(TwitterListFragment fragment);
}
