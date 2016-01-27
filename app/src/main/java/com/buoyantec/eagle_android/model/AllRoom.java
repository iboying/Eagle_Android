package com.buoyantec.eagle_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;

/**
 * Created by kang on 16/1/27.
 */
@Generated("org.jsonschema2pojo")
public class AllRoom {

    List<Room> rooms;

    public AllRoom() {
        rooms = new ArrayList<Room>();
    }

    @SerializedName("rooms")
    @Expose
    private List<Room> rooms = new ArrayList<Room>();

    public List<Room> getRooms() {
        return rooms;
    }

}

@Generated("org.jsonschema2pojo")
class Room {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("id")
    @Expose
    private int id;

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

}