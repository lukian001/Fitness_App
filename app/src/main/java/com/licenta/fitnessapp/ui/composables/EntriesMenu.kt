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
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.Surface
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.licenta.fitnessapp.data.Entry
import com.licenta.fitnessapp.logic.ServerLogic
import java.time.LocalDateTime

object EntriesMenu: Menu() {
    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun CustomUi() {
        val today: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Kalendar(
                    currentDay = today,
                    kalendarType = KalendarType.Oceanic,
                    events = KalendarEvents(),
                    kalendarColors = KalendarColors.default(),
                    kalendarDayKonfig = KalendarDayKonfig.default(),
                    daySelectionMode = DaySelectionMode.Single,
                    onDayClick = { selectedDay, events ->
                        // Handle day click event
                    }
                )
                LazyColumn (
                    modifier = Modifier.fillMaxHeight()
                ) {
                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                    items(Food.values()) {
                            item -> ItemCard(item)
                    }
                }

                if (dialogOpened.value) {
                    val selectedFood = remember {
                        mutableStateOf(Food.values()[0])
                    }
                    val portionValue = remember {
                        mutableStateOf(1f)
                    }
                    val quantityValue = remember {
                        mutableStateOf(100f)
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
                                FoodDropdownMenu(selectedFood)
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
                                Spacer(modifier = Modifier.height(20.dp))
                                Button(
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = {
                                        val entry = Entry(
                                            date = LocalDateTime.now(),
                                            food = selectedFood.value,
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
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun FoodDropdownMenu(selectedFood: MutableState<Food>) {
        var isExpanded by remember {
            mutableStateOf(false)
        }

        ExposedDropdownMenuBox(
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

    @Composable
    private fun ItemCard(item: Food) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 5.dp, start = 5.dp, end = 5.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(15.dp)
            ) {
                Text(
                    item.displayName
                )
                Text(
                    item.calories.toString()
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
