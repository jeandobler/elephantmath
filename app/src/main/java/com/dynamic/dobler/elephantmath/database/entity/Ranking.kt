package com.dynamic.dobler.elephantmath.database.entity

import java.util.*

data class Ranking(
    var id: String? = null,
    var email: String? = null,
    var createdAt: Date? = null,
    var points: Int? = null,
    var rankingItems: List<RankingItem>? = null
)
