package com.jeffcunningham.lv4t_android.di;

/**
 * Created by jeffcunningham on 1/22/17.
 */

import android.app.Activity;
import dagger.Module;
import dagger.Provides;

/**
 * A module to wrap the Activity state and expose it to the graph.
 */
@Module
public class ActivityModule {
    private final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    /**
     * Expose the activity to dependents in the graph.
     */
    @Provides Activity activity() {
        return activity;
    }
}
