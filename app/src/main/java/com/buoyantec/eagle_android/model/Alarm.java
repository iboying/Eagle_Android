package com.buoyantec.eagle_android.model;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

// 告警列表
@Generated("org.jsonschema2pojo")
public class Alarm {

    @SerializedName("total_pages")
    @Expose
    private int totalPages;

    @SerializedName("current_page")
    @Expose
    private int currentPage;

    @SerializedName("alarm_size")
    @Expose
    private int alarmSize;

    @SerializedName("point_alarms")
    @Expose
    private List<PointAlarm> pointAlarms = new ArrayList<PointAlarm>();

    /**
     *
     * @return
     * The totalPages
     */
    public int getTotalPages() {
        return totalPages;
    }

    /**
     *
     * @return
     * The currentPage
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     *
     * @return
     * The alarmSize
     */
    public int getAlarmSize() {return alarmSize;}

    /**
     *
     * @return
     * The pointAlarms
     */
    public List<PointAlarm> getPointAlarms() {
        return pointAlarms;
    }
}