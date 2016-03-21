package com.buoyantec.eagle_android.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kang on 16/2/7.
 */

@Generated("org.jsonschema2pojo")
public class Device {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    // 配电系统,空调系统(室外机,冷水机组)
    @SerializedName("alarm")
    @Expose
    private String alarm;

    @SerializedName("points")
    @Expose
    private List<HashMap<String, String>> points = new ArrayList<HashMap<String, String>>();

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
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return
     * The alarm
     */
    public String getAlarm() {
        return alarm;
    }

    /**
     *
     * @return
     * The points
     */
    public List<HashMap<String, String>> getPoints() {
        return points;
    }
}