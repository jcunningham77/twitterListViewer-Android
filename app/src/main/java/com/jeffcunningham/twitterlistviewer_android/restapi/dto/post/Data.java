
package com.jeffcunningham.twitterlistviewer_android.restapi.dto.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("alias")
    @Expose
    private String alias;
    @SerializedName("listId")
    @Expose
    private String listId;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("listName")
    @Expose
    private String listName;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }


    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }
}
