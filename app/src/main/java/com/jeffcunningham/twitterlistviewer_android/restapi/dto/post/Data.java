
package com.jeffcunningham.twitterlistviewer_android.restapi.dto.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("alias")
    @Expose
    private String alias;
    @SerializedName("listId")
    @Expose
    private long listId;
    @SerializedName("slug")
    @Expose
    private String slug;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }


    public long getListId() {
        return listId;
    }

    public void setListId(long listId) {
        this.listId = listId;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

}
