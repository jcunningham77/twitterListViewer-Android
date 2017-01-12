package com.jeffcunningham.twitterlistviewer_android;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.jeffcunningham.twitterlistviewer_android.events.SetDefaultListEvent;
import com.jeffcunningham.twitterlistviewer_android.events.ViewListEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeffcunningham on 12/17/16.
 */

public class ListSelectorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.tvListName)
    TextView tvListName;
    @BindView(R.id.tvMembers)
    TextView tvMembers;

    @BindView(R.id.cbMakeDefaultList)
    CheckBox cbMakeDefaultList;


    private long userId;
    private String listId;
    private boolean isDefault;


    //this is the string identifier of the list
    private String slug;

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    private static final String TAG = "ListSelectorViewHolder";

    @Override
    public void onClick(View v) {
        Log.i(TAG, "onClick: view Id = " + v.getId());


        if (v.getId() == cbMakeDefaultList.getId()){
            Toast.makeText(v.getContext(), "ITEM PRESSED = " + String.valueOf(getAdapterPosition())+ " List Name = " + tvListName.getText().toString() + " List ID = " + listId, Toast.LENGTH_LONG).show();
            Log.i(TAG, "onClick: ITEM PRESSED = " + String.valueOf(getAdapterPosition()));
            Log.i(TAG, "onClick: List Name = " + tvListName.getText().toString());
            Log.i(TAG, "onClick: List ID = " + listId);
            Log.i(TAG, "onClick: User Id = " + userId);

            EventBus.getDefault().post(new SetDefaultListEvent(getAdapterPosition(),listId,slug,tvListName.getText().toString()));
        }

        if (v.getId() == tvListName.getId()){
            Log.i(TAG, "onClick: ITEM PRESSED = " + String.valueOf(getAdapterPosition()));
            Log.i(TAG, "onClick: List Name = " + tvListName.getText().toString());
            EventBus.getDefault().post(new ViewListEvent(this.getSlug(),this.tvListName.getText().toString()));

        }


    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        Log.i(TAG, "setDefault: " + aDefault);
        isDefault = aDefault;
        cbMakeDefaultList.setChecked(isDefault);
    }

    public ListSelectorViewHolder(View v, int itemCount) {
        super(v);
        ButterKnife.bind(this,v);
        cbMakeDefaultList.setOnClickListener(this);
        cbMakeDefaultList.setChecked(isDefault);
        tvListName.setOnClickListener(this);

    }

    public void setTvListName(String listName){
        tvListName.setText(listName);

    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }



    public void setTvMembers(String members){
        tvMembers.setText(members);
    }
}



