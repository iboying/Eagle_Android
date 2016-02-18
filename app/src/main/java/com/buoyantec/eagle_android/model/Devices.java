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
    @SerializedName("devices")
    @Expose
    private List<Device> devices = new ArrayList<Device>();

    /**
     *
     * @return
     * The devices
     */
    public List<Device> getDevices() {
        return devices;
    }

    /**
     *
     * @param devices
     * The devices
     */
    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }
}
