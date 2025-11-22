package com.pkt.betattack.app.api.pojo.account;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class SpyAddress implements Serializable
{

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("ip")
    @Expose
    private String ip;
    @SerializedName("port")
    @Expose
    private Integer port;
    private final static long serialVersionUID = 6829688353777056436L;

    /**
     * No args constructor for use in serialization
     *
     */
    public SpyAddress() {
    }

    /**
     *
     * @param port
     * @param ip
     * @param id
     */
    public SpyAddress(String id, String ip, Integer port) {
        super();
        this.id = id;
        this.ip = ip;
        this.port = port;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SpyAddress withId(String id) {
        this.id = id;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public SpyAddress withIp(String ip) {
        this.ip = ip;
        return this;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public SpyAddress withPort(Integer port) {
        this.port = port;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("ip", ip).append("port", port).toString();
    }

    public String toUrlString() {
        return new StringBuilder().append("http://").append(ip).append(':').append(port).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(port).append(ip).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof SpyAddress) == false) {
            return false;
        }
        SpyAddress rhs = ((SpyAddress) other);
        return new EqualsBuilder().append(id, rhs.id).append(port, rhs.port).append(ip, rhs.ip).isEquals();
    }

}
