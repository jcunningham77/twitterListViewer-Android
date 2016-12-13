package com.jeffcunningham.twitterlistviewer_android.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.twitter.sdk.android.core.TwitterSession;

/**
 * Created by jeffcunningham on 12/12/16.
 */

public class TwitterSessionParcelable implements Parcelable {
    private TwitterSession session;

    protected TwitterSessionParcelable(Parcel in) {
//        session =
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeTypedObject(session,0);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TwitterSessionParcelable> CREATOR = new Creator<TwitterSessionParcelable>() {
        @Override
        public TwitterSessionParcelable createFromParcel(Parcel in) {
            return new TwitterSessionParcelable(in);
        }

        @Override
        public TwitterSessionParcelable[] newArray(int size) {
            return new TwitterSessionParcelable[size];
        }
    };
}
