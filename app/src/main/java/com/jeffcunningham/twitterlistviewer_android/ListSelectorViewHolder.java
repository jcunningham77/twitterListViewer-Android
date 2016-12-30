package com.jeffcunningham.twitterlistviewer_android;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jeffcunningham on 12/17/16.
 */

public class ListSelectorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.tvListName)
    TextView tvListName;

    @BindView(R.id.cbMakeDefaultList)
    CheckBox cbMakeDefaultList;



    private long userId;
    private long listId;
    private boolean isDefault;

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setListId(long listId) {
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

    public ListSelectorViewHolder(View v) {
        super(v);
        ButterKnife.bind(this,v);
        cbMakeDefaultList.setOnClickListener(this);
        cbMakeDefaultList.setChecked(isDefault);

    }

    public void setTvListName(String listName){
        tvListName.setText(listName);

    }

}



