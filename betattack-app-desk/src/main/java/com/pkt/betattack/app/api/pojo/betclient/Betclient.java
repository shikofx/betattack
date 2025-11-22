package com.pkt.betattack.app.api.pojo.betclient;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pkt.betattack.app.api.pojo.account.Account;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Betclient {

    @SerializedName("name")
    @Expose
    private Name name;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    private final static long serialVersionUID = -8749315494676331028L;

    /**
     * No args constructor for use in serialization
     */
    public Betclient() {
    }

    /**
     *
     */
    public Betclient(Name name, String id, String phone, String email, String createdAt, String updatedAt) {
        super();
        this.name = name;
        this.id = id;
        this.phone = phone;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public Betclient withName(Name name) {
        this.name = name;
        return this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Betclient withId(String id) {
        this.id = id;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Betclient withPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Betclient withEmail(String email) {
        this.email = email;
        return this;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Betclient withCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Betclient withUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("name", name).append("id", id).append("phone", phone)
            .append("email", email).append("createdAt", createdAt).append("updatedAt", updatedAt).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(createdAt).append(phone).append(name).append(id).append(email)
            .append(updatedAt).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Betclient) == false) {
            return false;
        }
        Betclient rhs = ((Betclient) other);
        return new EqualsBuilder().append(createdAt, rhs.createdAt).append(phone, rhs.phone).append(name, rhs.name)
            .append(id, rhs.id).append(email, rhs.email).append(updatedAt, rhs.updatedAt).isEquals();
    }

}

