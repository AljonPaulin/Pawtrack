package com.example.pawtrack.compose

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.example.pawtrack.R
import com.example.pawtrack.pages.getLatestImagePath
import com.example.pawtrack.ui.theme.Coffee
import com.example.pawtrack.ui.theme.MainColor
import com.example.pawtrack.ui.theme.AlertColor


@Composable
fun DogInfoBox(

    dogName : String? = null,
    dogId : String? = null,
    dogBreed : String? = null,
    dogPic : String? = null,
    navController: NavController) {

    var latestImage by remember { mutableStateOf<Bitmap?>(null) }
    var latestImagePath by remember { mutableStateOf<String?>(null) }



    // Load latest image on startup
    LaunchedEffect(Unit) {
        latestImagePath = dogPic
        latestImagePath?.let { path ->
            latestImage = BitmapFactory.decodeFile(path)
        }
    }
    Card(
        modifier = Modifier
            .width(150.dp)
            .height(215.dp),
        colors = CardDefaults.cardColors(
            contentColor = Color(red = 226, green = 255, blue = 172),
            containerColor = Coffee,
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
            )

            Spacer(modifier = Modifier.height(2.dp))

            // Image
            if (latestImage == null){

                Image(
                    painter = painterResource(R.drawable.dog),
                    contentDescription = "Dog Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(120.dp, 90.dp)
                        .background(Color.LightGray)
                )

            }else{
                latestImage?.let { bitmap ->
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Dog Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(120.dp, 90.dp)
                            .background(Color.LightGray)
                    )
                }
            }



            Spacer(modifier = Modifier.height(8.dp))

            // Dog Name
            if (dogName != null) {
                Text(
                    text = dogName.uppercase(),
                    fontSize = 18.sp,
                    color = MainColor,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center

            ){

                Text(
                    text = "Device ID:",
                    fontSize = 15.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold,
                )
                if (dogId != null) {
                    Text(
                        text = dogId,
                        fontSize = 15.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Bold,
                    )
                }

            }
            Spacer(modifier = Modifier.height(8.dp))

            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                TextButton(
                    onClick = {},
                    modifier = Modifier.background(MainColor, shape = RoundedCornerShape(10.dp)).size(60.dp, 36.dp)
                ) {
                    Text("Active",
                        fontSize = 12.sp,
                        color = Coffee)
                }
                TextButton(
                    onClick = {
                        navController.navigate("dogTrack/${dogId}")
                    },
                    modifier = Modifier.background(AlertColor, shape = RoundedCornerShape(10.dp)).size(60.dp, 36.dp)
                ) {
                    Text("View",
                        fontSize = 12.sp,
                        color = Color.White)
                }
            }


        }
    }
}