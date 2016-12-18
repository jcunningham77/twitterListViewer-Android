package com.jeffcunningham.twitterlistviewer_android;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeffcunningham on 12/17/16.
 */

public class ListSelectorViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvListName)
        TextView tvListName;


        public ListSelectorViewHolder(View v) {
            super(v);
            ButterKnife.bind(this,v);

        }

        public void setTvListName(String listName){
            tvListName.setText(listName);

        }

    }



