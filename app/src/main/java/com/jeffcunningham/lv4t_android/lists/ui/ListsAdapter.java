package com.jeffcunningham.lv4t_android.lists.ui;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeffcunningham.lv4t_android.R;
import com.jeffcunningham.lv4t_android.twitterCoreAPIExtensions.dto.list.TwitterList;
import com.twitter.sdk.android.Twitter;

import java.util.List;

/**
 * Created by jeffcunningham on 12/17/16.
 */

public class ListsAdapter extends Adapter{

    private static final String TAG = "ListsAdapter";
    private static final int TYPE_EMPTY_LIST_MESSAGE =0;
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
        if (viewType==TYPE_EMPTY_LIST_MESSAGE){
            Log.i(TAG, "onCreateViewHolder: viewType = Header" );
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_list_message, parent, false);
            viewHolder = new EmptyListMessageViewHolder(view);
        } else {
            Log.i(TAG, "onCreateViewHolder: viewType = list selector" );
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_selector_view, parent, false);
            viewHolder = new ListsSelectorViewHolder(view, getItemCount());
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (twitterLists!=null&&twitterLists.size()>1) {

            Log.i(TAG, "onBindViewHolder: position = " + position);
            ((ListsSelectorViewHolder) holder).setTvListName(twitterLists.get(position).getName());
            ((ListsSelectorViewHolder) holder).setSlug(twitterLists.get(position).getSlug());
            ((ListsSelectorViewHolder) holder).setListId(twitterLists.get(position).getIdStr());
            String membersLabel = new String("" + twitterLists.get(position).getMemberCount() + " Members");
            ((ListsSelectorViewHolder) holder).setTvMembers(membersLabel);
            ((ListsSelectorViewHolder) holder).setUserId(Twitter.getSessionManager().getActiveSession().getUserId());
            Log.i(TAG, "onBindViewHolder: setting default = " + twitterLists.get(position).isDefaultList());
            ((ListsSelectorViewHolder) holder).setDefault(twitterLists.get(position).isDefaultList());
        }
    }

    @Override
    public int getItemViewType(int position) {

        Log.i(TAG, "getItemViewType: position = " + position);

            if (this.twitterLists!=null && this.twitterLists.size()>0){


                Log.d(TAG, "getItemViewType() returned:  + TYPE_LIST_SELECTOR");
                return TYPE_LIST_SELECTOR;

            } else {
                Log.d(TAG, "getItemViewType() returned:  + TYPE_EMPTY_LIST_MESSAGE");

                return TYPE_EMPTY_LIST_MESSAGE;

            }



    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "getItemCount: = " + (twitterLists==null? 0 : twitterLists.size()));
        return twitterLists==null? 1 : twitterLists.size();
    }


}
