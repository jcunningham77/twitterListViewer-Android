package com.jeffcunningham.twitterlistviewer_android.util;

import android.widget.ImageView;

/**
 * Created by jeffcunningham on 1/29/17.
 */

public interface ImageLoader {

    void loadImageByUrlWithRoundedCorners(String url, ImageView view);
}
