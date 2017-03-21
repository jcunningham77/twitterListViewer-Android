package com.jeffcunningham.lv4t_android.events;

/**
 * Created by jeffcunningham on 3/20/17.
 */

public class GetUserLookupSuccessEvent {

    String avatarUrl;
    String screenName;

    public GetUserLookupSuccessEvent(String avatarUrl, String screenName) {
        this.avatarUrl = avatarUrl;
        this.screenName = screenName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getScreenName() {
        return screenName;
    }
}
