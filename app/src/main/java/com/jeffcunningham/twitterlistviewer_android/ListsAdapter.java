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
    private static final int TYPE_LIST_HEADER=0;
    private static final int TYPE_LIST_SELECTOR =1;



    public void setTwitterLists(List<TwitterList> twitterLists) {
        this.twitterLists = twitterLists;
        notifyDataSetChanged();
    }

    private List<TwitterList> twitterLists;
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;

        RecyclerView.ViewHolder viewHolder;
        if (viewType==TYPE_LIST_HEADER){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lists_header_view, parent, false);
            viewHolder = new ListsHeaderViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_selector_view, parent, false);
            // set the view's size, margins, paddings and layout parameters
            viewHolder = new ListSelectorViewHolder(view);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (position>0){
            ((ListSelectorViewHolder)holder).setTvListName(twitterLists.get(position-1).getName());
        }



    }

    @Override
    public int getItemViewType(int position) {
        if (position==0){
            return TYPE_LIST_HEADER;
        } else {
            return TYPE_LIST_SELECTOR;
        }
    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "getItemCount: = ");
        return twitterLists==null? 0 : twitterLists.size()+1;
    }


}
