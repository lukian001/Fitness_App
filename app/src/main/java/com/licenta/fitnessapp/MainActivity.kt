package com.licenta.fitnessapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.licenta.fitnessapp.data.Composables
import com.licenta.fitnessapp.ui.composables.AccountMenu
import com.licenta.fitnessapp.ui.composables.Login
import com.licenta.fitnessapp.ui.composables.EntriesMenu
import com.licenta.fitnessapp.ui.composables.QuestionsMenu
import com.licenta.fitnessapp.ui.composables.Register
import com.licenta.fitnessapp.ui.composables.StepCounterMenu
import com.licenta.fitnessapp.ui.theme.FitnessAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FitnessAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var initialValue = 2
                    if (Firebase.auth.currentUser == null) initialValue = 0

                    val composable = remember {
                        mutableStateOf(initialValue)
                    }

                    when (composable.value) {
                        Composables.LOGIN.index -> Login.Ui(composable)
                        Composables.REGISTER.index -> Register.Ui(composable)
                        Composables.ENTRIES.index -> EntriesMenu.Ui(composable)
                        Composables.QUESTIONS.index -> QuestionsMenu.Ui(composable)
                        Composables.STEPCOUNTER.index -> StepCounterMenu.Ui(composable)
                        Composables.ACCOUNT.index -> AccountMenu.Ui(composable)
                    }
                }
            }
        }
    }
}