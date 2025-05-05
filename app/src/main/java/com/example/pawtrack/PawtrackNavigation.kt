package com.example.pawtrack

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pawtrack.pages.AccountPage
import com.example.pawtrack.pages.AddDogPage
import com.example.pawtrack.pages.DogTrackPage
import com.example.pawtrack.pages.EditDogPage
import com.example.pawtrack.pages.FullScreenTrack
import com.example.pawtrack.pages.HelpPage
import com.example.pawtrack.pages.HistoryPage
import com.example.pawtrack.pages.HomePage
import com.example.pawtrack.pages.LoginPage
import com.example.pawtrack.pages.SigninPage
import com.example.pawtrack.pages.Store
import com.example.pawtrack.pages.VerifyPage
import com.example.pawtrack.pages.WelcomePage
import com.example.pawtrack.viewmodel.AuthViewModel
import com.example.pawtrack.viewmodel.DogViewModel
import org.osmdroid.util.GeoPoint

@Composable
fun PawtrackNavigation(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel, dogViewModel: DogViewModel, onSendSms: (String, String) -> Unit
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "welcome", builder = {
        composable(route = "welcome"){
            WelcomePage(modifier, navController ,authViewModel)
        }
        composable(route = "login"){
           LoginPage(modifier, navController ,authViewModel)
       }
        composable(route = "account"){
            AccountPage(modifier, navController ,authViewModel,)
        }
        composable(route = "signin"){
            SigninPage(modifier, navController,authViewModel)
        }
        composable(route = "verify/{fullPhone}/{randomCode}"){
            val fullPhone = it.arguments?.getString("fullPhone")
            val randomCode = it.arguments?.getString("randomCode")
            if (fullPhone != null) {
                if (randomCode != null) {
                    onSendSms(fullPhone, randomCode)
                    VerifyPage(modifier, navController,authViewModel, randomCode)
                }
            }
        }
        composable(route = "help"){
            HelpPage(modifier, navController,authViewModel)
        }
        composable(route = "home"){
            HomePage(modifier, navController,authViewModel, dogViewModel)
        }
        composable(route = "addDog"){
            AddDogPage(modifier, navController,authViewModel, dogViewModel)
        }
        composable(route = "history"){
            HistoryPage(modifier, navController,authViewModel, dogViewModel)
        }
        composable(route = "store"){
            Store(modifier)
        }
        composable(route = "fullScreenTrack/{dogId}/{currentLocation}"){
            val dogId = it.arguments?.getString("dogId")
            val locationString = it.arguments?.getString("currentLocation")
            var locationGeopoint: GeoPoint? = null

            locationString?.let { loc ->
                val parts = loc.split(",")
                locationGeopoint = GeoPoint(parts[0].toDouble(), parts[1].toDouble())
            }

            if (dogId != null) {
                locationGeopoint?.let { loc ->
                    FullScreenTrack(modifier, navController, dogId,
                        loc
                    )
                }
            }
        }
        composable(route = "dogTrack/{dogId}"){
            val dogId = it.arguments?.getString("dogId")
            if (dogId != null) {
                DogTrackPage(modifier, navController,authViewModel, dogViewModel, dogId)
            }
        }

        composable(route = "dogTrackEdit/{dogId}"){
            val dogId = it.arguments?.getString("dogId")
            if (dogId != null) {
                EditDogPage(modifier, navController,authViewModel, dogViewModel, dogId)
            }
        }

    } )

}