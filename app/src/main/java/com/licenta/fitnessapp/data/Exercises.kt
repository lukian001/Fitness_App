package com.licenta.fitnessapp.data

enum class Exercises (
    val displayName: String,
    val caloriesBurned: Float
) {
    PLEASESELECTEXERCISE("Please select an exercise", 0f),
    PUSHUPS("Push Ups", 1f),
    PULLUPS("Pull Ups", 2.3f),
    CRUNCHES("Crunches", 0.5f);
}
