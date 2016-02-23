package com.buoyantec.eagle_android.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kang on 16/2/7.
 * 获取设备列表字符串
 */

@Generated("org.jsonschema2pojo")
public class DevicesString {
    // 转化为String
    @SerializedName("devices")
    @Expose
    private String devicesString;

    /**
     *
     * @return
     * The devices
     */
    public String getDevicesString() {
        return devicesString;
    }
}
