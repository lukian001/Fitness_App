package com.licenta.fitnessapp.data

data class Entry(
    val id: String?,
    val date: java.time.LocalDateTime,
    val food: Food,
    val userId: String,
    val portion: Float,
    val grams: Float
)
