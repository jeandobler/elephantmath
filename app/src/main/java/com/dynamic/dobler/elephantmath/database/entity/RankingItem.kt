package com.dynamic.dobler.elephantmath.database.entity

data class RankingItem(
    var id: String? = null,
    var rankingId: String? = null,
    var number1: Int = 0,
    var number2: Int = 0,
    var speed: Float = 0f,
    var isCorrect: Boolean = false
)