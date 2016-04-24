package com.buoyantec.eagle_android.model;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

// 机房的所有子系统告警数
@Generated("org.jsonschema2pojo")
public class RoomAlarm {

    @SerializedName("room_count")
    @Expose
    private Integer roomCount;

    @SerializedName("sub_systems")
    @Expose
    private List<SubSystemAlarm> subSystems = new ArrayList<SubSystemAlarm>();

    // 房间数
    public Integer getRoomCount() {
        return roomCount;
    }

    // 子系统告警数
    public List<SubSystemAlarm> getSubSystemAlarms() {
        return subSystems;
    }

}