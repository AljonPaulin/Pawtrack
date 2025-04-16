package com.example.pawtrack.pages

import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pawtrack.R
import com.example.pawtrack.viewmodel.AuthState
import com.example.pawtrack.viewmodel.AuthViewModel
import com.example.pawtrack.ui.theme.Coffee
import com.example.pawtrack.ui.theme.MainColor
import com.example.pawtrack.ui.theme.TextSubColor
import com.example.pawtrack.ui.theme.AlertColor


@Composable
fun WelcomePage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel){
    var clicked by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val image = ImageBitmap.imageResource(id = R.drawable.dogmain)


    // Animation to fade in the title
    val titleAlpha by animateFloatAsState(
        targetValue = if (clicked) 1f else 0f,
        animationSpec = tween(durationMillis = 1000)
    )
    val buttonAlpha by animateFloatAsState(
        targetValue = if (clicked) 0f else 1f,
        animationSpec = tween(durationMillis = 500)
    )

    // Animation to move buttons up when clicked
    val buttonsOffsetY by animateFloatAsState(
        targetValue = if (clicked) 0f else 100f,
        animationSpec = tween(durationMillis = 1000)
    )
    // Animation to move buttons up when clicked
    val startButtonsOffsetY by animateFloatAsState(
        targetValue = if (clicked) 70f else 50f,
        animationSpec = tween(durationMillis = 1000)
    )
    val authState = authViewModel.authState.observeAsState()

    LaunchedEffect(authState.value) {
        when(authState.value){
            is AuthState.Authenticated -> navController.navigate("home")
            is AuthState.Error -> Toast.makeText(context,
                (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT).show()
            else -> Unit

        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                val customWidth = 2200
                val customHeight = (size.height * 1.1f).toInt()

                drawImage(
                    image = image,
                    dstSize = IntSize(customWidth, customHeight),
                    dstOffset = IntOffset(
                        ((size.width - customWidth) / 2).toInt(),  // center horizontally
                        ((size.height - customHeight) / 2).toInt() // center vertically
                    )
                    )
            }
            .background(color = Color.Black.copy(alpha = 0.6f))

    ){
        Column(
            modifier= Modifier
                .align(Alignment.TopCenter)
                .padding(top = 290.dp)
                .offset(y = buttonsOffsetY.dp)
        ) {
            Text(
                text = "PawTrack",
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = 70.sp,
                fontFamily = FontFamily.Cursive,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Text(
                text = "Welcome to the world of dog tracking!",
                color = Color.White,
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .alpha(buttonAlpha),
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,

                ){
                Button(
                    onClick = {
                        clicked = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Coffee,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .width(320.dp)
                        .padding(bottom = 20.dp)
                        .alpha(buttonAlpha),
                    enabled = !clicked
                ) {
                    Text("Get Started",
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .offset(y = buttonsOffsetY.dp)
                .fillMaxSize()
                .padding(top = 160.dp)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {

            Button(
                onClick = {
                    navController.navigate(route = "login")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Coffee,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .width(240.dp)
                    .padding(bottom = 20.dp)
                    .alpha(titleAlpha),
                enabled = clicked

                ) {
                Text("Login",
                    fontWeight = FontWeight.Bold,

                    )
            }

            Button(
                onClick = {
                    navController.navigate(route = "signin")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Coffee,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .width(240.dp)
                    .alpha(titleAlpha),
                enabled = clicked

            ) {
                Text(
                    text = "Sign In",
                    fontWeight = FontWeight.Bold,
                )
            }

        }

    }
}