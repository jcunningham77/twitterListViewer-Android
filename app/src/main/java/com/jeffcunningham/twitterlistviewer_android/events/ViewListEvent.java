package com.jeffcunningham.twitterlistviewer_android.events;

/**
 * Created by jeffcunningham on 1/8/17.
 */

public class ViewListEvent {

    String slug;

    public ViewListEvent(String slug) {
        this.slug = slug;
    }

    public String getSlug() {
        return slug;
    }
}
