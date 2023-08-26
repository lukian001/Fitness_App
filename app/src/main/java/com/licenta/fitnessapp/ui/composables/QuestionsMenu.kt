package com.licenta.fitnessapp.ui.composables

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.licenta.fitnessapp.data.Composables
import com.licenta.fitnessapp.data.Question
import com.licenta.fitnessapp.data.QuestionTags
import com.licenta.fitnessapp.logic.Cache
import com.licenta.fitnessapp.logic.ServerLogic
import java.time.LocalDateTime

object QuestionsMenu: Menu() {
    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun CustomUi() {
        val filterTags = remember {
            mutableListOf<QuestionTags>()
        }

        ServerLogic.getFirst10Questions()

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Spacer(Modifier.height(10.dp))
                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = "Questions",
                    fontSize = 20.sp
                )
                Spacer(Modifier.height(10.dp))
                ChipList(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    selectedItems = filterTags
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

                if (dialogOpened.value) {
                    val titleValue = remember {
                        mutableStateOf("")
                    }

                    val contentValue = remember {
                        mutableStateOf("")
                    }

                    val selectedTags = remember {
                        mutableListOf<QuestionTags>()
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
                                    text = "Add new question",
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                OutlinedTextField(
                                    modifier = Modifier.fillMaxWidth(),
                                    value = titleValue.value.toString(),
                                    onValueChange = {
                                        titleValue.value = it
                                    },
                                    singleLine = true,
                                    maxLines = 1,
                                    label = {
                                        Text(text = "Title")
                                    },
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        keyboardType = KeyboardType.Text
                                    ),
                                )
                                OutlinedTextField(
                                    modifier = Modifier.fillMaxWidth(),
                                    value = contentValue.value,
                                    onValueChange = {
                                        contentValue.value = it
                                    },
                                    label = {
                                        Text(text = "Content")
                                    },
                                    minLines = 5,
                                    maxLines = 5,
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        keyboardType = KeyboardType.Text
                                    ),
                                )
                                ChipList(selectedItems = selectedTags)
                                Spacer(modifier = Modifier.height(20.dp))
                                Button(
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = {
                                        val question = Question(
                                            id = null,
                                            parentQuestion = "",
                                            date = LocalDateTime.now(),
                                            title = titleValue.value,
                                            content = contentValue.value,
                                            tags = selectedTags,
                                            userEmail = Firebase.auth.currentUser!!.email
                                        )
                                        ServerLogic.addQuestionEntry(question)
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
        modifier: Modifier = Modifier,
        selectedItems: MutableList<QuestionTags> = mutableListOf()
    ) {
        Row(
            modifier = modifier
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                val chipsSelectedRemember = mutableListOf<MutableState<Boolean>>()
                Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                    QuestionTags.values().forEachIndexed { index, item ->
                        chipsSelectedRemember.add(remember {
                            mutableStateOf(false)
                        })
                        FilterChip(
                            modifier = Modifier.padding(horizontal = 4.dp),
                            selected = chipsSelectedRemember[index].value,
                            onClick = {
                                if (selectedItems.contains(item)) {
                                    selectedItems.remove(item)
                                    chipsSelectedRemember[index].value = false
                                } else {
                                    selectedItems.add(item)
                                    chipsSelectedRemember[index].value = true
                                }
                            },
                            label = { Text(item.displayName) }
                        )
                    }
                }
            }
        }
    }

    @Composable
    override fun FABText() {
        Text(text = "New question")
    }

    override fun FABLogic() {
        dialogOpened.value = true
    }
}