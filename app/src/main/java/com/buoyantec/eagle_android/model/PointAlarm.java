package com.buoyantec.eagle_android.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class PointAlarm {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("device_name")
    @Expose
    private String deviceName;

    @SerializedName("pid")
    @Expose
    private Object pid;

    @SerializedName("state")
    @Expose
    private int state;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    @SerializedName("is_checked")
    @Expose
    private boolean isChecked;

    @SerializedName("point_id")
    @Expose
    private int pointId;

    @SerializedName("comment")
    @Expose
    private String comment;

    /**
     *
     * @return
     * The id
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The deviceName
     */
    public String getDeviceName() {
        return deviceName;
    }

    /**
     *
     * @param deviceName
     * The device_name
     */
    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    /**
     *
     * @return
     * The pid
     */
    public Object getPid() {
        return pid;
    }

    /**
     *
     * @param pid
     * The pid
     */
    public void setPid(Object pid) {
        this.pid = pid;
    }

    /**
     *
     * @return
     * The state
     */
    public int getState() {
        return state;
    }

    /**
     *
     * @param state
     * The state
     */
    public void setState(int state) {
        this.state = state;
    }

    /**
     *
     * @return
     * The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     *
     * @param createdAt
     * The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     *
     * @return
     * The createdAt
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     *
     * @param createdAt
     * The created_at
     */
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     *
     * @return
     * The isChecked
     */
    public boolean isIsChecked() {
        return isChecked;
    }

    /**
     *
     * @param isChecked
     * The is_checked
     */
    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    /**
     *
     * @return
     * The pointId
     */
    public int getPointId() {
        return pointId;
    }

    /**
     *
     * @param pointId
     * The point_id
     */
    public void setPointId(int pointId) {
        this.pointId = pointId;
    }

    /**
     *
     * @return
     * The comment
     */
    public String getComment() {
        return comment;
    }

    /**
     *
     * @param comment
     * The comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

}