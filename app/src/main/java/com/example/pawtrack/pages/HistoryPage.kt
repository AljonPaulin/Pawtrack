package com.example.pawtrack.pages

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pawtrack.R
import com.example.pawtrack.compose.HistoryBox
import com.example.pawtrack.viewmodel.AuthViewModel
import com.example.pawtrack.viewmodel.DogViewModel
import com.google.firebase.auth.FirebaseAuth
import com.example.pawtrack.ui.theme.Coffee
import com.example.pawtrack.ui.theme.MainColor



@Composable
fun HistoryPage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel, dogViewModel: DogViewModel) {
    val auth : FirebaseAuth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser?.uid.toString()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current


    Scaffold (
        containerColor = MainColor,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, top = 15.dp, end = 12.dp)

            ){
                Image(
                    painter = painterResource(id = R.drawable.dog),
                    contentDescription = "logo",
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.TopStart)

                )
                Text(
                    text = "PawTrack",
                    color = Coffee,
                    fontSize = 40.sp,
                    fontFamily = FontFamily.Cursive,
                    modifier = Modifier
                        .padding(start = 60.dp)

                )
                TextButton(
                    onClick = {navController.navigate(route = "home")},
                    modifier = Modifier
                        .background(Coffee, shape = RoundedCornerShape(10.dp))
                        .align(Alignment.TopEnd)
                        .width(120.dp)
                ) {
                    Text("Back",
                        fontSize = 18.sp,
                        color = Color.White,

                    )
                }
            }
        },

        ){ innerPadding ->
            HistoryMain(innerPadding, navController)
    }

}

@Composable
fun HistoryMain(padding: PaddingValues, navController: NavController){
    Box(
        modifier = Modifier.fillMaxSize()
            .padding(padding)
            .padding(horizontal = 12.dp)
            .padding(top = 10.dp)
    ){
        Column{
            Text("HISTORY",
                fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Coffee,
                modifier = Modifier.align(Alignment.CenterHorizontally)

            )
            HistoryBox("Jaro", 30, 100, "Low", "10pm", navController)
            HistoryBox("Jaro", 30, 100, "Low", "10pm", navController)
            HistoryBox("Jaro", 30, 100, "Low", "10pm", navController)
        }

    }


}



