package com.dynamic.dobler.elephantmath.database.converter

import java.text.SimpleDateFormat
import java.util.*

object DateConverter {
    @JvmStatic
    fun toNormalDate(date: Date?): String {
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm")
        return format.format(date)
    }
}