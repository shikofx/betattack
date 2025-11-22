package com.pkt.betattack.app.api.pojo.user;

import javax.annotation.processing.Generated;

import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class User {

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("name")
    private String name;

    @SerializedName("_id")
    private String id;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("updatedAt")
    private String updatedAt;

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedAt() {
        if (createdAt != null) {
            return createdAt.split("T")[0];
        }
        return "";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        if (name != null) {
            return name;
        }
        return "";
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        if (id != null) {
            return id;
        }
        return "";
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        if (email != null) {
            return email;
        }
        return "";
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedAt() {
        if (updatedAt != null) {
            return updatedAt.split("T")[0];
        }
        return "";
    }

    public String getPassword() {
        if(password != null)
            return password;
        return "";
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEqualTo(User user) {
        if (!this.getName().equals(user.getName())) {
            return false;
        }
        if (!this.getPassword().equals(user.getPassword())) {
            return false;
        }
        return this.getEmail().equals(user.getEmail());
    }

    @Override
    public String toString() {
        return
            "User{" +
            "createdAt = '" + createdAt + '\'' +
            ",name = '" + name + '\'' +
            ",_id = '" + id + '\'' +
            ",email = '" + email + '\'' +
            ",password = '" + password + '\'' +

            ",updatedAt = '" + updatedAt + '\'' +
            "}";
    }
}