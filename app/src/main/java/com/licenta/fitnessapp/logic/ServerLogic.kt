package com.licenta.fitnessapp.logic

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.licenta.fitnessapp.data.Entry
import com.licenta.fitnessapp.data.Exercises
import com.licenta.fitnessapp.data.Food
import com.licenta.fitnessapp.data.Question
import com.licenta.fitnessapp.data.QuestionTags
import com.licenta.fitnessapp.data.Step
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
                "food" to entry.food,
                "exercise" to entry.exercise,
                "caloriesBurned" to entry.caloriesBurned,
                "portion" to entry.portion,
                "qty" to entry.grams
            )).addOnSuccessListener { documentReference ->
                Log.d("Food save", "DocumentSnapshot added with ID: ${documentReference.id}")
                Cache.entries.add(0, entry)
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
                "tags" to getTags(question.tags),
                "user" to question.userEmail
            )).addOnSuccessListener { documentReference ->
                Log.d("Food save", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("Food save", "Error adding document", e)
            }
    }
    private fun getTags(tags: List<QuestionTags>): String? {
        var tagString = ""
        for (tag in tags) {
            tagString += "$tag"
            if (tags.indexOf(tag) != tags.size - 1) tagString += ","
        }
        return tagString
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
                        getFood(document.data["food"].toString()),
                        getExersise(document.data["exercise"].toString()),
                        document.data["caloriesBurned"].toString().toFloat(),
                        Firebase.auth.currentUser!!.uid,
                        document.data["portion"].toString().toFloat(),
                        document.data["qty"].toString().toFloat()
                    ))
                }
             }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getSteps() {
        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + "step")
            .orderBy("date", Query.Direction.DESCENDING).get()
            .addOnSuccessListener {
                Cache.stepsHistory.clear()

                for (document in it) {
                    Log.w("Food save", "DocumentSnapshot added with ID: ${document.id}")
                    Cache.stepsHistory.add(
                        Step(
                            document.id,
                            LocalDateTime.parse(document.data["date"].toString()),
                            document.data["walkedSteps"].toString().toInt()
                        )
                    )
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getQuestionsForUser(email: String) {
        Firebase.firestore.collection("questions")
            .whereEqualTo("user", email)
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
                            tags = getTags(document.data["tags"].toString()),
                            userEmail = document.data["user"].toString()
                        )
                    )
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getFirst10Questions() {
        Firebase.firestore.collection("questions")
            .whereEqualTo("parent", "")
            .limit(10)
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
                        tags = getTags(document.data["tags"].toString()),
                        userEmail = document.data["user"].toString()
                        )
                    )
                }
            }
    }
    private fun getFood(foodString: String): Food {
        return Food.valueOf(foodString)
    }
    private fun getExersise(exercises: String): Exercises {
        return Exercises.valueOf(exercises)
    }
    private fun getTags(tagString: String): List<QuestionTags> {
        val tagsList = mutableListOf<QuestionTags>()
        val tagsAsString = tagString.split(",")
        for(tagAsString in tagsAsString) {
            tagsList.add(QuestionTags.valueOf(tagAsString))
        }
        return tagsList
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun getCommentsForQuestion(id: String?) {
        Firebase.firestore.collection("questions")
            .whereEqualTo("parent", id)
            .get()
            .addOnSuccessListener {
                Cache.commentsForQuestion.clear()

                for (document in it) {
                    Log.w("Food save", "DocumentSnapshot added with ID: ${document.id}")
                    Cache.commentsForQuestion.add(
                        Question(
                            id = document.id,
                            date = LocalDateTime.parse(document.data["date"].toString()),
                            title = document.data["title"].toString(),
                            content = document.data["content"].toString(),
                            parentQuestion = document.data["parent"].toString(),
                            tags = getTags(document.data["tags"].toString()),
                            userEmail = document.data["user"].toString()
                        )
                    )
                }
            }
    }
    fun addComment(question: Question) {
        Firebase.firestore.collection("questions")
            .add(hashMapOf(
                "date" to question.date.toString(),
                "title" to question.title,
                "content" to question.content,
                "parent" to question.parentQuestion,
                "tags" to getTags(question.tags),
                "user" to question.userEmail
            )).addOnSuccessListener { documentReference ->
                Log.d("Food save", "DocumentSnapshot added with ID: ${documentReference.id}")
                Cache.commentsForQuestion.add(0, question)
            }
            .addOnFailureListener { e ->
                Log.w("Food save", "Error adding document", e)
            }
    }
    fun deleteEntry(selectedEntry: Entry) {
        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + "food")
            .document(selectedEntry.id!!)
            .delete()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getEntriesForDates(timeForDb: String, timeForDb1: String) {
        Firebase.firestore.collection(Firebase.auth.currentUser!!.uid + "food")
            .whereGreaterThanOrEqualTo("date", timeForDb)
            .whereLessThan("date", timeForDb1)
            .orderBy("date", Query.Direction.DESCENDING).get()
            .addOnSuccessListener {
                Cache.entries.clear()

                for (document in it) {
                    Log.w("Food save", "DocumentSnapshot added with ID: ${document.id}")
                    Cache.entries.add(Entry(
                        document.id,
                        LocalDateTime.parse(document.data["date"].toString()),
                        getFood(document.data["food"].toString()),
                        getExersise(document.data["exercise"].toString()),
                        document.data["caloriesBurned"].toString().toFloat(),
                        Firebase.auth.currentUser!!.uid,
                        document.data["portion"].toString().toFloat(),
                        document.data["qty"].toString().toFloat()
                    ))
                }
            }
    }
}