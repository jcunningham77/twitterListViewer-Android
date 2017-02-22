
package com.jeffcunningham.lv4t_android.restapi.dto.get;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DefaultList {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("alias")
    @Expose
    private String alias;
    @SerializedName("listId")
    @Expose
    private String listId;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("listName")
    @Expose
    private String listName;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
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
