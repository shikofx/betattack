
package com.pkt.betattack.app.api.pojo.game;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;
import java.util.Map;

public class Bets {

    //Перевод в режим автоматического подбора исходов
//    @SerializedName("bets")
//    @Expose
//    private Map<String, BetCase> bets;
//
//    public Map<String, BetCase> getBets() { return bets; }
//
//    public void setBets(Map<String, BetCase> bets) {
//        this.bets = bets;
//    }
//
//    public Bets withBets(Map<String, BetCase> bets){
//        this.bets = bets;
//        return this;
//    }
//    @SerializedName("bets")
//    @Expose
//    private Map<String, BetCase> bets;
//
//    public Map<String, BetCase> getBets() { return bets; }
//
//    public void setBets(Map<String, BetCase> bets) {
//        this.bets = bets;
//    }
//
//    public Bets withBets(Map<String, BetCase> bets){
//        this.bets = bets;
//        return this;
//    }
//
    @SerializedName("totalPenalty")
    @Expose
    private Map<String, List<Map<String, String>>> totalPenalty;

    public Map<String, List<Map<String, String>>> getTotalPenalty() {
        return totalPenalty;
    }

    public void setTotalPenalty(Map<String, List<Map<String, String>>> totalPenalty) {
        this.totalPenalty = totalPenalty;
    }

    public Bets withTotalPenalty(Map<String, List<Map<String, String>>> totalPenalty) {
        this.totalPenalty = totalPenalty;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("totalPenalty", totalPenalty).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(totalPenalty).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Bets) == false) {
            return false;
        }
        Bets rhs = ((Bets) other);
        return new EqualsBuilder().append(totalPenalty, rhs.totalPenalty).isEquals();
    }

}
