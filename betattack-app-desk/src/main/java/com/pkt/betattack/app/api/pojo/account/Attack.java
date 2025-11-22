package com.pkt.betattack.app.api.pojo.account;

import java.util.List;

public class Attack {
    private String game;
    private boolean available;
    private List<AttackValue> values;

    public Attack(String game, boolean available,
                  List<AttackValue> values) {
        this.game = game;
        this.available = available;
        this.values = values;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public List<AttackValue> getValues() {
        return values;
    }

    public void setValues(List<AttackValue> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        return "Attack{" +
               "game='" + game + '\'' +
               ", available=" + available +
               ", values=" + values +
               '}';
    }

    class AttackValue {
        Double sum;
        Double koefficient;

        public AttackValue(Double sum, Double koefficient) {
            this.sum = sum;
            this.koefficient = koefficient;
        }

        public Double getSum() {
            return sum;
        }

        public void setSum(Double sum) {
            this.sum = sum;
        }

        public Double getKoefficient() {
            return koefficient;
        }

        public void setKoefficient(Double koefficient) {
            this.koefficient = koefficient;
        }

        @Override
        public String toString() {
            return "AttackValue{" +
                   "sum=" + sum +
                   ", koefficient=" + koefficient +
                   '}';
        }
    }

}