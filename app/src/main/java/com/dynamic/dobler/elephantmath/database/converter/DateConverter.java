package com.dynamic.dobler.elephantmath.database.converter;

import java.util.Date;

import androidx.room.TypeConverter;

public class DateConverter {

    public static String toNormalDate(Date date) {
        return date.getDay() + "/" + date.getMonth() + "/" + date.getYear() ;
    }
}
