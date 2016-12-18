package com.jeffcunningham.twitterlistviewer_android;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeffcunningham.twitterlistviewer_android.twitterCoreAPIExtensions.dto.TwitterList;

import java.util.List;

/**
 * Created by jeffcunningham on 12/17/16.
 */

public class ListsAdapter extends Adapter {

    private static final String TAG = "ListsAdapter";

    public void setTwitterLists(List<TwitterList> twitterLists) {
        this.twitterLists = twitterLists;
        notifyDataSetChanged();
    }

    private List<TwitterList> twitterLists;
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_selector_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ListSelectorViewHolder vh = new ListSelectorViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((ListSelectorViewHolder)holder).setTvListName(twitterLists.get(position).getName());

    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "getItemCount: = ");
        return twitterLists==null? 0 : twitterLists.size();
    }


}
