package com.pkt.betattack.app.api.pojo.account;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Account implements Serializable
{

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("login")
    @Expose
    private String login;
    @SerializedName("id_number")
    @Expose
    private String idNumber;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("owner")
    @Expose
    private String owner;
    @SerializedName("server")
    @Expose
    private String server;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("spyAddress")
    @Expose
    private List<SpyAddress> spyAddress = null;
    @SerializedName("attacks")
    @Expose
    private List<Attack> attacks = null;


    private final static long serialVersionUID = -4669889642070498561L;

    /**
     * No args constructor for use in serialization
     *
     */
    public Account() {
    }

    /**
     *
     * @param owner
     * @param server
     * @param createdAt
     * @param password
     * @param spyAddress
     * @param attacks
     * @param id
     * @param login
     * @param idNumber
     * @param updatedAt
     */
    public Account(String id, String login, String idNumber, String password, String owner, String server, String createdAt, String updatedAt, Integer v, List<SpyAddress> spyAddress, List<Attack> attacks) {
        super();
        this.id = id;
        this.login = login;
        this.idNumber = idNumber;
        this.password = password;
        this.owner = owner;
        this.server = server;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.spyAddress = spyAddress;
        this.attacks = attacks;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Account withId(String id) {
        this.id = id;
        return this;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Account withLogin(String login) {
        this.login = login;
        return this;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public Account withIdNumber(String idNumber) {
        this.idNumber = idNumber;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Account withPassword(String password) {
        this.password = password;
        return this;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Account withOwner(String owner) {
        this.owner = owner;
        return this;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public Account withServer(String server) {
        this.server = server;
        return this;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Account withCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Account withUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public List<SpyAddress> getSpyAddress() {
        return spyAddress;
    }

    public void setSpyAddress(List<SpyAddress> spyAddress) {
        this.spyAddress = spyAddress;
    }

    public Account withSpyAddress(List<SpyAddress> spyAddress) {
        this.spyAddress = spyAddress;
        return this;
    }

    public List<Attack> getAttacks() {
        return attacks;
    }

    public void setAttacks(List<Attack> attacks) {
        this.attacks = attacks;
    }

    public Account withAttacks(List<Attack> attacks){
        this.attacks = attacks;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("login", login).append("idNumber", idNumber).append("password", password).append("owner", owner).append("server", server).append("createdAt", createdAt).append("updatedAt", updatedAt).append("spyAddress", spyAddress).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(owner).append(server).append(createdAt).append(password).append(spyAddress).append(id).append(login).append(idNumber).append(updatedAt).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Account) == false) {
            return false;
        }
        Account rhs = ((Account) other);
        return new EqualsBuilder().append(owner, rhs.owner).append(server, rhs.server).append(createdAt, rhs.createdAt).append(password, rhs.password).append(spyAddress, rhs.spyAddress).append(id, rhs.id).append(login, rhs.login).append(idNumber, rhs.idNumber).append(updatedAt, rhs.updatedAt).isEquals();
    }

}
