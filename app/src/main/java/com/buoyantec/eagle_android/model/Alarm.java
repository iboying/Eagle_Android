package com.buoyantec.eagle_android.model;

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
     * @param totalPages
     * The total_pages
     */
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
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
     * @param currentPage
     * The current_page
     */
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    /**
     *
     * @return
     * The pointAlarms
     */
    public List<PointAlarm> getPointAlarms() {
        return pointAlarms;
    }

    /**
     *
     * @param pointAlarms
     * The point_alarms
     */
    public void setPointAlarms(List<PointAlarm> pointAlarms) {
        this.pointAlarms = pointAlarms;
    }

}