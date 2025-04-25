package com.example.pawtrack.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pawtrack.ui.theme.Coffee
import com.example.pawtrack.ui.theme.MainColor
import com.example.pawtrack.ui.theme.AlertColor
import java.sql.Time


@Composable
fun HistoryBox(
    Location : String? = null,
    Battery : Int? = null,
    Distance : Int? = null,
    Signal : String? = null,
    Time : String? = null,
    navController: NavController) {

    Spacer(modifier = Modifier.height(10.dp))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        colors = CardDefaults.cardColors(
            contentColor = Coffee,
            containerColor = Color.Transparent,
        ),
        shape = RoundedCornerShape(5.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
    ) {
        Row (
            modifier = Modifier.fillMaxSize()
                .padding(10.dp)
        ){
            Column (
                modifier = Modifier.fillMaxHeight()
            ){
                Text("Previous Location: $Location",)
                Text("Battery Status: $Battery%",)
                Text("Distance From Home: $Distance meters",)
                Text("Device Signal Status: $Signal",)

            }
            Column (
                modifier = Modifier.fillMaxSize()
                    .wrapContentWidth(align = Alignment.CenterHorizontally)
            ){
                Text("$Time",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                        .padding(bottom = 5.dp)
                    )
                Spacer(modifier = Modifier.height(10.dp))
                TextButton(
                    onClick = {
                        navController.navigate("home")
                    },
                    modifier = Modifier.background(AlertColor, shape = RoundedCornerShape(10.dp)).size(60.dp, 36.dp)
                ) {
                    Text("Track",
                        fontSize = 11.sp,
                        color = Color.White)
                }
            }
        }
    }
}