//package com.jeffcunningham.twitterlistviewer_android.di;
//
//import android.app.Application;
//import android.content.SharedPreferences;
//import android.preference.PreferenceManager;
//
//import javax.inject.Singleton;
//
//import dagger.Module;
//import dagger.Provides;
//
///**
// * Created by jeffcunningham on 1/19/17.
// */
//@Module
//public class SharedPreferencesModule {
//
////    static private Context context;
////
////    public SharedPreferencesModule(Context context){
////        this.context = context;
////    }
////
////    @Provides
////    static SharedPreferencesRepositoryImpl provideSharedPreferences(){
////        return new SharedPreferencesRepositoryImpl(context);
////    }
//
////    @Provides
////    @Singleton
////        // Application reference must come from AppModule.class
////    SharedPreferencesRepository providesSharedPreferencesRepository(Application application) {
////        return SharedPreferencesRepositoryImpl(application.getApplicationContext());
////    }
//
//    @Provides
//    @Singleton
//        // Application reference must come from AppModule.class
//    SharedPreferences providesSharedPreferences(Application application) {
//        return PreferenceManager.getDefaultSharedPreferences(application);
//    }
//
//}
