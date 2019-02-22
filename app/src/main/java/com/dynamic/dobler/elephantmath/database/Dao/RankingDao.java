package com.dynamic.dobler.elephantmath.database.Dao;

import com.dynamic.dobler.elephantmath.database.entity.Ranking;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface RankingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void save(Ranking ranking);

    @Query("SELECT * FROM ranking WHERE googleId = :googleId order by id desc")
    LiveData<List<Ranking>> get(int googleId);

    @Query("SELECT * FROM ranking WHERE id= :id")
    LiveData<Ranking> show(int id);


}
