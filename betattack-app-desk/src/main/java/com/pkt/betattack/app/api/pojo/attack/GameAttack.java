package com.pkt.betattack.app.api.pojo.attack;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GameAttack {

    @SerializedName("bet")
    @Expose
    BetAttack bet;

    @SerializedName("url")
    @Expose
    private String url;

    public BetAttack getBet() {
        return bet;
    }

    public void bet(BetAttack bet) {
        this.bet = bet;
    }

    public GameAttack bet(String name, String value, String direction) {
        bet = new BetAttack()
            .name(name)
            .direction(direction)
            .value(value);
        return this;
    }

    public String getUrl() {
        return url;
    }

    public GameAttack url(String url) {
        this.url=url;
        return  this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "GameAttack{" +
               "bet=" + bet +
               ", url='" + url + '\'' +
               '}';
    }
}