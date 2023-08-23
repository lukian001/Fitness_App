package com.licenta.fitnessapp.logic

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.licenta.fitnessapp.data.Entry
import com.licenta.fitnessapp.data.Question
import com.licenta.fitnessapp.data.QuestionTags
import com.licenta.fitnessapp.data.stringToFood
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.LocalDateTime

object ServerLogic {
    fun addFoodEntry(entry: Entry) {
        Firebase.firestore.collection(entry.userId + "food")
            .add(hashMapOf(
                "date" to entry.date.toString(),
                "food" to entry.food.displayName,
                "portion" to entry.portion,
                "qty" to entry.grams
            )).addOnSuccessListener { documentReference ->
                Log.d("Food save", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("Food save", "Error adding document", e)
            }
    }

    fun addQuestionEntry(question: Question) {
        Firebase.firestore.collection("questions")
            .add(hashMapOf(
                "date" to question.date.toString(),
                "title" to question.title,
                "content" to question.content,
                "parent" to question.parentQuestion,
                "tags" to question.tags,
                "user" to question.userEmail
            )).addOnSuccessListener { documentReference ->
                Log.d("Food save", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("Food save", "Error adding document", e)
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getEntriesForDate(selectedDay: LocalDateTime) {
        val nextDay = selectedDay.toKotlinLocalDateTime()
            .toInstant(TimeZone.currentSystemDefault())
            .plus(1, DateTimeUnit.DAY, TimeZone.currentSystemDefault())
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .toJavaLocalDateTime()

        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + "food")
            .whereGreaterThanOrEqualTo("date", selectedDay.toString())
            .whereLessThan("date", nextDay.toString())
            .orderBy("date", Query.Direction.DESCENDING).get()
            .addOnSuccessListener {
                Cache.entries.clear()

                for (document in it) {
                    Log.w("Food save", "DocumentSnapshot added with ID: ${document.id}")
                    Cache.entries.add(Entry(
                        document.id,
                        LocalDateTime.parse(document.data["date"].toString()),
                        stringToFood(document.data["food"].toString()),
                        Firebase.auth.currentUser!!.uid,
                        document.data["portion"].toString().toFloat(),
                        document.data["qty"].toString().toFloat()
                    ))
                }
             }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getFirst5Questions() {
        Firebase.firestore.collection("questions")
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                Cache.questions.clear()

                for (document in it) {
                    Log.w("Food save", "DocumentSnapshot added with ID: ${document.id}")
                    Cache.questions.add(
                        Question(
                        id = document.id,
                        date = LocalDateTime.parse(document.data["date"].toString()),
                        title = document.data["title"].toString(),
                        content = document.data["content"].toString(),
                        parentQuestion = document.data["parent"].toString(),
                        tags = parseTags(arrayOf(document.data["tags"])),
                        userEmail = document.data["user"].toString()
                        )
                    )
                }
            }
    }

    private fun parseTags(arrayOf: Array<Any?>): List<QuestionTags> {
        for(it in arrayOf) {
            val da = it.toString()
        }

        return listOf()
    }
}