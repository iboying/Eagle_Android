package com.buoyantec.eagle_android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by kang on 16/1/25.
 * 模型: User
 * 用途: 登录,用户信息
 */

public class User implements Serializable{

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("authentication_token")
    @Expose
    private String authenticationToken;

    @SerializedName("os")
    @Expose
    private String os;


    @SerializedName("device_token")
    @Expose
    private String deviceToken;

    @SerializedName("errors")
    @Expose
    private String errors;

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
     * @return
     * The email
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @return
     * The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     *
     * @return
     * The updatedAt
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

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
     * The phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     *
     * @return
     * The authenticationToken
     */
    public String getAuthenticationToken() {
        return authenticationToken;
    }


    public String getErrors() {
        return errors;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public String getOs() {
        return os;
    }
}