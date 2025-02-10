package com.example.pawtrack

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Scaffold
import androidx.compose.ui.platform.LocalContext
import com.example.pawtrack.ui.theme.PawtrackTheme

class AccountUI: ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PawtrackTheme {
                val context = LocalContext.current
                Scaffold (
                    topBar = {

                    }
                ){ innerPadding ->


                }



            }
        }
    }
}