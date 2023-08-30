package com.licenta.fitnessapp.logic

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.compose.runtime.MutableState

object StepCounterManager {
    lateinit var eventListener: SensorEventListener
    fun onResume() {
        running.value = true
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        sensorManager?.registerListener(eventListener, stepSensor, SensorManager.SENSOR_DELAY_UI)
    }
    fun stop() {
        sensorManager?.unregisterListener(eventListener)
        currentSteps.value = 0
        previousTotalSteps.value = totalSteps.value
        running.value = false
    }

    val RECORD_REQUEST_CODE = 10
    lateinit var currentSteps: MutableState<Int>
    var sensorManager: SensorManager? = null
    lateinit var running: MutableState<Boolean>
    lateinit var totalSteps: MutableState<Float>
    lateinit var previousTotalSteps: MutableState<Float>
}