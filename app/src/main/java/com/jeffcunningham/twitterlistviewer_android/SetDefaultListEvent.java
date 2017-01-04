package com.jeffcunningham.twitterlistviewer_android;

/**
 * Created by jeffcunningham on 1/3/17.
 */

public class SetDefaultListEvent {
    int position;
    long listId;
    String slug;

    public SetDefaultListEvent(int position, long listId, String slug){
        this.position = position;
        this.listId = listId;
        this.slug = slug;
    }

}
