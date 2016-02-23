package com.buoyantec.eagle_android.model;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kang on 16/2/7.
 */

@Generated("org.jsonschema2pojo")
public class Devices {
    // 转化为POJO
    @SerializedName("devices")
    @Expose
    private List<Device> devices = new ArrayList<Device>();

//    // 转化为String
//    @SerializedName("devices")
//    @Expose
//    private String stringDevices;

    /**
     *
     * @return
     * The devices
     */
    public List<Device> getDevices() {
        return devices;
    }

//    public String getStringDevices() {
//        return stringDevices;
//    }

}
