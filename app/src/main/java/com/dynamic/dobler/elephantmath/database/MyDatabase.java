package com.dynamic.dobler.elephantmath.database;


import com.dynamic.dobler.elephantmath.database.Dao.RankingDao;
import com.dynamic.dobler.elephantmath.database.Dao.RankingItemDao;
import com.dynamic.dobler.elephantmath.database.converter.DateConverter;
import com.dynamic.dobler.elephantmath.database.entity.Ranking;
import com.dynamic.dobler.elephantmath.database.entity.RankingItem;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Ranking.class, RankingItem.class}, version = 1)

@TypeConverters(DateConverter.class)
public abstract class MyDatabase extends RoomDatabase {

    // --- SINGLETON ---
    private static volatile MyDatabase INSTANCE;

    // --- DAO ---
    public abstract RankingDao rankingDao();
    public abstract RankingItemDao rankingItemDao();

}