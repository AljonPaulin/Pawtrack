 package com.example.pawtrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.pawtrack.ui.theme.PawtrackTheme
import com.example.pawtrack.viewmodel.AuthViewModel
import com.example.pawtrack.viewmodel.CatViewModel

 class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val authViewModel : AuthViewModel by viewModels()
        val catViewModel : CatViewModel by viewModels()

        setContent {
            PawtrackTheme {
               PawtrackNavigation(authViewModel = authViewModel, catViewModel = catViewModel)
            }
        }
    }
}








