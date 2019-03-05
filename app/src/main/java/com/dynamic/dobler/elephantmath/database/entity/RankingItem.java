package com.dynamic.dobler.elephantmath.database.entity;

import androidx.annotation.NonNull;

public class RankingItem {

    private String id;

    private String rankingId;

    private int number1;

    private int number2;

    private float speed;

    private boolean isCorrect;

    public RankingItem() {

    }

    public RankingItem(int number1, int number2, float speed, boolean isCorrect) {
        this.number1 = number1;
        this.number2 = number2;
        this.speed = speed;
        this.isCorrect = isCorrect;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getRankingId() {
        return rankingId;
    }

    public void setRankingId(String rankingId) {
        this.rankingId = rankingId;
    }

    public int getNumber1() {
        return number1;
    }

    public void setNumber1(int number1) {
        this.number1 = number1;
    }

    public int getNumber2() {
        return number2;
    }

    public void setNumber2(int number2) {
        this.number2 = number2;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }
}
