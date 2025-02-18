package com.example.pawtrack

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pawtrack.pages.AddCatPage
import com.example.pawtrack.pages.CatTrackPage
import com.example.pawtrack.pages.EditCatPage
import com.example.pawtrack.pages.FullScreenTrack
import com.example.pawtrack.pages.HomePage
import com.example.pawtrack.pages.LoginPage
import com.example.pawtrack.pages.SigninPage
import com.example.pawtrack.pages.WelcomePage
import com.example.pawtrack.viewmodel.AuthViewModel
import com.example.pawtrack.viewmodel.CatViewModel
import org.osmdroid.util.GeoPoint

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
        composable(route = "fullScreenTrack/{catId}/{currentLocation}"){
            val catId = it.arguments?.getString("catId")
            val locationString = it.arguments?.getString("currentLocation")
            var locationGeopoint: GeoPoint? = null

            val location = locationString?.let { loc ->
                val parts = loc.split(",")
                locationGeopoint = GeoPoint(parts[0].toDouble(), parts[1].toDouble())
            }

            if (catId != null) {
                locationGeopoint?.let { loc ->
                    FullScreenTrack(modifier, navController, catId,
                        loc
                    )
                }
            }
        }
        composable(route = "catTrack/{catId}"){
            val catId = it.arguments?.getString("catId")
            if (catId != null) {
                CatTrackPage(modifier, navController,authViewModel, catViewModel, catId)
            }
        }

        composable(route = "catTrackEdit/{catId}"){
            val catId = it.arguments?.getString("catId")
            if (catId != null) {
                EditCatPage(modifier, navController,authViewModel, catViewModel, catId)
            }
        }

    } )

}