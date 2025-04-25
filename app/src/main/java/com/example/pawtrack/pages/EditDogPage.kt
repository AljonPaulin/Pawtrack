package com.example.pawtrack.pages

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pawtrack.R
import com.example.pawtrack.UserDog
import com.example.pawtrack.viewmodel.AuthState
import com.example.pawtrack.viewmodel.AuthViewModel
import com.example.pawtrack.viewmodel.DogViewModel
import com.google.firebase.auth.FirebaseAuth
import com.example.pawtrack.ui.theme.Coffee
import com.example.pawtrack.ui.theme.MainColor
import kotlinx.coroutines.launch

@Composable
fun EditDogPage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel, dogViewModel: DogViewModel, dogId: String) {
    val auth : FirebaseAuth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser?.uid.toString()
    val authState = authViewModel.authState.observeAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current


    // Check ff user not Authentidoged then it will go back to welcome page
    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> {
                navController.navigate("welcome")
            }
            else -> {
                dogViewModel.fetchOneDog(dogId)
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
                        onClick = {
                            scope.launch { drawerState.open() }
                        },
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
            }

        ){ innerPadding ->
            EditDogField(innerPadding, navController, dogViewModel, currentUser, dogId)

        }
    }


}

@Composable
fun EditDogField(innerPadding: PaddingValues, navController: NavController, dogViewModel: DogViewModel, currentUser : String, dogId: String) {
    val context = LocalContext.current
    val uniqueDog by dogViewModel.uniqueDog.collectAsState()
    var dogName by remember { mutableStateOf(uniqueDog?.dogName) }
    var dogColor by remember { mutableStateOf(uniqueDog?.dogColor) }
    var dogBreed by remember { mutableStateOf(uniqueDog?.dogBreed) }
    var dogPic by remember { mutableStateOf(uniqueDog?.dogPic) }

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var latestImage by remember { mutableStateOf<Bitmap?>(null) }
    var latestImagePath by remember { mutableStateOf<String?>(null) }



    // Load latest image on startup
    LaunchedEffect(Unit) {
        latestImagePath = dogPic
        latestImagePath?.let { path ->
            latestImage = BitmapFactory.decodeFile(path)
        }
    }

    // Launcher to pick image from gallery
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                dogPic = getFileNameFromUri(context, it).toString()
                imageUri = it
                val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                latestImage = bitmap

            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MainColor)
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
                containerColor = Coffee,
            ),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),


            ) {

            if (latestImage == null){

                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
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

        Spacer(modifier = Modifier.height(20.dp))

        // Dog Id TextField
        Row (
            verticalAlignment = Alignment.CenterVertically
        ){
            if (dogPic == null){
                Button(
                    onClick = {
                        imagePickerLauncher.launch("image/*")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Coffee,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .width(150.dp)
                        .height(44.dp)
                ) {
                    Text(text = "Select Image")
                }
            } else{
                Button(onClick = {
                    imagePickerLauncher.launch("image/*")
                },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Coffee,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .width(120.dp)
                        .height(44.dp)
                ) {
                    Text(text = "Selected")
                }
            }

            Spacer(modifier = Modifier.width(8.dp))
            // Dog Picture TextField
            dogPic?.let { it ->
                OutlinedTextField(
                    value = it,
                    onValueChange = { dogPic= it },
                    label = { Text("Selected Dog Picture") },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        focusedLabelColor = Coffee,
                        unfocusedLabelColor = Coffee,
                        focusedIndicatorColor = Coffee,
                        unfocusedIndicatorColor = Coffee,
                        focusedTextColor =  Coffee,
                        unfocusedTextColor =  Coffee,
                        cursorColor =  Coffee
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Dog Name TextField
        dogName?.let {
            OutlinedTextField(
                value = it,
                onValueChange = { dogName = it },
                label = { Text("Dog Name") },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    focusedLabelColor = Coffee,
                    unfocusedLabelColor = Coffee,
                    focusedIndicatorColor = Coffee,
                    unfocusedIndicatorColor = Coffee,
                    focusedTextColor = Coffee,
                    unfocusedTextColor = Coffee,
                    cursorColor = Coffee
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Dog Color TextField
        dogColor?.let {
            OutlinedTextField(
                value = it,
                onValueChange = { dogColor = it },
                label = { Text("Dog Color") },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    focusedLabelColor = Coffee,
                    unfocusedLabelColor = Coffee,
                    focusedIndicatorColor = Coffee,
                    unfocusedIndicatorColor = Coffee,
                    focusedTextColor = Coffee,
                    unfocusedTextColor = Coffee,
                    cursorColor = Coffee
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Dog Id TextField
        dogBreed?.let {
            OutlinedTextField(
                value = it,
                onValueChange = { dogBreed = it },
                label = { Text("Dog Breed") },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    focusedLabelColor = Coffee,
                    unfocusedLabelColor = Coffee,
                    focusedIndicatorColor = Coffee,
                    unfocusedIndicatorColor = Coffee,
                    focusedTextColor = Coffee,
                    unfocusedTextColor = Coffee,
                    cursorColor = Coffee
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center

        ){
            Button(
                onClick = {
                    navController.navigate("dogTrack/${dogId}")
                    Toast.makeText(context, "Unsuccessful to Edit Dog", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Coffee,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .width(140.dp)
            ) {
                Text(text = "Cancel")
            }

            Spacer(modifier = Modifier.width(42.dp))

            Button(
                onClick = {
                    if (dogPic == null){
                        val dog = UserDog(
                            dogName  = dogName,
                            dogColor = dogColor,
                            dogPic = null,
                            dogId= dogId,
                            dogBreed = dogBreed
                        )
                        dogViewModel.editDog(dog, currentUser, dogId)
                        navController.navigate("dogTrack/${dogId}")
                        Toast.makeText(context, "Edit Dog Successfully", Toast.LENGTH_SHORT).show()

                    }else{
                        latestImage?.let { bitmap ->

                            var savedPath : String

                            if(dogPic?.contains("files") == true){
                                savedPath = uniqueDog?.dogPic.toString()
                            }else{
                                val filename = dogPic?.substringBeforeLast(".")
                                savedPath =
                                    filename?.let {
                                        saveImageToInternalStorage(context, bitmap,
                                            it
                                        )
                                    }.toString()
                            }
                            val dog = UserDog(
                                dogName  = dogName,
                                dogColor = dogColor,
                                dogPic = savedPath,
                                dogId= dogId,
                                dogBreed = dogBreed
                            )
                            dogViewModel.editDog(dog, currentUser, dogId)
                            navController.navigate("dogTrack/${dogId}")
                            Toast.makeText(context, "Edit Dog Successfully", Toast.LENGTH_SHORT).show()
                    }

                    }

                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Coffee,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .width(140.dp)
            ) {
                Text(text = "Save")
            }
        }
    }
}