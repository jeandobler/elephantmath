package com.dynamic.dobler.elephantmath.database.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class RankingItem {

    @PrimaryKey
    @NonNull
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("rankingId")
    @Expose
    private String rankingId;

    @SerializedName("number1")
    @Expose
    private int number1;

    @SerializedName("number2")
    @Expose
    private int number2;

    @SerializedName("speed")
    @Expose
    private float speed;

    @SerializedName("is_correct")
    @Expose
    private boolean isCorrect;

    public RankingItem(@NonNull String id, String rankingId, int number1, int number2, float speed, boolean isCorrect) {
        this.id = id;
        this.rankingId = rankingId;
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
