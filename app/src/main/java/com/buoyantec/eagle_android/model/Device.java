package com.buoyantec.eagle_android.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kang on 16/2/7.
 */

@Generated("org.jsonschema2pojo")
public class Device {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("id")
    @Expose
    private int id;

    // 配电系统
    @SerializedName("alarm")
    @Expose
    private boolean alarm;

    // UPS系统,电量仪系统,漏水系统
    @SerializedName("A相电压")
    @Expose
    private String av;

    @SerializedName("B相电压")
    @Expose
    private String bv;

    @SerializedName("C相电压")
    @Expose
    private String cv;

    @SerializedName("频率")
    @Expose
    private String rate;

    // 空调系统
    @SerializedName("温度")
    @Expose
    private String temperature;

    @SerializedName("湿度")
    @Expose
    private String humidity;

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

    // 配电系统
    public boolean getAlarm() {
        return alarm;
    }

    // UPS系统,电量仪系统
    public String[] getAv() {
        String[] cache = {"A相电压", av};
        return cache;
    }

    public String[] getBv() {
        String[] cache = {"B相电压", bv};
        return cache;
    }

    public String[] getCv() {
        String[] cache = {"C相电压", cv};
        return cache;
    }

    public String[] geRate() {
        String[] cache = {"频率", rate};
        return cache;
    }

    // 空调系统
    public String[] getTemperature() {
        String[] cache = {"温度", temperature};
        return cache;
    }

    public String[] geHumidity() {
        String[] cache = {"湿度", humidity};
        return cache;
    }
}