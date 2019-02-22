package com.dynamic.dobler.elephantmath.api;

import com.dynamic.dobler.elephantmath.database.entity.Ranking;
import com.dynamic.dobler.elephantmath.database.entity.RankingItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface RakingItemWebservice {

    @GET("/ranking-item")
    Call<List<RankingItem>> getRankingItems(@Query("rankingId") String rankingId);

}
