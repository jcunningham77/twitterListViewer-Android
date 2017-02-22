package com.jeffcunningham.lv4t_android.events;

/**
 * Created by jeffcunningham on 1/8/17.
 */

public class ViewListEvent {

    String slug;
    String listName;

    public ViewListEvent(String slug, String listName) {
        this.slug = slug;
        this.listName = listName;
    }

    public String getSlug() {
        return slug;
    }

    public String getListName() {
        return listName;
    }
}
