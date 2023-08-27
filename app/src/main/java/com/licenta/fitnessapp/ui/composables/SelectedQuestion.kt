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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.licenta.fitnessapp.data.Composables
import com.licenta.fitnessapp.data.Question
import com.licenta.fitnessapp.data.QuestionTags
import com.licenta.fitnessapp.logic.Cache
import com.licenta.fitnessapp.logic.ServerLogic
import java.time.LocalDateTime

object SelectedQuestion: Menu() {
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    override fun CustomUi() {
        ServerLogic.getCommentsForQuestion(Cache.selectedQuestion!!.id)

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 5.dp
                    )
                ) {
                    Text(Cache.selectedQuestion!!.title)
                    Text("Posted by ${Cache.selectedQuestion!!.userEmail}")
                    Text(Cache.selectedQuestion!!.content)

                    var comment by remember { mutableStateOf("") }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = comment,
                            onValueChange = { comment = it },
                            modifier = Modifier.weight(1f),
                            label = { Text("Comment") }
                        )

                        Spacer(Modifier.width(5.dp))

                        Button(
                            onClick = {
                                comment = ""
                                val commentAdd = Question(
                                    id = null,
                                    parentQuestion = Cache.selectedQuestion!!.id!!,
                                    date = LocalDateTime.now(),
                                    title = "",
                                    content = comment,
                                    tags = QuestionTags.values().toList(),
                                    userEmail = Firebase.auth.currentUser!!.email
                                )
                                ServerLogic.addComment(commentAdd)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Play Button",
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                }

                LazyColumn (
                    modifier = Modifier.fillMaxHeight()
                ) {
                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                    items(Cache.commentsForQuestion) {
                            item -> ItemCard(item)
                    }
                }
            }
        }
    }

    @Composable
    private fun ItemCard(item: Question) {
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
                    "${item.userEmail} commented:"
                )
                Text(
                    item.content
                )
            }
        }
    }

    @Composable
    override fun FABText() {
        Text(text = "Go Back")
    }

    override fun FABLogic() {
        Cache.selectedQuestion = null
        composable.value = Composables.QUESTIONS.index
    }
}