package com.dynamic.dobler.elephantmath.database.converter;

import java.util.Date;

import androidx.room.TypeConverter;

/**
 * Created by Philippe on 02/03/2018.
 */

public class DateConverter {
    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
