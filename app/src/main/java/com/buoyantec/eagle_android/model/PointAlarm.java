package com.buoyantec.eagle_android.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PointAlarm {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("device_name")
    @Expose
    private String deviceName;

    @SerializedName("pid")
    @Expose
    private String pid;

    @SerializedName("room_id")
    @Expose
    private Integer roomId;

    @SerializedName("reported_at")
    @Expose
    private String reportedAt;

    @SerializedName("cleared_at")
    @Expose
    private String clearedAt;

    @SerializedName("checked_at")
    @Expose
    private String checkedAt;

    @SerializedName("is_checked")
    @Expose
    private Boolean isChecked;

    @SerializedName("is_cleared")
    @Expose
    private Boolean isCleared;

    @SerializedName("point_id")
    @Expose
    private Integer pointId;

    @SerializedName("point_name")
    @Expose
    private String pointName;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("meaning")
    @Expose
    private String meaning;

    @SerializedName("checked_user")
    @Expose
    private String checkedUser;


    public int getId() {
        return id;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getPid() {
        return pid;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public String getReportedAt() {
        return reportedAt;
    }

    public String getClearedAt() {
        return clearedAt;
    }

    public String getCheckedAt() {
        return checkedAt;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public Boolean getCleared() {
        return isCleared;
    }

    public Integer getPointId() {
        return pointId;
    }

    public String getPointName() {
        return pointName;
    }

    public String getType() {
        return type;
    }

    public String getMeaning() {
        return meaning;
    }

    public String getCheckedUser() {
        return checkedUser;
    }
}