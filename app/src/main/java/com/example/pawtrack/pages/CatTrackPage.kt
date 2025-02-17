package com.example.pawtrack.pages

import android.Manifest
import android.app.Activity
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
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.pawtrack.R
import com.example.pawtrack.viewmodel.AuthState
import com.example.pawtrack.viewmodel.AuthViewModel
import com.example.pawtrack.viewmodel.CatViewModel
import kotlinx.coroutines.delay
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import com.example.pawtrack.ui.theme.Coffee
import com.example.pawtrack.ui.theme.MainColor
import com.example.pawtrack.ui.theme.TextSubColor
import com.example.pawtrack.ui.theme.SubColor
import com.example.pawtrack.ui.theme.CaramelColor
import com.example.pawtrack.ui.theme.AlertColor
import kotlinx.coroutines.launch

@Composable
fun CatTrackPage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel,catViewModel: CatViewModel, catId: String) {
    var isContentReady by remember { mutableStateOf(false) }
    val authState = authViewModel.authState.observeAsState()
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()


    // Check ff user not Authenticated then it will go back to welcome page
    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> {
                navController.navigate("welcome")
            }
            else -> {
                catViewModel.fetchOneCat(catId)
                delay(1000L)
                isContentReady = true
            }
        }
    }

    //For navigating Menu buttons
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerMenu(authViewModel, context)
        }
    ) {
        Scaffold (
            containerColor = MainColor,
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
                        color = Coffee,
                        fontSize = 40.sp,
                        fontFamily = FontFamily.Cursive,
                        modifier = Modifier
                            .padding(start = 60.dp)

                    )
                    IconButton(
                        onClick = { scope.launch { drawerState.open() } },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Burger Menu Icon",
                            tint = Coffee,
                            modifier = Modifier
                                .size(80.dp)

                        )
                    }
                }
            },
            bottomBar = {
                Button(
                    onClick = {
                        showDialog = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TextSubColor,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)


                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete Icon",
                        tint = Color.White,
                        modifier = Modifier
                            .size(35.dp)

                    )
                }
            }

        ){ innerPadding ->
            if (isContentReady) {
                CatCardScreen(innerPadding, context, navController, catViewModel, catId, showDialog, { showDialog = it })
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(60.dp, 60.dp),
                        color = Coffee,
                    )
                }
            }

        }
    }


}

@Composable
fun CatCardScreen(innerPadding : PaddingValues, context: Context, navController: NavController, catViewModel: CatViewModel, catId: String, showDialog: Boolean,
                  setShowDialog: (Boolean) -> Unit) {
    val uniqueCat by catViewModel.uniqueCat.collectAsState()
    val cat = remember {
            CurrentCat(
                id = uniqueCat?.catId,
                name = uniqueCat?.catName,
                breed = uniqueCat?.catBreed,
                color = uniqueCat?.catColor,
                location = "Living Room",
                status = "Active"
            )
    }


    if (showDialog) {
        AlertDialog(
            onDismissRequest = { setShowDialog(false) },
            title = { Text("Delete Cat") },
            text = { Text("Are you sure you want to delete this cat? This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        catViewModel.deleteCat(catId)
                        setShowDialog(false)
                        navController.navigate(route = "home")
                        Toast.makeText(context, "Cat Deleted SuccessFully", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                    ),
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(
                    onClick = { setShowDialog(false)},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MainColor,
                    ),
                ) {
                    Text("Cancel")
                }
            }
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(5.dp))
        Buttons(navController, catId)

        CatInfoCard(cat = cat, context, navController)

    }
}

@Composable
fun Buttons(navController: NavController, catId: String) {
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
                containerColor = Coffee,
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
                navController.navigate("catTrackEdit/${catId}")
                Toast.makeText(context, "Edit", Toast.LENGTH_SHORT).show()

            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Coffee,
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
fun CatInfoCard(cat: CurrentCat, context: Context, navController: NavController) {
    val currentLocation = produceState<GeoPoint?>(initialValue = null) {
        value = currentLocationWithMap(context)
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            contentColor = Coffee,
            containerColor = MainColor
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
                    cat.name?.let { Text(text = it, style = MaterialTheme.typography.headlineMedium) }

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

            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = MaterialTheme.shapes.medium.copy(CornerSize(16.dp))
            ) {
                currentLocation.value?.let { location ->
                    OpenStreetMapView(context = context, navController, innerPadding = null, catId = cat.id, false, currentLocation = location )
                }

            }
        }

    }
}

suspend fun currentLocationWithMap(context: Context): GeoPoint {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
    }

    return suspendCoroutine { continuation ->
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                continuation.resume(GeoPoint(location.latitude, location.longitude))
            } else {
                continuation.resumeWithException(IllegalStateException("Location not available"))
            }
        }
    }
}

@Composable
fun OpenStreetMapView(context: Context, navController: NavController, innerPadding: PaddingValues?, catId: String?,  fullScreen: Boolean, currentLocation: GeoPoint? = null ) {
    var isFullScreen = fullScreen


    // Handle back button when in full screen
    BackHandler(enabled = isFullScreen) {
        isFullScreen = false
        navController.navigate("catTrack/${catId}")
    }
    //Render map
    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = {
            Configuration.getInstance().load(context, context.getSharedPreferences("osm_prefs", Context.MODE_PRIVATE))

            MapView(context).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)
                isClickable = true
                controller.setZoom(20.0)
                controller.setCenter(GeoPoint(14.5995, 120.9842))

                // Add a marker for the current location if available
                Log.d("Current", "display $currentLocation")
                currentLocation?.let {
                    val currentLocationMarker = Marker(this).apply {
                        position = it
                        title = "Your Current Location"
                        snippet = "This is your current location."
                    }
                    overlays.add(currentLocationMarker)
                }


                val marker = Marker(this).apply {
                    position = GeoPoint(14.5995, 120.9842)
                    title = "Manila"
                    snippet = "Capital of the Philippines"
                }
                overlays.add(marker)

                val marker2 = Marker(this).apply {
                    position = GeoPoint(14.6760, 121.0437)  // Quezon City
                    title = "Quezon City"
                    snippet = "The City of Stars"
                }
                overlays.add(marker2)
            }
        }, modifier = Modifier.fillMaxSize())

        if (isFullScreen) {
            Button(
                onClick = {
                    isFullScreen = false
                    navController.navigate("catTrack/${catId}")
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Text("Back")
            }
        }

        if (!isFullScreen) {
            Button(
                onClick = {
                    navController.navigate(route = "fullScreenTrack/${catId}")
                    isFullScreen = true
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Text("FullScreen")
            }
        }
    }
}

data class CurrentCat(
    val id: String? = null,
    val name: String? = null,
    val breed: String? = null,
    val color: String? = null,
    val location: String,
    val status: String
)

