package com.dynamic.dobler.elephantmath.database.converter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {

    public static String toNormalDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return format.format(date);
    }
}
