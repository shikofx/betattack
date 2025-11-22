package com.example;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Betserver implements Serializable
{

//    @SerializedName("_id")
//    @Expose
//    private String id;
//    @SerializedName("name")
//    @Expose
//    private String name;
//    @SerializedName("urls")
//    @Expose
//    private Urls urls;
//    @SerializedName("selectors")
//    @Expose
//    private Selectors selectors;
//    @SerializedName("createdAt")
//    @Expose
//    private String createdAt;
//    @SerializedName("updatedAt")
//    @Expose
//    private String updatedAt;
//    private final static long serialVersionUID = 6418670228299555960L;
//
//    /**
//     * No args constructor for use in serialization
//     *
//     */
//    public Betserver() {
//    }
//
//    /**
//     *
//     * @param createdAt
//     * @param urls
//     * @param name
//     * @param id
//     * @param selectors
//     * @param updatedAt
//     */
//    public Betserver(String id, String name, Urls urls, Selectors selectors, String createdAt, String updatedAt) {
//        super();
//        this.id = id;
//        this.name = name;
//        this.urls = urls;
//        this.selectors = selectors;
//        this.createdAt = createdAt;
//        this.updatedAt = updatedAt;
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public Betserver withId(String id) {
//        this.id = id;
//        return this;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public Betserver withName(String name) {
//        this.name = name;
//        return this;
//    }
//
//    public Urls getUrls() {
//        return urls;
//    }
//
//    public void setUrls(Urls urls) {
//        this.urls = urls;
//    }
//
//    public Betserver withUrls(Urls urls) {
//        this.urls = urls;
//        return this;
//    }
//
//    public Selectors getSelectors() {
//        return selectors;
//    }
//
//    public void setSelectors(Selectors selectors) {
//        this.selectors = selectors;
//    }
//
//    public Betserver withSelectors(Selectors selectors) {
//        this.selectors = selectors;
//        return this;
//    }
//
//    public String getCreatedAt() {
//        return createdAt;
//    }
//
//    public void setCreatedAt(String createdAt) {
//        this.createdAt = createdAt;
//    }
//
//    public Betserver withCreatedAt(String createdAt) {
//        this.createdAt = createdAt;
//        return this;
//    }
//
//    public String getUpdatedAt() {
//        return updatedAt;
//    }
//
//    public void setUpdatedAt(String updatedAt) {
//        this.updatedAt = updatedAt;
//    }
//
//    public Betserver withUpdatedAt(String updatedAt) {
//        this.updatedAt = updatedAt;
//        return this;
//    }
//
//    @Override
//    public String toString() {
//        return new ToStringBuilder(this).append("id", id).append("name", name).append("urls", urls).append("selectors", selectors).append("createdAt", createdAt).append("updatedAt", updatedAt).toString();
//    }
//
//    @Override
//    public int hashCode() {
//        return new HashCodeBuilder().append(createdAt).append(urls).append(name).append(id).append(selectors).append(updatedAt).toHashCode();
//    }
//
//    @Override
//    public boolean equals(Object other) {
//        if (other == this) {
//            return true;
//        }
//        if ((other instanceof Betserver) == false) {
//            return false;
//        }
//        Betserver rhs = ((Betserver) other);
//        return new EqualsBuilder().append(createdAt, rhs.createdAt).append(urls, rhs.urls).append(name, rhs.name).append(id, rhs.id).append(selectors, rhs.selectors).append(updatedAt, rhs.updatedAt).isEquals();
//    }

}