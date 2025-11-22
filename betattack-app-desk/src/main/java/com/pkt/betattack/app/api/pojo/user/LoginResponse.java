package com.pkt.betattack.app.api.pojo.user;

import com.google.gson.annotations.SerializedName;

import javax.annotation.processing.Generated;

//@Generated("com.robohorse.robopojogenerator")
public class LoginResponse {

    @SerializedName("user")
    private User user;

    @SerializedName("token")
    private String token;

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return
            "LoginResponse{" +
            "user = '" + user + '\'' +
            ",token = '" + token + '\'' +
            "}";
    }
}