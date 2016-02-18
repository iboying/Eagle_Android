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
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

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
     * @param id
     * The id
     */
    public void setId(int id) {
        this.id = id;
    }
}