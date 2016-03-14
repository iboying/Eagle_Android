package com.buoyantec.iGrid.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

/**
 * Created by kang on 16/1/27.
 */
@Generated("org.jsonschema2pojo")
public class Rooms {

    @SerializedName("rooms")
    @Expose
    private List<Room> rooms = new ArrayList<Room>();

    /**
     *
     * @return
     * The rooms
     */
    public List<Room> getRooms() {
        return rooms;
    }
}

