package com.jeffcunningham.twitterlistviewer_android.events;

/**
 * Created by jeffcunningham on 1/3/17.
 */

public class SetDefaultListEvent {
    int position;
    String listId;
    String slug;



    public SetDefaultListEvent(int position, String listId, String slug){
        this.position = position;
        this.listId = listId;
        this.slug = slug;
    }

    public int getPosition() {
        return position;
    }

    public String getListId() {
        return listId;
    }

    public String getSlug() {
        return slug;
    }
}
