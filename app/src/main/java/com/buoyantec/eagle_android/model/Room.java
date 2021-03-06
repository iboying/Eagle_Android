package com.buoyantec.eagle_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



/**
 * Created by kang on 16/1/27.
 */
public class Room {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("pic")
    @Expose
    private String pic;


    public String getName() {
        return name;
    }


    public int getId() {
        return id;
    }

    public String getPic() {
        return pic;
    }

}
