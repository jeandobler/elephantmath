package com.dynamic.dobler.elephantmath.database.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class Ranking {

    @PrimaryKey
    @NonNull
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("googleId")
    @Expose
    private int googleId;

    @SerializedName("createdAt")
    @Expose
    private Date createdAt;

    @SerializedName("points")
    @Expose
    private Integer points;

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public int getGoogleId() {
        return googleId;
    }

    public void setGoogleId(int googleId) {
        this.googleId = googleId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Ranking(@NonNull String id, int googleId, Date createdAt, Integer points) {
        this.id = id;
        this.googleId = googleId;
        this.createdAt = createdAt;
        this.points = points;
    }
}
