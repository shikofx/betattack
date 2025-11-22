
package com.pkt.betattack.app.api.pojo.game;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Objects;

public class Game {

    @SerializedName("_id")
    @Expose
    private String id;

    @SerializedName("serverName")
    @Expose
    private String serverName;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("startTime")
    @Expose
    private String startTime;

    @SerializedName("timer")
    @Expose
    private String timer;

    @SerializedName("isFinished")
    @Expose
    private Boolean isFinished;

    @SerializedName("championship")
    @Expose
    private Championship championship;

    @SerializedName("teamFirst")
    @Expose
    private Team teamFirst;

    @SerializedName("teamSecond")
    @Expose
    private Team teamSecond;

    @SerializedName("bets")
    @Expose
    private Bets bets;

    public Championship getChampionship() {
        return championship;
    }

    public void setChampionship(Championship championship) {
        this.championship = championship;
    }

    public Game withChampionship(Championship championship) {
        this.championship = championship;
        return this;
    }

    public Team getTeamFirst() {
        return teamFirst;
    }

    public void setTeamFirst(Team teamFirst) {
        this.teamFirst = teamFirst;
    }

    public Game withTeamFirst(Team team) {
        this.teamFirst = team;
        return this;
    }

    public Team getTeamSecond() {
        return teamSecond;
    }

    public void setTeamSecond(Team teamSecond) {
        this.teamSecond = teamSecond;
    }

    public Game withTeamSecond(Team teamSecond) {
        this.teamSecond = teamSecond;
        return this;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Game withDate(String date) {
        this.date = date;
        return this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Game withId(String id) {
        this.id = id;
        return this;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public Game withServerName(String serverName) {
        this.serverName = serverName;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Game withUrl(String url) {
        this.url = url;
        return this;
    }

    public Boolean getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(Boolean isFinished) {
        this.isFinished = isFinished;
    }

    public Game withIsFinished(Boolean isFinished) {
        this.isFinished = isFinished;
        return this;
    }

    public String getStartTime() {
        return startTime;
    }

    public int getStartMoment(){
        String[] dateArray = this.date.split("-");
        String[] timeArray = this.startTime.split(":");
        int day = dateArray.length > 0 ? Integer.parseInt(dateArray[2].split("T")[0]) : 0;
        int month = dateArray.length > 1 ? Integer.parseInt(dateArray[1]) : 0;
        int year = dateArray.length > 2 ? Integer.parseInt(dateArray[0]) : 0;
        int hour = timeArray.length > 0 ? Integer.parseInt(timeArray[0]) : 0;
        int minute = 0;
//        int minute = 0timeArray.length > 1 ? Integer.parseInt(timeArray[1]) : 0;
        return  year * 365 * 24 * 60 + month * 30 * 24 * 60 + day * 24 * 60 + hour * 60 + minute;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Game withStartTime(String startTime) {
        this.startTime = startTime;
        return this;
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    public Game withTimer(String timer) {
        this.timer = timer;
        return this;
    }

    public Bets getBets() {
        return bets;
    }

    public void setBets(Bets bets) {
        this.bets = bets;
    }

    public Game withBets(Bets bets) {
        this.bets = bets;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Game game = (Game) o;
        return Objects.equals(id, game.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("championship", championship).append("teamFirst",
                                                                                     teamFirst).append("teamSecond", teamSecond).append("date", date).append("id", id).append("serverName", serverName).append("url", url).append("isFinished", isFinished).append("startTime", startTime).append("timer", timer).append("bets", bets).toString();
    }

}
