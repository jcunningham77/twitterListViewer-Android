package com.jeffcunningham.lv4t_android.lists.ui;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeffcunningham.lv4t_android.R;
import com.jeffcunningham.lv4t_android.twitterCoreAPIExtensions.dto.TwitterList;
import com.twitter.sdk.android.Twitter;

import java.util.List;

/**
 * Created by jeffcunningham on 12/17/16.
 */

public class ListsAdapter extends Adapter{

    private static final String TAG = "ListsAdapter";
    private static final int TYPE_LIST_HEADER=0;
    private static final int TYPE_LIST_SELECTOR =1;


    public void setTwitterUserId(long twitterUserId) {
        this.twitterUserId = twitterUserId;
    }

    private long twitterUserId;




    public void setTwitterLists(List<TwitterList> twitterLists) {
        //TODO: inject util logger
        if (twitterLists!=null) {
            Log.i(TAG, "setTwitterLists: twitterLists.size =" + twitterLists.size());
            this.twitterLists = twitterLists;
        } else {
            Log.e(TAG, "setTwitterLists: twitterLists is null");
        }
        notifyDataSetChanged();
    }

    public List<TwitterList> getTwitterLists() {
        return twitterLists;
    }



    private List<TwitterList> twitterLists;
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {



        View view;

        RecyclerView.ViewHolder viewHolder;
        if (viewType==TYPE_LIST_HEADER){
            Log.i(TAG, "onCreateViewHolder: viewType = Header" );
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lists_header_view, parent, false);
            viewHolder = new ListsHeaderViewHolder(view);
        } else {
            Log.i(TAG, "onCreateViewHolder: viewType = list selector" );
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_selector_view, parent, false);
            // set the view's size, margins, paddings and layout parameters
            viewHolder = new ListsSelectorViewHolder(view, getItemCount());
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Log.i(TAG, "onBindViewHolder: position = " + position);

        if (position>0){
            ((ListsSelectorViewHolder)holder).setTvListName(twitterLists.get(position-1).getName());
            ((ListsSelectorViewHolder)holder).setSlug(twitterLists.get(position-1).getSlug());
            ((ListsSelectorViewHolder)holder).setListId(twitterLists.get(position-1).getIdStr());
            String membersLabel = new String("" + twitterLists.get(position-1).getMemberCount() + " Members");
            ((ListsSelectorViewHolder)holder).setTvMembers(membersLabel);
            ((ListsSelectorViewHolder)holder).setUserId(Twitter.getSessionManager().getActiveSession().getUserId());
            Log.i(TAG, "onBindViewHolder: setting default = " + twitterLists.get(position-1).isDefaultList());
            ((ListsSelectorViewHolder)holder).setDefault(twitterLists.get(position-1).isDefaultList());

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
        Log.i(TAG, "getItemCount: = " + (twitterLists==null? 0 : twitterLists.size()+1));
        return twitterLists==null? 0 : twitterLists.size()+1;
    }


}