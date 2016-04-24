package com.buoyantec.eagle_android.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

// 机房下某个子系统的告警信息
@Generated("org.jsonschema2pojo")
public class SubSystemAlarm {

    @SerializedName("sub_system_id")
    @Expose
    private Integer subSystemId;

    @SerializedName("sub_system_name")
    @Expose
    private String subSystemName;

    @SerializedName("sub_system_count")
    @Expose
    private Integer subSystemCount;


    public Integer getSubSystemId() {
        return subSystemId;
    }

    public String getSubSystemName() {
        return subSystemName;
    }

    public Integer getSubSystemCount() {
        return subSystemCount;
    }
}