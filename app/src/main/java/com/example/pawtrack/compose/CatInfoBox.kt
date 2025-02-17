package com.example.pawtrack.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.R
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.pawtrack.ui.theme.Coffee
import com.example.pawtrack.ui.theme.MainColor
import com.example.pawtrack.ui.theme.TextSubColor
import com.example.pawtrack.ui.theme.SubColor
import com.example.pawtrack.ui.theme.CaramelColor
import com.example.pawtrack.ui.theme.AlertColor


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
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "Cat Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp, 90.dp)
                    .background(Color.LightGray)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Cat Name
            if (catName != null) {
                Text(
                    text = catName,
                    fontSize = 18.sp,
                    color = MainColor,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
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
                        navController.navigate("catTrack/${catId}")
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