package com.buoyantec.eagle_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by kang on 16/1/27.
 */
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

