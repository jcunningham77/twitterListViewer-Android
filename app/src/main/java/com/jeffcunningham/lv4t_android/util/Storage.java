package com.jeffcunningham.lv4t_android.util;

/**
 * Created by jeffcunningham on 3/24/18.
 */

public interface Storage<T> {

    void update(T value);

    T retrieve();


}
