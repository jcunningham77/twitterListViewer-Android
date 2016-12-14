
package com.jeffcunningham.twitterlistviewer_android.model.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class List {

    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("id_str")
    @Expose
    private String idStr;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("uri")
    @Expose
    private String uri;
    @SerializedName("subscriber_count")
    @Expose
    private Integer subscriberCount;
    @SerializedName("member_count")
    @Expose
    private Integer memberCount;
    @SerializedName("mode")
    @Expose
    private String mode;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("following")
    @Expose
    private Boolean following;
    @SerializedName("user")
    @Expose
    private User user;

    /**
     * 
     * @return
     *     The id
     */
    public Long getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The idStr
     */
    public String getIdStr() {
        return idStr;
    }

    /**
     * 
     * @param idStr
     *     The id_str
     */
    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The uri
     */
    public String getUri() {
        return uri;
    }

    /**
     * 
     * @param uri
     *     The uri
     */
    public void setUri(String uri) {
        this.uri = uri;
    }

    /**
     * 
     * @return
     *     The subscriberCount
     */
    public Integer getSubscriberCount() {
        return subscriberCount;
    }

    /**
     * 
     * @param subscriberCount
     *     The subscriber_count
     */
    public void setSubscriberCount(Integer subscriberCount) {
        this.subscriberCount = subscriberCount;
    }

    /**
     * 
     * @return
     *     The memberCount
     */
    public Integer getMemberCount() {
        return memberCount;
    }

    /**
     * 
     * @param memberCount
     *     The member_count
     */
    public void setMemberCount(Integer memberCount) {
        this.memberCount = memberCount;
    }

    /**
     * 
     * @return
     *     The mode
     */
    public String getMode() {
        return mode;
    }

    /**
     * 
     * @param mode
     *     The mode
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
     * 
     * @return
     *     The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 
     * @return
     *     The slug
     */
    public String getSlug() {
        return slug;
    }

    /**
     * 
     * @param slug
     *     The slug
     */
    public void setSlug(String slug) {
        this.slug = slug;
    }

    /**
     * 
     * @return
     *     The fullName
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * 
     * @param fullName
     *     The full_name
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * 
     * @return
     *     The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * 
     * @param createdAt
     *     The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * 
     * @return
     *     The following
     */
    public Boolean getFollowing() {
        return following;
    }

    /**
     * 
     * @param following
     *     The following
     */
    public void setFollowing(Boolean following) {
        this.following = following;
    }

    /**
     * 
     * @return
     *     The user
     */
    public User getUser() {
        return user;
    }

    /**
     * 
     * @param user
     *     The user
     */
    public void setUser(User user) {
        this.user = user;
    }

}
