package com.jeffcunningham.lv4t_android.di;

import android.app.Application;

import com.jeffcunningham.lv4t_android.BaseApplication;
import com.jeffcunningham.lv4t_android.util.ImageLoader;
import com.jeffcunningham.lv4t_android.util.ListsStorage;
import com.jeffcunningham.lv4t_android.util.Logger;
import com.jeffcunningham.lv4t_android.util.SharedPreferencesRepository;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(BaseApplication baseApplication);
    Application application();

    //seems like we need these available to use the @Inject at field level
    SharedPreferencesRepository sharedPreferencesRepository();
    Logger logger();

    ImageLoader imageLoader();

    ListsStorage listsStorage();

    OkHttpClient okhttpClient();



}
