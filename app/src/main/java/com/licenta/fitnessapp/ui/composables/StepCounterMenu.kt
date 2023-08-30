package com.licenta.fitnessapp.ui.composables

import android.os.Build
import android.widget.Button
import android.widget.Space
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.licenta.fitnessapp.logic.StepCounterManager
import java.time.LocalDateTime

object StepCounterMenu: Menu() {
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    override fun CustomUi() {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Spacer(Modifier.weight(0.2f))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = LocalDateTime.now().toString(),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )
                Spacer(Modifier.weight(0.5f))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Current Walked Steps: 3",
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp
                )

                Spacer(Modifier.weight(1f))
                Button(
                    modifier = Modifier.fillMaxWidth()
                        .padding(5.dp),
                    onClick = {
                    if (!StepCounterManager.running.value) {
                        StepCounterManager.onResume()
                    } else {
                        StepCounterManager.stop()
                    }
                }) {
                    if (!StepCounterManager.running.value) {
                        Text("Stop step counter")
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