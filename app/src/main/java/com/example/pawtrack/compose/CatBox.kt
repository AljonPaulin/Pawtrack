package com.example.pawtrack.compose

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.pawtrack.CatTrackActvity
import com.example.pawtrack.DashboardActivity

@Composable
fun CatInfoBox(catName: String, status: String, imageRes: Int, meterValue: Int) {
    val context = LocalContext.current
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
            val intent = Intent(context, CatTrackActvity::class.java)
            context.startActivity(intent)

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
                    .background(if (meterValue > 50) Color.Green else Color.Red)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(fraction = meterValue / 100f)
                        .height(8.dp)
                        .background(Color.Blue)
                )
            }

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
            Text(
                text = catName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            // Status
            Text(
                text = status,
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}