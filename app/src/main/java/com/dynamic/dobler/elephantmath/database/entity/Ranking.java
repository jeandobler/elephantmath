package com.dynamic.dobler.elephantmath.database.entity;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.Entity;


@Entity
public class Ranking {


    private String id;

    private String email;

    private Date createdAt;

    private Integer points;

    private List<RankingItem> rankingItems;


    public Ranking() {

    }

    public Ranking(String email, Date createdAt, Integer points) {

        this.email = email;
        this.createdAt = createdAt;
        this.points = points;
    }

    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public List<RankingItem> getRankingItems() {
        return rankingItems;
    }

    public void setRankingItems(List<RankingItem> rankingItems) {
        this.rankingItems = rankingItems;
    }
}
