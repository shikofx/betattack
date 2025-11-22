package com.pkt.betattack.app.api.pojo.attack;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AttackRequest {

    @SerializedName("game")
    @Expose
    private GameAttack game;

    @SerializedName("minKeff")
    @Expose
    private String minKef;

    @SerializedName("sum")
    @Expose
    private String sum;

    @SerializedName("repeater")
    @Expose
    private  Integer repeater;

    @SerializedName("error")
    @Expose
    public String error;

    public AttackRequest() {
    }

    public AttackRequest(GameAttack game, String minKef, String sum, Integer repeater, String error) {
        this.game = game;
        this.minKef = minKef;
        this.sum = sum;
        this.repeater = repeater;
        this.error = error;
    }

    public String getMinKef() {
        return minKef;
    }

    public void setMinKef(String minKef) {
        this.minKef = minKef;
    }

    public AttackRequest withMinKef(String minKef) {
        this.minKef = minKef;
        return this;
    }

    public Integer getRepeater() {
        return repeater;
    }

    public void setRepeater(Integer repeater) {
        this.repeater = repeater;
    }

    public AttackRequest withRepeater(Integer repeater) {
        this.repeater = repeater;
        return this;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }



    private GameAttack getGame() {
        return game;
    }

    private void setGame(GameAttack game) {
        this.game = game;
    }

    public AttackRequest game(GameAttack game) {
        this.game = game;
        return this;
    }

    private String getSum() {
        return sum;
    }

    private void setSum(String sum) {
        this.sum = sum;
    }

    public AttackRequest sum(String sum) {
        this.sum = sum;
        return this;
    }

    public AttackRequest repeater(Integer rep){
        this.repeater = rep;
        return  this;
    }

    @Override
    public String toString() {
        return "AttackRequest{" +
               "game=" + game +
               ", minKef=" + minKef +
               ", sum='" + sum + '\'' +
               ", repeater=" + repeater +
               ", error='" + error + '\'' +
               '}';
    }
}
