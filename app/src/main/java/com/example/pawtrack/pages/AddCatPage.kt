package com.example.pawtrack.pages

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pawtrack.R
import com.example.pawtrack.viewmodel.AuthViewModel

@Composable
fun AddCatPage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {
    val context = LocalContext.current

    Scaffold (
        containerColor = Color(red = 226, green = 255, blue = 172),
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, top = 15.dp, end = 12.dp)

            ){
                Image(
                    painter = painterResource(id = R.drawable.paww),
                    contentDescription = "logo",
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.TopStart)

                )
                Text(
                    text = "PawTrack",
                    color = Color(red = 122, green = 188, blue = 0),
                    fontSize = 40.sp,
                    fontFamily = FontFamily.Cursive,
                    modifier = Modifier
                        .padding(start = 60.dp)

                )
                IconButton(
                    onClick = {

                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Menu,
                        contentDescription = "Burger Menu Icon",
                        tint = Color(red = 122, green = 188, blue = 0),
                        modifier = Modifier
                            .size(80.dp)


                    )
                }
            }
        }, bottomBar = {
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center

            ){
                Button(
                    onClick = {
                        navController.navigate(route = "home")
                        Toast.makeText(context, "Unsuccessful to Add Cat", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(red = 122, green = 188, blue = 0),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .width(140.dp)
                ) {
                    Text(text = "Cancel")
                }

                Spacer(modifier = Modifier.width(32.dp))

                Button(
                    onClick = {
                        navController.navigate(route = "home")
                        Toast.makeText(context, "Add Cat Successfully", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(red = 122, green = 188, blue = 0),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .width(140.dp)
                ) {
                    Text(text = "Save")
                }
            }
        }
    ){ innerPadding ->
        AddCatField(innerPadding)
    }
}

@Composable
fun AddCatField(innerPadding: PaddingValues) {
    var catName by remember { mutableStateOf("") }
    var catColor by remember { mutableStateOf("") }
    var catId by remember { mutableStateOf("") }
    var catBreed by remember { mutableStateOf("") }
    val subColor = Color(red = 122, green = 188, blue = 0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(red = 226, green = 255, blue = 172))
            .padding(innerPadding)
            .padding(10.dp),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .width(250.dp)
                .height(250.dp),

            colors = CardDefaults.cardColors(
                contentColor = Color(red = 226, green = 255, blue = 172),
                containerColor = Color(red = 122, green = 188, blue = 0),
            ),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),


            ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "Cat Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(250.dp)
                    .background(Color.LightGray)

            )
        }

        Spacer(modifier = Modifier.height(20.dp))
        // Cat Name TextField
        OutlinedTextField(
            value = catName,
            onValueChange = { catName = it },
            label = { Text("Cat Name") },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                focusedLabelColor = subColor,
                unfocusedLabelColor = subColor,
                focusedIndicatorColor = subColor,
                unfocusedIndicatorColor = subColor,
                focusedTextColor =  subColor,
                unfocusedTextColor =  subColor,
                cursorColor =  subColor
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Cat Color TextField
        OutlinedTextField(
            value = catColor,
            onValueChange = { catColor = it },
            label = { Text("Cat Color") },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                focusedLabelColor = subColor,
                unfocusedLabelColor = subColor,
                focusedIndicatorColor = subColor,
                unfocusedIndicatorColor = subColor,
                focusedTextColor =  subColor,
                unfocusedTextColor =  subColor,
                cursorColor = subColor
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Cat Id TextField
        OutlinedTextField(
            value = catId,
            onValueChange = { catId = it },
            label = { Text("Cat ID") },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                focusedLabelColor = subColor,
                unfocusedLabelColor = subColor,
                focusedIndicatorColor = subColor,
                unfocusedIndicatorColor = subColor,
                focusedTextColor =  subColor,
                unfocusedTextColor =  subColor,
                cursorColor =  subColor
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Cat Id TextField
        OutlinedTextField(
            value = catBreed,
            onValueChange = { catBreed = it },
            label = { Text("Cat Breed") },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                focusedLabelColor = subColor,
                unfocusedLabelColor = subColor,
                focusedIndicatorColor = subColor,
                unfocusedIndicatorColor = subColor,
                focusedTextColor =  subColor,
                unfocusedTextColor =  subColor,
                cursorColor =  subColor
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

    }
}

