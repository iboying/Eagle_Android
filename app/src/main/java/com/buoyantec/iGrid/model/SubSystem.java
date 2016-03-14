package com.buoyantec.iGrid.model;

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
     * @return
     * The subSystemName
     */
    public String getSubSystemName() {
        return subSystemName;
    }

}