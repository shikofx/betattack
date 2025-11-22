package com.pkt.betattack.app.desk.utils;

import com.pkt.betattack.app.desk.controllers.main.GameItemController;

import javax.swing.*;

public class GameTimer {

    private Integer currentTime = Integer.valueOf(0);
    private Timer clock;
    private GameItemController controller;

    private GameTimer setCurrentTime(Integer currentTime) {
        this.currentTime = currentTime;
        return this;
    }

    public GameTimer(GameItemController controller) {
        this.controller = controller;
    }

    private Integer stringToTimeSeconds(String timeStr) {
        return this.getMinutes(timeStr) * 60 + this.getSeconds(timeStr);
    }

    public Integer getMinutes(String timeStr) {
        Integer minutes = 0;
        if (timeStr.contains(":")) {
            String[] timeArr = timeStr.split(":");
            minutes = Integer.parseInt(timeArr[0]);
        }
        return minutes;
    }

    public Integer getSeconds(String timeStr) {
        Integer seconds = 0;
        if (timeStr.contains(":")) {
            String[] timeArr = timeStr.split(":");
            seconds = Integer.parseInt(timeArr[1]);
        }
        return seconds;
    }

    private String timerToStr(Integer time) {
        Integer minutes = (time - time % 60) / 60;
        Integer seconds = time % 60;
        String timeStr = new StringBuilder()
            .append(addZero(minutes))
            .append(':')
            .append(addZero(seconds))
            .toString();
        return timeStr;
    }

    private String addZero(Integer timeItem) {
        return timeItem > 9 ? Integer.toString(timeItem)
                            : new StringBuilder().append('0').append(timeItem).toString();
    }

    public GameTimer start(String startTimeStr) {
//        Integer startTime = stringToTimeSeconds(startTimeStr);
//        currentTime = startTime;
//        if (startTime > 0) {
//            clock = new Timer(5000, new ActionListener() {
//                @Override
//                public void actionPerformed(java.awt.event.ActionEvent e) {
//                    currentTime = currentTime + 5;
//                    controller.setTimer(timerToStr(currentTime));
//                }
//            });
//            clock.start();
//        } else {
            controller.setTimer(startTimeStr);
//        }
        return this;
    }

    public GameTimer stop() {
        if (clock != null && clock.isRunning()) {
            clock.stop();
        }
        return this;
    }
}

