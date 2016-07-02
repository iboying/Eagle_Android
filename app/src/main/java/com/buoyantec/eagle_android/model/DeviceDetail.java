package com.buoyantec.eagle_android.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeviceDetail {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("pic")
    @Expose
    private String pic;


    // V2版本
    @SerializedName("number_type")
    @Expose
    private List<HashMap<String, String>> numberType = new ArrayList<HashMap<String, String>>();

    @SerializedName("status_type")
    @Expose
    private List<HashMap<String, String>> statusType = new ArrayList<HashMap<String, String>>();

    @SerializedName("alarm_type")
    @Expose
    private List<HashMap<String, String>> alarmType = new ArrayList<HashMap<String, String>>();


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

    public String getPic() {
        return pic;
    }


    // V2 版本

    public List<HashMap<String, String>> getNumberType() {
        return numberType;
    }

    public List<HashMap<String, String>> getAlarmType() {
        return alarmType;
    }

    public List<HashMap<String, String>> getStatusType() {
        return statusType;
    }

}