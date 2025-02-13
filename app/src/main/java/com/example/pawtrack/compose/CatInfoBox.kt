package com.example.pawtrack.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController

@Composable
fun CatInfoBox(

    catName : String? = null,
    catId : String? = null,
    catBreed : String? = null,
    imageRes: Int,
    navController: NavController) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .height(200.dp),
        colors = CardDefaults.cardColors(
            contentColor = Color(red = 226, green = 255, blue = 172),
            containerColor = Color(red = 122, green = 188, blue = 0),
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        onClick = {
            navController.navigate(route = "CatTrack")

        }


    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Meter (Top bar)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .background(if (20 > 50) Color.Green else Color.Red)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Image
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "Cat Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.LightGray)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Cat Name
            if (catName != null) {
                Text(
                    text = catName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            // CatID
            if (catId != null) {
                Text(
                    text = catId,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
            // CatID
            if (catBreed != null) {
                Text(
                    text = catBreed,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}