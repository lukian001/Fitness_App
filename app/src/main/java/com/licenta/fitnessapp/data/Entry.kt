package com.licenta.fitnessapp.data

data class Entry(
    val id: String?,
    val date: java.time.LocalDateTime,
    val food: Food,
    val exercise: Exercises,
    val caloriesBurned: Float,
    val userId: String,
    val portion: Float,
    val grams: Float
)
