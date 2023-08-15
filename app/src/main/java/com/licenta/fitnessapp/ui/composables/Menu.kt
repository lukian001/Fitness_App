package com.licenta.fitnessapp.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.licenta.fitnessapp.data.Composables

abstract class Menu {
    protected lateinit var composable: MutableState<Int>

    @Composable
    fun Ui(composable: MutableState<Int>) {
        this.composable = composable
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            CustomUi()

            var extended by remember {
                mutableStateOf(false)
            }

            AddPaymentFab(
                Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 5.dp, bottom = 45.dp),
                extended
            )

            ExtendedFloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 10.dp, bottom = 10.dp),
                text = {
                    Text(text = "Close Menu", color = Color.White)
                },
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.KeyboardArrowUp,
                        contentDescription = "Close Menu",
                        tint = Color.White,
                    )
                },
                onClick = {
                    extended = !extended
                },
                expanded = extended,
                containerColor = MaterialTheme.colorScheme.secondary
            )
        }
    }

    @Composable
    private fun AddPaymentFab(
        modifier: Modifier,
        extended: Boolean
    ) {
        val density = LocalDensity.current

        AnimatedVisibility(
            modifier = modifier,
            visible = extended,
            enter = slideInHorizontally {
                with(density) { 40.dp.roundToPx() }
            } + fadeIn(),
            exit = fadeOut(
                animationSpec = keyframes {
                    this.durationMillis = 120
                }
            )
        ) {
            Column (
                modifier = modifier,
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                ExtendedFloatingActionButton(
                    text = {
                        if (composable.value == Composables.ACCOUNT.index) {
                            Text(text = "Entries", color = Color.White)
                        } else {
                            Text(text = "Account", color = Color.White)
                        }
                    },
                    icon = {
                        if (composable.value == Composables.ACCOUNT.index) {
                            Icon(
                                imageVector = Icons.Rounded.Check,
                                contentDescription = "Close Menu",
                                tint = Color.White,
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Rounded.AccountCircle,
                                contentDescription = "Close Menu",
                                tint = Color.White,
                            )
                        }
                    },
                    onClick = {
                        if (composable.value == Composables.ACCOUNT.index) {
                            composable.value = Composables.ENTRIES.index
                        } else {
                            composable.value = Composables.ACCOUNT.index
                        }
                    },
                    expanded = true,
                    containerColor = MaterialTheme.colorScheme.secondary
                )

                ExtendedFloatingActionButton(
                    text = {
                        if (composable.value == Composables.STEPCOUNTER.index) {
                            Text(text = "Entries", color = Color.White)
                        } else {
                            Text(text = "Step Counter", color = Color.White)
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Rounded.PlayArrow,
                            contentDescription = "Close Menu",
                            tint = Color.White,
                        )
                    },
                    onClick = {
                        if (composable.value == Composables.STEPCOUNTER.index) {
                            composable.value = Composables.ENTRIES.index
                        } else {
                            composable.value = Composables.STEPCOUNTER.index
                        }
                    },
                    expanded = true,
                    containerColor = MaterialTheme.colorScheme.secondary
                )

                ExtendedFloatingActionButton(
                    text = {
                        if (composable.value == Composables.QUESTIONS.index) {
                            Text(text = "Entries", color = Color.White)
                        } else {
                            Text(text = "Questions", color = Color.White)
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Rounded.Send,
                            contentDescription = "Close Menu",
                            tint = Color.White,
                        )
                    },
                    onClick = {
                        if (composable.value == Composables.QUESTIONS.index) {
                            composable.value = Composables.ENTRIES.index
                        } else {
                            composable.value = Composables.QUESTIONS.index
                        }
                    },
                    expanded = true,
                    containerColor = MaterialTheme.colorScheme.secondary
                )

                ExtendedFloatingActionButton(
                    text = {
                        FABText()
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = "Close Menu",
                            tint = Color.White,
                        )
                    },
                    onClick = {
                        FABLogic()
                    },
                    expanded = true,
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }

    @Composable
    abstract fun CustomUi()

    @Composable
    abstract fun FABText()

    abstract fun FABLogic()
}