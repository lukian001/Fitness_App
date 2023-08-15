package com.licenta.fitnessapp.ui.composables

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.licenta.fitnessapp.data.Composables

object AccountMenu: Menu() {
    @Composable
    override fun CustomUi() {

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