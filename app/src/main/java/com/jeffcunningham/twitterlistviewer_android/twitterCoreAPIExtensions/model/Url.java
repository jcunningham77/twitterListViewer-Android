
package com.jeffcunningham.twitterlistviewer_android.twitterCoreAPIExtensions.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Url {

    @SerializedName("urls")
    @Expose
    private List<Url_> urls = null;

    /**
     * 
     * @return
     *     The urls
     */
    public List<Url_> getUrls() {
        return urls;
    }

    /**
     * 
     * @param urls
     *     The urls
     */
    public void setUrls(List<Url_> urls) {
        this.urls = urls;
    }

}
