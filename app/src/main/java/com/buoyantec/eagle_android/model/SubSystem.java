package com.buoyantec.eagle_android.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
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
     * @param id
     * The id
     */
    public void setId(Integer id) {
        this.id = id;
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