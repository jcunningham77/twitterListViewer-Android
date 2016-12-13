package com.jeffcunningham.twitterlistviewer_android.twitterCoreAPIExtensions;

import com.jeffcunningham.twitterlistviewer_android.twitterCoreAPIExtensions.model.ListMembershipDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by jeffcunningham on 12/12/16.
 */

public interface ListOwnershipService {

    /**
     * Returns an HTTP 200 OK response code and a representation of the requesting user if
     * authentication was successful; returns a 401 status code and an error message if not. Use
     * this method to test if supplied user credentials are valid.
     *
     * @param screenName (optional) The entities node will not be included when set to false.
     */
    @GET("/1.1/lists/list.json")
    Call<ListMembershipDTO> listOwnershipByScreenName(@Query("screen_name") String screenName);
}
