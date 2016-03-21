package com.buoyantec.eagle_android.model;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class MySystems {

    @SerializedName("systems")
    @Expose
    private List<MySystem> systems = new ArrayList<MySystem>();

    /**
     * No args constructor for use in serialization
     *
     */
    public MySystems() {
    }

    /**
     *
     * @param systems
     */
    public MySystems(List<MySystem> systems) {
        this.systems = systems;
    }

    /**
     *
     * @return
     * The systems
     */
    public List<MySystem> getMySystems() {
        return systems;
    }

}