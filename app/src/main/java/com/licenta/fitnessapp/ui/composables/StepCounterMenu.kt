package com.licenta.fitnessapp.ui.composables

import android.widget.Button
import android.widget.Space
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.licenta.fitnessapp.logic.StepCounterManager

object StepCounterMenu: Menu() {
    @Composable
    override fun CustomUi() {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Text("Current steps ${StepCounterManager.currentSteps.value}")
                Spacer(Modifier.weight(1f))
                Button(onClick = {
                    if (!StepCounterManager.running.value) {
                        StepCounterManager.onResume()
                    } else {
                        StepCounterManager.stop()
                    }
                }) {
                    if (!StepCounterManager.running.value) {
                        Text("Start step counter")
                    } else {
                        Text("Stop step counter")
                    }
                }
            }
        }
    }

    @Composable
    override fun FABText() {
        TODO("Not yet implemented")
    }

    override fun FABLogic() {
        TODO("Not yet implemented")
    }
}