package com.buoyantec.eagle_android.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class DeviceDetail {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("points")
    @Expose
    private List<HashMap<String, String>> points = new ArrayList<HashMap<String, String>>();
    @SerializedName("alarms")
    @Expose
    private List<HashMap<String, String>> alarms = new ArrayList<HashMap<String, String>>();

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
     * The points
     */
    public List<HashMap<String, String>> getPoints() {
        return points;
    }

    /**
     *
     * @return
     * The alarms
     */
    public List<HashMap<String, String>> getAlarms() {
        return alarms;
    }

}