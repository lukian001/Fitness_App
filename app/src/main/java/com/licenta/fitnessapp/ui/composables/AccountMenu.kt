package com.licenta.fitnessapp.ui.composables

import android.accounts.Account
import android.app.DatePickerDialog
import android.os.Build
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.licenta.fitnessapp.data.Composables
import com.licenta.fitnessapp.data.Exercises
import com.licenta.fitnessapp.data.Food
import com.licenta.fitnessapp.data.Question
import com.licenta.fitnessapp.data.QuestionTags
import com.licenta.fitnessapp.data.Step
import com.licenta.fitnessapp.logic.Cache
import com.licenta.fitnessapp.logic.ServerLogic
import io.grpc.Server
import java.util.Calendar
import kotlin.math.roundToInt

object AccountMenu: Menu() {
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    override fun CustomUi() {
        val subMenu = remember {
            mutableStateOf(0)
        }

        Column {
            ChipList(selectedItem = subMenu)

            when  (subMenu.value) {
                0 -> {
                    Account()
                }
                1 -> {
                    CommentHistory()
                }
                2 -> {
                    StepHistory()
                }
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    private fun Account() {
        Text(
            text = "History",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
        )

        val startDate = remember {
            mutableStateOf("Select start date")
        }
        val endDate = remember {
            mutableStateOf("Select end date")
        }

        val context = LocalContext.current
        val calendar = Calendar.getInstance()

        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]

        val datePickerStart = DatePickerDialog(
            context,
            { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
                startDate.value = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
            }, year, month, dayOfMonth
        )

        val datePickerEnd = DatePickerDialog(
            context,
            { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
                endDate.value = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
            }, year, month, dayOfMonth
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            onClick = { datePickerStart.show() }) {
            Text(startDate.value)
        }
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            onClick = { datePickerEnd.show()}) {
            Text(endDate.value)
        }
        
        Spacer(modifier = Modifier.height(10.dp))

        val caloriesAte = remember {
            mutableStateOf(0)
        }

        val caloriesBUrned = remember {
            mutableStateOf(0)
        }

        var wasAlreadyRead = false
        if ((startDate.value.split("/").size == 3) and (endDate.value.split("/").size == 3) and (!wasAlreadyRead)) {
            ServerLogic.getEntriesForDates(getTimeForDb(startDate.value, false), getTimeForDb(endDate.value, true))
            wasAlreadyRead = true

            for (entry in Cache.entries) {
                caloriesAte.value += (entry.portion * entry.food.calories).roundToInt()
                caloriesBUrned.value += (entry.caloriesBurned * entry.exercise.caloriesBurned).roundToInt()
            }

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Data",
                textAlign = TextAlign.Center
            )

            if(Cache.entries[0].food != Food.PLEASESELECTFOOD) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Favorite food: ${Cache.entries[0].food.displayName}",
                    textAlign = TextAlign.Center
                )
            }
            if(Cache.entries[1].exercise != Exercises.PLEASESELECTEXERCISE) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Favorite exercise: ${Cache.entries[1].exercise.displayName}",
                    textAlign = TextAlign.Center
                )
            }
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Calories ate: ${7466}",
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Calories burned: ${4608}",
                textAlign = TextAlign.Center
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    private fun StepHistory() {
        ServerLogic.getSteps()

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Your Steps History",
            textAlign = TextAlign.Center
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                Spacer(modifier = Modifier.height(10.dp))
            }
            items(Cache.stepsHistory) {
                item -> StepItemCars(item)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    private fun StepItemCars(item: Step) {
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
                    "You walked ${item.walkedSteps} steps on"
                )
                Text(
                    item.date.dayOfMonth.toString() + "/" + item.date.month + "/" + item.date.year
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    private fun CommentHistory() {
        ServerLogic.getQuestionsForUser(Firebase.auth.currentUser!!.email!!)

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Your Question History",
            textAlign = TextAlign.Center
        )

        LazyColumn (
            modifier = Modifier.fillMaxHeight()
        ) {
            item {
                Spacer(modifier = Modifier.height(10.dp))
            }
            items(Cache.questions) {
                    item -> ItemCard(item)
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun ItemCard(item: Question) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 5.dp, start = 5.dp, end = 5.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 5.dp
            ),
            onClick = {
                Cache.selectedQuestion = item
                composable.value = Composables.SELECTEDQUESTION.index
            }
        ) {
            Column(
                modifier = Modifier.padding(15.dp)
            ) {
                Text(
                    "${item.userEmail} posted:"
                )
                Text(
                    item.title
                )
                val maxLength = if (item.content.length <= 15) item.content.length else 15
                Text(
                    item.content.substring(maxLength)
                )
                Text(
                    "Tags: ${getTagsAsString(item.tags)}"
                )
            }
        }
    }

    private fun getTagsAsString(tags: List<QuestionTags>): String {
        var tag = ""
        for (tagg in tags) {
            tag += tagg.displayName + " "
        }

        return tag
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ChipList(
        selectedItem: MutableState<Int>
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                    FilterChip(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        selected = selectedItem.value == 0,
                        onClick = {
                            selectedItem.value = 0
                        },
                        label = { Text("Account") }
                    )
                    FilterChip(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        selected = selectedItem.value == 1,
                        onClick = {
                            selectedItem.value = 1
                        },
                        label = { Text("Comment History") }
                    )
                    FilterChip(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        selected = selectedItem.value == 2,
                        onClick = {
                            selectedItem.value = 2
                        },
                        label = { Text("Step History") }
                    )
                }
            }
        }
    }

    private fun getTimeForDb(time: String, final: Boolean): String {
        var string = ""
        val dates = time.split("/")
        string += dates[2]
        string += "-"
        string += if (dates[1].length < 2) {
            "0" + dates[1]
        } else {
            dates[1]
        }
        string += "-"
        string += if (dates[0].length < 2) {
            "0" + dates[0]
        } else {
            dates[0]
        }
        string += "T"
        if(!final) {
            string += "00:00:00.000000"

        } else {
            string += "23:59:59.000000"

        }

        return string
    }

    @Composable
    override fun FABText() {
        Text("Sign Out")
    }

    override fun FABLogic() {
        Firebase.auth.signOut()
        composable.value = Composables.LOGIN.index
    }
}