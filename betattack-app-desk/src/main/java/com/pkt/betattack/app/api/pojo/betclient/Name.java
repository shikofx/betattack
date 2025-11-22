package com.pkt.betattack.app.api.pojo.betclient;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class Name implements Serializable {

    @SerializedName("first")
    @Expose
    private String first;
    @SerializedName("middle")
    @Expose
    private String middle;
    @SerializedName("last")
    @Expose
    private String last;
    private final static long serialVersionUID = 874323268196148378L;

    /**
     * No args constructor for use in serialization
     */
    public Name() {
    }

    /**
     *
     */
    public Name(String first, String middle, String last) {
        super();
        this.first = first;
        this.middle = middle;
        this.last = last;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public Name withFirst(String first) {
        this.first = first;
        return this;
    }

    public String getMiddle() {
        return middle;
    }

    public void setMiddle(String middle) {
        this.middle = middle;
    }

    public Name withMiddle(String middle) {
        this.middle = middle;
        return this;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public Name withLast(String last) {
        this.last = last;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("first", first).append("middle", middle).append("last", last)
            .toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(middle).append(last).append(first).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Name) == false) {
            return false;
        }
        Name rhs = ((Name) other);
        return new EqualsBuilder().append(middle, rhs.middle).append(last, rhs.last).append(first, rhs.first)
            .isEquals();
    }
}

