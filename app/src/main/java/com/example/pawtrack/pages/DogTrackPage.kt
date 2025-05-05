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
import com.example.pawtrack.viewmodel.DogViewModel
import kotlinx.coroutines.delay
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import kotlinx.coroutines.launch

@Composable
fun DogTrackPage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel,dogViewModel: DogViewModel, dogId: String) {
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
                dogViewModel.fetchOneDog(dogId)
                delay(1000L)
                isContentReady = true
            }
        }
    }

    //For navigating Menu buttons
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerMenu(authViewModel, context , navController)
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
                DogCardScreen(innerPadding, context, navController, dogViewModel, dogId, showDialog, { showDialog = it })
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
fun DogCardScreen(innerPadding : PaddingValues, context: Context, navController: NavController, dogViewModel: DogViewModel, dogId: String, showDialog: Boolean,
                  setShowDialog: (Boolean) -> Unit) {
    val uniqueDog by dogViewModel.uniqueDog.collectAsState()
    val dog = remember {
            CurrentDog(
                id = uniqueDog?.dogId,
                name = uniqueDog?.dogName,
                breed = uniqueDog?.dogBreed,
                color = uniqueDog?.dogColor,
                pic = uniqueDog?.dogPic,
                location = "Living Room",
                status = "Active"
            )
    }


    if (showDialog) {
        AlertDialog(
            onDismissRequest = { setShowDialog(false) },
            title = { Text("Delete Dog") },
            text = { Text("Are you sure you want to delete this dog? This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        dogViewModel.deleteDog(dogId)
                        setShowDialog(false)
                        navController.navigate(route = "home")
                        Toast.makeText(context, "Dog Deleted SuccessFully", Toast.LENGTH_SHORT).show()
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
        Buttons(navController, dogId)

        DogInfoCard(dog = dog, context, navController)

    }
}

@Composable
fun Buttons(navController: NavController, dogId: String) {
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
                navController.navigate("dogTrackEdit/${dogId}")
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
fun DogInfoCard(dog: CurrentDog, context: Context, navController: NavController) {
    val currentLocation = produceState<GeoPoint?>(initialValue = null) {
        value = currentLocationWithMap(context)
    }
    var latestImage by remember { mutableStateOf<Bitmap?>(null) }
    var latestImagePath by remember { mutableStateOf<String?>(null) }


    // Load latest image on startup
    LaunchedEffect(Unit) {
        latestImagePath = dog.pic
        latestImagePath?.let { path ->
            latestImage = BitmapFactory.decodeFile(path)
        }
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
                    // Dog Name
                    dog.name?.let { Text(text = it, style = MaterialTheme.typography.headlineMedium) }

                    // Dog Breed
                    Text(text = "Breed: ${dog.breed}", style = MaterialTheme.typography.bodyLarge)

                    // Dog Color
                    Text(text = "Color: ${dog.color}", style = MaterialTheme.typography.bodyLarge)

                    // Dog ID
                    Text(text = "ID: ${dog.id}", style = MaterialTheme.typography.bodyMedium)

                    // Dog Location
                    Text(text = "Location: ${dog.location}", style = MaterialTheme.typography.bodyMedium)

                    // Dog Status
                    val statusColor = if (dog.status == "Active") Color.Green else Color.Red
                    Text(
                        text = "Status: ${dog.status}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = statusColor
                    )
                }
                Spacer(modifier = Modifier.width(30.dp))
                Column {
                    Card(
                        modifier = Modifier
                            .width(130.dp)
                            .height(100.dp),

                        colors = CardDefaults.cardColors(
                            contentColor = Color(red = 226, green = 255, blue = 172),
                            containerColor = Color(red = 122, green = 188, blue = 0),
                        ),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),


                        ) {
                        // Image
                        if (latestImage == null){

                            Image(
                                painter = painterResource(R.drawable.dog),
                                contentDescription = "Dog Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(250.dp)
                                    .background(Color.LightGray)
                            )

                        }else{
                            latestImage?.let { bitmap ->
                                Image(
                                    bitmap = bitmap.asImageBitmap(),
                                    contentDescription = "Dog Image",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(250.dp)
                                        .background(Color.LightGray)
                                )
                            }
                        }
                    }
                    //HISTORY BUTTON

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
                    OpenStreetMapView(context = context, navController, innerPadding = null, dogId = dog.id, false, currentLocation = location )
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
fun OpenStreetMapView(context: Context, navController: NavController, innerPadding: PaddingValues?, dogId: String?,  fullScreen: Boolean, currentLocation: GeoPoint? = null ) {
    var isFullScreen = fullScreen


    // Handle back button when in full screen
    BackHandler(enabled = isFullScreen) {
        isFullScreen = false
        navController.navigate("dogTrack/${dogId}")
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
                    navController.navigate("dogTrack/${dogId}")
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Coffee,
                    contentColor = Color.White
                ),
            ) {
                Text("Back")
            }
        }

        if (!isFullScreen) {
            Button(
                onClick = {
                    navController.navigate(route = "fullScreenTrack/${dogId}/${currentLocation}")
                    isFullScreen = true
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Coffee,
                    contentColor = Color.White
                ),
            ) {
                Text("FullScreen")
            }
        }
    }
}

data class CurrentDog(
    val id: String? = null,
    val name: String? = null,
    val breed: String? = null,
    val color: String? = null,
    val pic: String? = null,
    val location: String,
    val status: String
)

