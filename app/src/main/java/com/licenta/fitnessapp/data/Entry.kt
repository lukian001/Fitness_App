package com.licenta.fitnessapp.data

import java.time.LocalDateTime

data class Entry(
    val date: LocalDateTime,
    val food: Food
)
