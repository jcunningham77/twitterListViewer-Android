package com.jeffcunningham.twitterlistviewer_android.twitterCoreAPIExtensions;

import com.jeffcunningham.twitterlistviewer_android.twitterCoreAPIExtensions.dto.TwitterList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by jeffcunningham on 12/12/16.
 */

public interface ListOwnershipService {

    /**
     * Returns an HTTP 200 OK response code and a List of TwitterLists that the given screen name is an owner of
     *
     * @param screenName (required) The twitter username to retrive lists owned
     */
    @GET("/1.1/lists/list.json")
    Call<List<TwitterList>> listOwnershipByScreenName(@Query("screen_name") String screenName);
}
