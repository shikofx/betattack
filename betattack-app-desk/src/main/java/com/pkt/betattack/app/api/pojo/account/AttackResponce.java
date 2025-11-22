package com.pkt.betattack.app.api.pojo.account;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.json.JSONException;
import org.json.JSONObject;

public class AttackResponce {

    @SerializedName("serverName")
    @Expose
    private String serverName;

    @SerializedName("deposit")
    @Expose
    private Double deposit;

    @SerializedName("betsSum")
    @Expose
    private Double betsSum;

    @SerializedName("timer")
    @Expose
    private String timer;

    @SerializedName("sum")
    @Expose
    private Double sum;

    @SerializedName("koefficient")
    @Expose
    private Double koefficient;

    @SerializedName("status")
    @Expose
    private Boolean status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("fio")
    @Expose
    private String fio;

    public AttackResponce(JSONObject jsonResponce) throws JSONException {
        this.serverName = jsonResponce.getString("serverName");
        this.fio = jsonResponce.getString("fio");
        this.deposit = jsonResponce.getDouble("deposit");
        this.betsSum = jsonResponce.getDouble("betsSum");
        this.timer = jsonResponce.getString("timer");
        this.sum = jsonResponce.getDouble("sum");
        this.koefficient = jsonResponce.getDouble("koefficient");
        this.status = jsonResponce.getBoolean("status");
        this.message = jsonResponce.getString("message");
    }

    public String getServerName() {
        return serverName;
    }

    public String getFio() {
        return fio;
    }

    public Double getDeposit() {
        return deposit;
    }

    public Double getBetsSum() {
        return betsSum;
    }

    public String getTimer() {
        return timer;
    }

    public Double getSum() {
        return sum;
    }

    public Double getKoefficient() {
        return koefficient;
    }

    public Boolean getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "AttackResponce{" +
               "serverName='" + serverName + '\'' +
               ", fio='" + fio + '\'' +
               ", deposit=" + deposit +
               ", betsSum=" + betsSum +
               ", timer='" + timer + '\'' +
               ", sum=" + sum +
               ", koefficient=" + koefficient +
               ", status=" + status +
               ", message='" + message + '\'' +
               '}';
    }
}
