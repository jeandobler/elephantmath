package com.dynamic.dobler.elephantmath.api;

import com.dynamic.dobler.elephantmath.database.entity.Ranking;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface RakingWebservice {
    @GET("/ranking")
    Call<List<Ranking>> getRankings(@Query("googleId") String googleId);
}
