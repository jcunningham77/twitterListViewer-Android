
package com.jeffcunningham.twitterlistviewer_android.api.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BELoginUserResponse {

    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("lastLogin")
    @Expose
    private Integer lastLogin;
    @SerializedName("created")
    @Expose
    private Integer created;
    @SerializedName("___class")
    @Expose
    private String _class;
    @SerializedName("user-token")
    @Expose
    private String userToken;
    @SerializedName("ownerId")
    @Expose
    private String ownerId;
    @SerializedName("updated")
    @Expose
    private Integer updated;
    @SerializedName("objectId")
    @Expose
    private String objectId;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("__meta")
    @Expose
    private String meta;

    /**
     * 
     * @return
     *     The firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * 
     * @param firstName
     *     The firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * 
     * @return
     *     The lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * 
     * @param lastName
     *     The lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * 
     * @return
     *     The lastLogin
     */
    public Integer getLastLogin() {
        return lastLogin;
    }

    /**
     * 
     * @param lastLogin
     *     The lastLogin
     */
    public void setLastLogin(Integer lastLogin) {
        this.lastLogin = lastLogin;
    }

    /**
     * 
     * @return
     *     The created
     */
    public Integer getCreated() {
        return created;
    }

    /**
     * 
     * @param created
     *     The created
     */
    public void setCreated(Integer created) {
        this.created = created;
    }

    /**
     * 
     * @return
     *     The _class
     */
    public String getClass_() {
        return _class;
    }

    /**
     * 
     * @param _class
     *     The ___class
     */
    public void setClass_(String _class) {
        this._class = _class;
    }

    /**
     * 
     * @return
     *     The userToken
     */
    public String getUserToken() {
        return userToken;
    }

    /**
     * 
     * @param userToken
     *     The user-token
     */
    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    /**
     * 
     * @return
     *     The ownerId
     */
    public String getOwnerId() {
        return ownerId;
    }

    /**
     * 
     * @param ownerId
     *     The ownerId
     */
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    /**
     * 
     * @return
     *     The updated
     */
    public Integer getUpdated() {
        return updated;
    }

    /**
     * 
     * @param updated
     *     The updated
     */
    public void setUpdated(Integer updated) {
        this.updated = updated;
    }

    /**
     * 
     * @return
     *     The objectId
     */
    public String getObjectId() {
        return objectId;
    }

    /**
     * 
     * @param objectId
     *     The objectId
     */
    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    /**
     * 
     * @return
     *     The email
     */
    public String getEmail() {
        return email;
    }

    /**
     * 
     * @param email
     *     The email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 
     * @return
     *     The meta
     */
    public String getMeta() {
        return meta;
    }

    /**
     * 
     * @param meta
     *     The __meta
     */
    public void setMeta(String meta) {
        this.meta = meta;
    }

}
