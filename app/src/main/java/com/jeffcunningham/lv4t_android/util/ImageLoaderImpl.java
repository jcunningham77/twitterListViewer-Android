package com.jeffcunningham.lv4t_android.util;

import android.content.Context;
import android.widget.ImageView;

import com.jeffcunningham.lv4t_android.di.annotations.ApplicationContext;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import javax.inject.Inject;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by jeffcunningham on 1/29/17.
 */

public class ImageLoaderImpl implements ImageLoader {

    Context context;

    @Inject
    public ImageLoaderImpl(@ApplicationContext Context context) {
        this.context = context;
    }

    @Override
    public void loadImageByUrlWithRoundedCorners(String url, ImageView view) {

        final int radius = 3;
        final int margin = 3;
        final Transformation transformation = new RoundedCornersTransformation(radius, margin);
        Picasso.with(context).load(url).transform(transformation).into(view);

    }
}
