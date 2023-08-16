package com.licenta.fitnessapp.logic

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.licenta.fitnessapp.data.Entry

object ServerLogic {
    fun addFoodEntry(entry: Entry) {
        Firebase.firestore.collection(entry.userId + "food")
            .add(hashMapOf(
                "date" to entry.date,
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
}