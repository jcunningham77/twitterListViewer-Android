package com.jeffcunningham.lv4t_android.list;

/**
 * Created by jeffcunningham on 1/24/17.
 * This interface doesn't extend BasePresenter to inherit the start/stop lifecycle behavior, because the TwitterListPresenter doesn't
 * make any asynch calls that would have to be cancelled.
 */

public interface TwitterListPresenter {

    public String getTwitterUserName();

    public String getTwitterAvatarImgUrl();
}
