package com.example.pawtrack

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pawtrack.pages.AddCatPage
import com.example.pawtrack.pages.CatTrackPage
import com.example.pawtrack.pages.HomePage
import com.example.pawtrack.pages.LoginPage
import com.example.pawtrack.pages.SigninPage
import com.example.pawtrack.pages.WelcomePage
import com.example.pawtrack.viewmodel.AuthViewModel
import com.example.pawtrack.viewmodel.CatViewModel
import com.google.firebase.database.DatabaseReference

@Composable
fun PawtrackNavigation(modifier: Modifier = Modifier,authViewModel: AuthViewModel, catViewModel: CatViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "welcome", builder = {
        composable(route = "welcome"){
            WelcomePage(modifier, navController,authViewModel)
        }
        composable(route = "login"){
           LoginPage(modifier, navController,authViewModel)
       }
        composable(route = "signin"){
            SigninPage(modifier, navController,authViewModel)
        }
        composable(route = "home"){
            HomePage(modifier, navController,authViewModel, catViewModel)
        }
        composable(route = "addCat"){
            AddCatPage(modifier, navController,authViewModel, catViewModel)
        }
        composable(route = "catTrack"){
            CatTrackPage(modifier, navController,authViewModel)
        }

    } )

}