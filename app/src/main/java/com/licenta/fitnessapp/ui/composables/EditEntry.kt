package com.licenta.fitnessapp.ui.composables

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.licenta.fitnessapp.data.Composables
import com.licenta.fitnessapp.logic.Cache

class EditEntry(val readOnly: Boolean): Menu() {
    @Composable
    override fun CustomUi() {
        TODO("Not yet implemented")
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