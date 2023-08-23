package com.licenta.fitnessapp.data

import java.time.LocalDateTime

data class Question(
    val id: String?,
    val parentQuestion: String,
    val date: LocalDateTime,
    val title: String,
    val content: String,
    val tags: List<QuestionTags>,
    val userEmail: String?
)
