package com.licenta.fitnessapp.logic

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.licenta.fitnessapp.data.Entry
import com.licenta.fitnessapp.data.Question
import com.licenta.fitnessapp.data.Step

object Cache: ViewModel() {
    val stepsHistory = mutableStateListOf<Step>()
    var selectedEntry: Entry? = null
    var entries = mutableStateListOf<Entry>()
    var questions = mutableStateListOf<Question>()
    var selectedQuestion: Question? = null
    var commentsForQuestion = mutableStateListOf<Question>()
}