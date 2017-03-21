package com.jeffcunningham.lv4t_android.events;

import com.jeffcunningham.lv4t_android.twitterCoreAPIExtensions.dto.list.TwitterList;

import java.util.List;

/**
 * Created by jeffcunningham on 1/19/17.
 */

public class GetListOwnershipByTwitterUserSuccessEvent {
    private List<TwitterList> twitterLists;
    public GetListOwnershipByTwitterUserSuccessEvent(List<TwitterList> twitterLists) {
        this.twitterLists = twitterLists;
    }

    public List<TwitterList> getTwitterLists() {
        return twitterLists;
    }
}
