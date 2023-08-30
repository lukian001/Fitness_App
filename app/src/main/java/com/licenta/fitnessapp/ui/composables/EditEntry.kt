package com.licenta.fitnessapp.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.licenta.fitnessapp.data.Composables
import com.licenta.fitnessapp.data.Exercises
import com.licenta.fitnessapp.data.Food
import com.licenta.fitnessapp.logic.Cache
import com.licenta.fitnessapp.logic.ServerLogic

object EditEntry: Menu() {
    lateinit var readOnly: MutableState<Boolean>

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun CustomUi() {
        readOnly = remember {
            mutableStateOf(true)
        }

        val selectedFood = remember {
            mutableStateOf(Cache.selectedEntry!!.food)
        }

        val selectedExercises = remember {
            mutableStateOf(Cache.selectedEntry!!.exercise)
        }

        val portionValue = remember {
            mutableStateOf(Cache.selectedEntry!!.portion)
        }

        val quantityValue = remember {
            mutableStateOf(Cache.selectedEntry!!.grams)
        }

        val caloriesBurned = remember {
            mutableStateOf(Cache.selectedEntry!!.caloriesBurned)
        }

        Column (
            Modifier.fillMaxSize()
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Entry ID: ${Cache.selectedEntry!!.id}",
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(20.dp))

            if (Cache.selectedEntry!!.exercise != Exercises.PLEASESELECTEXERCISE) {
                EntriesMenu.ExerciseDropdownMenu(
                    Modifier.fillMaxWidth().padding(5.dp),
                    selectedExercises
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    value = caloriesBurned.value.toString(),
                    onValueChange = {
                        caloriesBurned.value = it.replace("-", "").toFloat()
                    },
                    label = {
                        Text(text = "Reps")
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    readOnly = readOnly.value
                )
                Spacer(modifier = Modifier.height(20.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 5.dp, start = 5.dp, end = 5.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 5.dp
                    ),
                ) {
                    Column(
                        modifier = Modifier.padding(15.dp)
                    ) {
                        Text(text = "Exercise Data")
                        Row( modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Calories:")
                            Text("" + selectedExercises.value.caloriesBurned * caloriesBurned.value)
                        }
                    }
                }
            } else {
                EntriesMenu.FoodDropdownMenu(
                    Modifier.fillMaxWidth().padding(5.dp),
                    selectedFood
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    value = portionValue.value.toString(),
                    onValueChange = {
                        portionValue.value = it.replace("-", "").toFloat()
                        quantityValue.value = portionValue.value * 100
                    },
                    label = {
                        Text(text = "Portion")
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    readOnly = readOnly.value
                )
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    value = quantityValue.value.toString(),
                    onValueChange = {
                        quantityValue.value = it.replace("-", "").toFloat()
                        portionValue.value = quantityValue.value / 100
                    },
                    label = {
                        Text(text = "Quantity in grams")
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    readOnly = readOnly.value
                )
                Spacer(modifier = Modifier.height(20.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 5.dp, start = 5.dp, end = 5.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 5.dp
                    ),
                ) {
                    Column(
                        modifier = Modifier.padding(15.dp)
                    ) {
                        Text(text = "Food Data")
                        Spacer(modifier = Modifier.height(12.dp))
                        Row( modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Calories:")
                            Text("" + selectedFood.value.calories * portionValue.value)
                        }
                        Row( modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Proteins:")
                            Text("" + selectedFood.value.proteins * portionValue.value)
                        }
                        Row( modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Lipides:")
                            Text("" + selectedFood.value.lipides * portionValue.value)
                        }
                        Row( modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Carbohydrates:")
                            Text("" + selectedFood.value.carbohydrates * portionValue.value)
                        }
                        Row( modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Fibers:")
                            Text("" + selectedFood.value.fibers * portionValue.value)
                        }
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                onClick = {
                    ServerLogic.deleteEntry(Cache.selectedEntry!!)
                    Cache.selectedEntry = null
                    composable.value = Composables.ENTRIES.index
                }
            ) {
                Text("Delete entry")
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                onClick = {
                    readOnly.value = !readOnly.value
                }
            ) {
                if (readOnly.value) {
                    Text("Edit")
                } else {
                    Text("Save changes")
                }

            }
        }
    }

    @Composable
    override fun FABText() {
        Text(text = "Go Back")
    }

    override fun FABLogic() {
        Cache.selectedEntry = null
        composable.value = Composables.ENTRIES.index
    }
}