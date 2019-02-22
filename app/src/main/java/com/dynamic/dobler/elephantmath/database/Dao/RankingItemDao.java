package com.dynamic.dobler.elephantmath.database.Dao;

import com.dynamic.dobler.elephantmath.database.entity.Ranking;
import com.dynamic.dobler.elephantmath.database.entity.RankingItem;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface RankingItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void save(List<RankingItem> rankingItems);

    @Query("SELECT * FROM rankingItem WHERE rankingId = :rankingId order by id desc")
    LiveData<List<RankingItem>> get(int rankingId);

    @Query("SELECT * FROM rankingItem WHERE id= :id")
    LiveData<RankingItem> show(int id);


}
