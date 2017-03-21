package com.jeffcunningham.lv4t_android.util;

import com.jeffcunningham.lv4t_android.twitterCoreAPIExtensions.dto.list.TwitterList;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by jeffcunningham on 2/27/17.
 */

public class ListsCache {

    private List<TwitterList> twitterLists;
    private DateTime depositTimestamp;


    public List<TwitterList> getTwitterLists() {
        return twitterLists;
    }

    public void setTwitterLists(List<TwitterList> twitterLists) {
        this.twitterLists = twitterLists;
    }

    public DateTime getDepositTimestamp() {
        return depositTimestamp;
    }

    public void setDepositTimestamp(DateTime depositTimestamp) {
        this.depositTimestamp = depositTimestamp;
    }
}
