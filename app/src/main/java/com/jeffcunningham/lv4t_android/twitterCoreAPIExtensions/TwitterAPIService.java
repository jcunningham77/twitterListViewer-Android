package com.jeffcunningham.lv4t_android.twitterCoreAPIExtensions;

import com.jeffcunningham.lv4t_android.twitterCoreAPIExtensions.dto.list.TwitterList;
import com.jeffcunningham.lv4t_android.twitterCoreAPIExtensions.dto.user.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by jeffcunningham on 12/12/16.
 */

public interface TwitterAPIService {

    /**
     * Returns an HTTP 200 OK response code and a List of TwitterLists that the given screen name is an owner of
     *
     * @param screenName (required) The twitter username to retrive lists owned
     */
    @GET("/1.1/lists/list.json")
    Call<List<TwitterList>> listOwnershipByScreenName(@Query("screen_name") String screenName);



    @GET("/1.1/users/lookup.json")
    Call<List<User>> userLookupByScreenName(@Query("screen_name") String screenName);
}
