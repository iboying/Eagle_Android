package com.buoyantec.eagle_android.model;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Result {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("size")
    @Expose
    private int size;

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
     * @return
     * The size
     */
    public int getSize() {
        return size;
    }

}