
package com.jeffcunningham.lv4t_android.restapi.dto.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostDefaultList {

    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}
