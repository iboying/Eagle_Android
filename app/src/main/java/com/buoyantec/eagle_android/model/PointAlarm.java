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

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("alarm_value")
    @Expose
    private String alarmValue;

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
     * @return
     * The deviceName
     */
    public String getDeviceName() {
        return deviceName;
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
     * @return
     * The state
     */
    public int getState() {
        return state;
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
     * @return
     * The createdAt
     */
    public String getUpdatedAt() {
        return updatedAt;
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
     * @return
     * The pointId
     */
    public int getPointId() {
        return pointId;
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
     * @return
     * The type
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @return
     * The alarmValue
     */
    public String getAlarmValue() {
        return alarmValue;
    }

}