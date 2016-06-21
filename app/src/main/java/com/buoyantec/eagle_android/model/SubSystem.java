package com.buoyantec.eagle_android.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubSystem {

    @SerializedName("id")
    private Integer id;

    @SerializedName("sub_system_name")
    @Expose
    private String subSystemName;

    /**
     *
     * @return
     * The id
     */
    public Integer getId() {
        return id;
    }


    /**
     *
     * @return
     * The subSystemName
     */
    public String getSubSystemName() {
        return subSystemName;
    }

}