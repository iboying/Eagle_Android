package com.buoyantec.iGrid.model;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Alarm {

    @SerializedName("total_pages")
    @Expose
    private int totalPages;
    @SerializedName("current_page")
    @Expose
    private int currentPage;
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
     * The pointAlarms
     */
    public List<PointAlarm> getPointAlarms() {
        return pointAlarms;
    }
}