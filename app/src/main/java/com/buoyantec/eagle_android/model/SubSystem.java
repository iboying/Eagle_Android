package com.buoyantec.eagle_android.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class SubSystem {

    @SerializedName("sub_system_name")
    @Expose
    private String subSystemName;

    /**
     * No args constructor for use in serialization
     *
     */
    public SubSystem() {
    }

    /**
     *
     * @param subSystemName
     */
    public SubSystem(String subSystemName) {
        this.subSystemName = subSystemName;
    }

    /**
     *
     * @return
     * The subSystemName
     */
    public String getSubSystemName() {
        return subSystemName;
    }

    /**
     *
     * @param subSystemName
     * The sub_system_name
     */
    public void setSubSystemName(String subSystemName) {
        this.subSystemName = subSystemName;
    }

}