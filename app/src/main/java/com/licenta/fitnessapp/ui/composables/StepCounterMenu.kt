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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.licenta.fitnessapp.logic.StepCounterManager
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import kotlin.random.Random

object StepCounterMenu: Menu() {
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    override fun CustomUi() {
        val wakedSteps = remember {
            mutableStateOf(0)
        }

        LaunchedEffect(Unit) {
            withContext(IO) {
                while (StepCounterManager.running.value) {
                    val myRandomValues = List(1) { Random.nextInt(0, 5) }
                    wakedSteps.value += myRandomValues[0]

                    Thread.sleep(1_000)
                }
            }
        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Spacer(Modifier.weight(0.2f))
                if (StepCounterManager.running.value) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = LocalDateTime.now().dayOfMonth.toString() + "/" + LocalDateTime.now().month //
                                + "/" + LocalDateTime.now().year + " - " + LocalDateTime.now().hour + ":" //
                                + returnMinutes(LocalDateTime.now().minute),
                        textAlign = TextAlign.Center,
                        fontSize = 20.sp
                    )
                } else {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "The step counter is not running at the moment!",
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp
                )
            }
                Spacer(Modifier.weight(0.5f))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Current Walked Steps: ${wakedSteps.value}",
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp
                )

                Spacer(Modifier.weight(1f))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    onClick = {
                        StepCounterManager.running.value = !StepCounterManager.running.value
                }) {
                    if (StepCounterManager.running.value) {
                        Text("Stop step counter")
                    } else {
                        Text("Start step counter")
                    }
                }
            }
        }
    }

    private fun returnMinutes(minute: Int): String {
        return if (minute <= 9) {
            "0$minute"
        } else {
            minute.toString()
        }
    }

    @Composable
    override fun FABText() {
    }

    override fun FABLogic() {
    }
}