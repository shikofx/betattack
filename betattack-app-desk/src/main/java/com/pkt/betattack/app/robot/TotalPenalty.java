package com.pkt.betattack.app.robot;

import com.pkt.betattack.app.desk.controllers.main.GameItemController;

import java.util.ArrayList;
import java.util.List;

public class TotalPenalty {
    private List<GameItemController> controllers = new ArrayList<>();

    public TotalPenalty checkBets(List<GameItemController> controllers) {
        this.controllers = controllers;
        for(GameItemController controller:controllers){
        }
        return this;
    }
}
