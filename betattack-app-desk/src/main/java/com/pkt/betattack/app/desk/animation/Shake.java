package com.pkt.betattack.app.desk.animation;

import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class Shake {

    private final Node node;
    private TranslateTransition animation;
    private int duration;
    private double xSatart;
    private double xFinish;

    public Shake(Node node) {
        this.node = node;
        animation = new TranslateTransition(Duration.millis(50), this.node);
        animation.setFromX(0f);
        animation.setByX(5f);
        animation.setCycleCount(3);
        animation.setAutoReverse(true);
    }

    public Shake duration(int milliseconds){
        animation = new TranslateTransition(Duration.millis(50), node);
        return this;
    }

    public Shake xStart(double x){
        animation.setFromX(x);
        return this;
    }

    public Shake xFinish(double x){
        animation.setByX(5f);
        return this;
    }

    public Shake yStart(double y){
        animation.setFromY(y);
        return this;
    }

    public Shake yFinish(double y){
        animation.setByX(y);
        return this;
    }

    public Shake cycles(int i) {
        animation.setCycleCount(i);
        return this;
    }

    public Shake play() {
        animation.playFromStart();
        return this;
    }


}
