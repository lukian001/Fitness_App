package com.licenta.fitnessapp.data

import java.time.LocalDateTime

data class Entry(
    val date: LocalDateTime,
    val food: Food,
    val userId: String,
    val portion: Float,
    val grams: Float
)
