
package com.jeffcunningham.twitterlistviewer_android.twitterCoreAPIExtensions.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ListMembershipDTO {

    @SerializedName("next_cursor")
    @Expose
    private Integer nextCursor;
    @SerializedName("next_cursor_str")
    @Expose
    private String nextCursorStr;
    @SerializedName("previous_cursor")
    @Expose
    private Integer previousCursor;
    @SerializedName("previous_cursor_str")
    @Expose
    private String previousCursorStr;
    @SerializedName("lists")
    @Expose
    private java.util.List<List> lists = null;

    /**
     * 
     * @return
     *     The nextCursor
     */
    public Integer getNextCursor() {
        return nextCursor;
    }

    /**
     * 
     * @param nextCursor
     *     The next_cursor
     */
    public void setNextCursor(Integer nextCursor) {
        this.nextCursor = nextCursor;
    }

    /**
     * 
     * @return
     *     The nextCursorStr
     */
    public String getNextCursorStr() {
        return nextCursorStr;
    }

    /**
     * 
     * @param nextCursorStr
     *     The next_cursor_str
     */
    public void setNextCursorStr(String nextCursorStr) {
        this.nextCursorStr = nextCursorStr;
    }

    /**
     * 
     * @return
     *     The previousCursor
     */
    public Integer getPreviousCursor() {
        return previousCursor;
    }

    /**
     * 
     * @param previousCursor
     *     The previous_cursor
     */
    public void setPreviousCursor(Integer previousCursor) {
        this.previousCursor = previousCursor;
    }

    /**
     * 
     * @return
     *     The previousCursorStr
     */
    public String getPreviousCursorStr() {
        return previousCursorStr;
    }

    /**
     * 
     * @param previousCursorStr
     *     The previous_cursor_str
     */
    public void setPreviousCursorStr(String previousCursorStr) {
        this.previousCursorStr = previousCursorStr;
    }

    /**
     * 
     * @return
     *     The lists
     */
    public java.util.List<List> getLists() {
        return lists;
    }

    /**
     * 
     * @param lists
     *     The lists
     */
    public void setLists(java.util.List<List> lists) {
        this.lists = lists;
    }

}
