package com.licenta.fitnessapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.licenta.fitnessapp.data.Composables
import com.licenta.fitnessapp.logic.StepCounterManager
import com.licenta.fitnessapp.ui.composables.AccountMenu
import com.licenta.fitnessapp.ui.composables.EditEntry
import com.licenta.fitnessapp.ui.composables.Login
import com.licenta.fitnessapp.ui.composables.EntriesMenu
import com.licenta.fitnessapp.ui.composables.QuestionsMenu
import com.licenta.fitnessapp.ui.composables.Register
import com.licenta.fitnessapp.ui.composables.SelectedQuestion
import com.licenta.fitnessapp.ui.composables.StepCounterMenu
import com.licenta.fitnessapp.ui.theme.FitnessAppTheme

class MainActivity : ComponentActivity(), SensorEventListener {
    @RequiresApi(Build.VERSION_CODES.Q)
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StepCounterManager.sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        StepCounterManager.eventListener = this

        setContent {
            FitnessAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if(ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){
                        //ask for permission
                        ActivityCompat.requestPermissions(this,
                            arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                            StepCounterManager.RECORD_REQUEST_CODE)
                    }

                    StepCounterManager.currentSteps = remember {
                        mutableStateOf(0)
                    }
                    StepCounterManager.running = remember {
                        mutableStateOf(false)
                    }
                    StepCounterManager.totalSteps = remember {
                        mutableStateOf(0f)
                    }
                    StepCounterManager.previousTotalSteps = remember {
                        mutableStateOf(0f)
                    }

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
                        Composables.SELECTEDQUESTION.index -> SelectedQuestion.Ui(composable)
                        Composables.EDITENTRY.index -> EditEntry.Ui(composable)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
//        StepCounterManager.onResume(this)
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        if (StepCounterManager.running.value) {
            StepCounterManager.totalSteps.value = p0!!.values[0]
            StepCounterManager.currentSteps.value = StepCounterManager.totalSteps.value.toInt() - StepCounterManager.previousTotalSteps.value.toInt()
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }
}