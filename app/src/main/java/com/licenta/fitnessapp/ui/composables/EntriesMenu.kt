package com.licenta.fitnessapp.ui.composables

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.himanshoe.kalendar.Kalendar
import com.himanshoe.kalendar.KalendarEvents
import com.himanshoe.kalendar.KalendarType
import com.himanshoe.kalendar.color.KalendarColors
import com.himanshoe.kalendar.ui.component.day.KalendarDayKonfig
import com.himanshoe.kalendar.ui.firey.DaySelectionMode
import com.licenta.fitnessapp.data.Food
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.licenta.fitnessapp.data.Composables
import com.licenta.fitnessapp.data.Entry
import com.licenta.fitnessapp.data.Exercises
import com.licenta.fitnessapp.logic.Cache
import com.licenta.fitnessapp.logic.ServerLogic
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.LocalDateTime

object EntriesMenu: Menu() {
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    override fun CustomUi() {
        val todayTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime()

        ServerLogic.getEntriesForDate(todayTime.toLocalDate().atStartOfDay())

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Kalendar(
                    currentDay = todayTime.toKotlinLocalDateTime().date,
                    kalendarType = KalendarType.Oceanic,
                    events = KalendarEvents(),
                    kalendarColors = KalendarColors.default(),
                    kalendarDayKonfig = KalendarDayKonfig.default(),
                    daySelectionMode = DaySelectionMode.Single,
                    onDayClick = { selectedDay, events ->
                        ServerLogic.getEntriesForDate(
                            selectedDay.atStartOfDayIn(TimeZone.currentSystemDefault())
                                .toLocalDateTime(TimeZone.currentSystemDefault())
                                .toJavaLocalDateTime()
                        )
                    }
                )
                LazyColumn (
                    modifier = Modifier.fillMaxHeight()
                ) {
                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                    items(Cache.entries) {
                            item -> ItemCard(item)
                    }
                }

                AddEntry()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AddEntry() {
        if (dialogOpened.value) {
            val state = remember {
                mutableStateOf(true)
            }
            val selectedFood = remember {
                mutableStateOf(Food.values()[0])
            }
            val selectedExercises = remember {
                mutableStateOf(Exercises.values()[0])
            }
            val portionValue = remember {
                mutableStateOf(1f)
            }
            val quantityValue = remember {
                mutableStateOf(100f)
            }
            val caloriesBurned = remember {
                mutableStateOf(1f)
            }

            AlertDialog(
                onDismissRequest = {
                    dialogOpened.value = false
                }
            ) {
                Surface(
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight(),
                    shape = MaterialTheme.shapes.large,
                    tonalElevation = AlertDialogDefaults.TonalElevation
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Add new entry",
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(Modifier.selectableGroup()) {
                            RadioButton(
                                selected = state.value,
                                onClick = { state.value = true },
                                modifier = Modifier.semantics { contentDescription = "Sel food" }
                            )
                            Text("Food")
                            RadioButton(
                                selected = !state.value,
                                onClick = { state.value = false },
                                modifier = Modifier.semantics { contentDescription = "Sel exercise" }
                            )
                            Text("Exercise")
                        }
                        if (state.value) {
                            FoodSelection(selectedFood, portionValue, quantityValue)
                        } else {
                            ExerciseSelection(selectedExercises, caloriesBurned)
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                val entry = Entry(
                                    null,
                                    date = LocalDateTime.now(),
                                    food = selectedFood.value,
                                    exercise = selectedExercises.value,
                                    caloriesBurned = caloriesBurned.value,
                                    userId = Firebase.auth.currentUser!!.uid,
                                    portion = portionValue.value,
                                    grams = quantityValue.value
                                )
                                ServerLogic.addFoodEntry(entry)
                                dialogOpened.value = false

                            }
                        ) {
                            Text(text = "Save entry")
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun ExerciseSelection(
        selectedExercises: MutableState<Exercises>,
        caloriesBurned: MutableState<Float>
    ) {
        ExerciseDropdownMenu(
            Modifier,
            selectedExercises
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
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
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(text = "Exercise Data")
        Row( modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Calories:")
            Text("" + selectedExercises.value.caloriesBurned * caloriesBurned.value)
        }
    }

    @Composable
    private fun FoodSelection(
        selectedFood: MutableState<Food>,
        portionValue: MutableState<Float>,
        quantityValue: MutableState<Float>
    ) {
        FoodDropdownMenu(
            Modifier,
            selectedFood
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
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
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
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
        )
        Spacer(modifier = Modifier.height(20.dp))
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

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun FoodDropdownMenu(
        modifier: Modifier,
        selectedFood: MutableState<Food>) {
        var isExpanded by remember {
            mutableStateOf(false)
        }

        ExposedDropdownMenuBox(
            modifier = modifier,
            expanded = isExpanded,
            onExpandedChange = {
                isExpanded = it
            }
        ) {
            OutlinedTextField(
                value = selectedFood.value.displayName,
                label = {
                        Text(text = "Food")
                },
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = {
                    isExpanded = false
                }
            ) {
               Food.values().forEach {
                   if (it != Food.PLEASESELECTFOOD) {
                       DropdownMenuItem(
                           text = {
                                  Text(text = it.displayName)
                           },
                           onClick = {
                               selectedFood.value = it
                               isExpanded = false
                           }
                       )
                   }
               }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ExerciseDropdownMenu(
        modifier: Modifier,
        selectedExercises: MutableState<Exercises>
    ) {
        var isExpanded by remember {
            mutableStateOf(false)
        }

        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = {
                isExpanded = it
            },
            modifier = modifier,
        ) {
            OutlinedTextField(
                value = selectedExercises.value.displayName,
                label = {
                    Text(text = "Exercise")
                },
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = {
                    isExpanded = false
                }
            ) {
                Exercises.values().forEach {
                    if (it != Exercises.PLEASESELECTEXERCISE) {
                        DropdownMenuItem(
                            text = {
                                Text(text = it.displayName)
                            },
                            onClick = {
                                selectedExercises.value = it
                                isExpanded = false
                            }
                        )
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun ItemCard(item: Entry) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 5.dp, start = 5.dp, end = 5.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp
            ),
            onClick = {
                Cache.selectedEntry = item
                composable.value = Composables.EDITENTRY.index
            }
        ) {
            Column(
                modifier = Modifier.padding(15.dp)
            ) {
                Text(
                    if (item.food == Food.PLEASESELECTFOOD) {
                        item.exercise!!.displayName
                    } else {
                        item.food!!.displayName
                    }
                )
                Text(
                    if (item.food == Food.PLEASESELECTFOOD) {
                        item.exercise!!.caloriesBurned.toString()
                    } else {
                        item.food.calories.toString()
                    }
                )
            }
        }
    }

    @Composable
    override fun FABText() {
        Text("New entry")
    }

    override fun FABLogic() {
        dialogOpened.value = true
    }
}
