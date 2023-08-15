package com.licenta.fitnessapp.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.modifier.modifierLocalMapOf

object EntriesMenu: Menu() {
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
        TODO("Not yet implemented")
    }
}
