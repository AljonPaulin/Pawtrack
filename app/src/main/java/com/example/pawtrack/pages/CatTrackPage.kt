package com.example.pawtrack.pages

import android.widget.Toast
import androidx.activity.viewModels
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
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pawtrack.R
import com.example.pawtrack.viewmodel.AuthViewModel
import com.example.pawtrack.viewmodel.MapViewModel

@Composable
fun CatTrackPage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {
    //val mapViewModel = ViewModelProvider(this)[MapViewModel::class.java]

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
                    onClick = {},
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
        }
    ){ innerPadding ->
        CatCardScreen(innerPadding, navController)
    }
}

@Composable
fun CatCardScreen(innerPadding : PaddingValues, navController: NavController) {
    val cat = remember {
        CurrentCat(
            id = "12345",
            name = "Whiskers",
            breed = "Siamese",
            color = "Gray",
            location = "Living Room",
            status = "Active"
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(5.dp))
        Buttons(navController)

        CatInfoCard(cat = cat)

    }
}

@Composable
fun Buttons(navController: NavController) {
    val context = LocalContext.current
    Row (
        horizontalArrangement = Arrangement.Center


    ){
        Button(
            onClick = {
                navController.navigate(route = "home")

                Toast.makeText(context, "Back", Toast.LENGTH_SHORT).show()

            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(red = 122, green = 188, blue = 0),
                contentColor = Color.White
            ),
            modifier = Modifier
                .width(140.dp)
        ) {
            Text(text = "BACK")
        }
        Spacer( modifier = Modifier.width(10.dp))
        Button(
            onClick = {
                navController.navigate(route = "home")
                Toast.makeText(context, "Edit", Toast.LENGTH_SHORT).show()

            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(red = 122, green = 188, blue = 0),
                contentColor = Color.White
            ),
            modifier = Modifier
                .width(140.dp)
        ) {
            Text(text = "EDIT")
        }
    }


}

@Composable
fun CatInfoCard(cat: CurrentCat) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            contentColor = Color.White,
            containerColor = Color(red = 122, green = 188, blue = 0)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = MaterialTheme.shapes.medium.copy(CornerSize(16.dp))
    ) {
        Column {
            Row (
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth(),

                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center

            ){
                Column {
                    // Cat Name
                    Text(text = cat.name, style = MaterialTheme.typography.headlineMedium)

                    // Cat Breed
                    Text(text = "Breed: ${cat.breed}", style = MaterialTheme.typography.bodyLarge)

                    // Cat Color
                    Text(text = "Color: ${cat.color}", style = MaterialTheme.typography.bodyLarge)

                    // Cat ID
                    Text(text = "ID: ${cat.id}", style = MaterialTheme.typography.bodyMedium)

                    // Cat Location
                    Text(text = "Location: ${cat.location}", style = MaterialTheme.typography.bodyMedium)

                    // Cat Status
                    val statusColor = if (cat.status == "Active") Color.Green else Color.Red
                    Text(
                        text = "Status: ${cat.status}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = statusColor
                    )
                }
                Spacer(modifier = Modifier.width(30.dp))
                Card(
                    modifier = Modifier
                        .width(130.dp)
                        .height(130.dp),

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
            }
            Spacer(modifier = Modifier.height(16.dp))

            // API/Location or Tracker info
            Text(
                text = "Cat Tracker: Location and metrics will be fetched from the API",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )

        }

    }
}

data class CurrentCat(
    val id: String,
    val name: String,
    val breed: String,
    val color: String,
    val location: String,
    val status: String
)

@Composable
fun fetchApiMap(){

}