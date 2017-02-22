package com.jeffcunningham.lv4t_android.events;

/**
 * Created by jeffcunningham on 1/3/17.
 */

public class SetDefaultListEvent {
    int position;
    String listId;
    String slug;
    String listName;


    public SetDefaultListEvent(int position, String listId, String slug, String listName) {
        this.position = position;
        this.listId = listId;
        this.slug = slug;
        this.listName = listName;
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

    public String getListName() {
        return listName;
    }
}
