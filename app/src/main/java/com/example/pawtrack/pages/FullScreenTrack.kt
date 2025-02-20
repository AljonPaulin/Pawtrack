package com.example.pawtrack.pages

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pawtrack.R
import com.example.pawtrack.ui.theme.Coffee
import com.example.pawtrack.ui.theme.MainColor
import com.example.pawtrack.ui.theme.TextSubColor
import com.example.pawtrack.ui.theme.SubColor
import com.example.pawtrack.ui.theme.CaramelColor
import com.example.pawtrack.ui.theme.AlertColor
import org.osmdroid.util.GeoPoint

@Composable
fun FullScreenTrack(modifier: Modifier = Modifier, navController: NavController, dogId: String, lodogion: GeoPoint) {
    val context = LocalContext.current

    Log.d("dogid", "ID $dogId")

    Scaffold (
        containerColor = Color(red = 226, green = 255, blue = 172),
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
            }
        }

    ){ innerPadding ->
        OpenStreetMapView(context, navController, innerPadding, dogId, fullScreen = true, currentLocation = lodogion)
    }
}