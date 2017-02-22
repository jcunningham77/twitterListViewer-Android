package com.jeffcunningham.lv4t_android.events;

import com.jeffcunningham.lv4t_android.restapi.dto.get.DefaultList;

/**
 * Created by jeffcunningham on 1/19/17.
 */

public class GetDefaultListSuccessEvent {
    private DefaultList defaultList;

    public GetDefaultListSuccessEvent(DefaultList defaultList) {
        this.defaultList = defaultList;
    }

    public DefaultList getDefaultList() {
        return defaultList;
    }
}
