package com.pkt.betattack.app.api.pojo.attack;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class BetAttack {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("value")
    @Expose
    private String value;

    @SerializedName("direction")
    @Expose
    private String direction;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public BetAttack name(String name){
        this.setName(name);
        return this;
    }

    public BetAttack direction(String direction){
        this.setDirection(direction);
        return this;
    }

    public BetAttack value(String value){
        this.setValue(value);
        return this;
    }

    @Override
    public String toString() {
        return "AttackBet{" +
               "name='" + name + '\'' +
               ", value='" + value + '\'' +
               ", direction='" + direction + '\'' +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BetAttack attackBet = (BetAttack) o;
        return Objects.equals(name, attackBet.name) &&
               Objects.equals(value, attackBet.value) &&
               Objects.equals(direction, attackBet.direction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value, direction);
    }
}
