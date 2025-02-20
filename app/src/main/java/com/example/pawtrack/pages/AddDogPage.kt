package com.example.pawtrack.pages

import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pawtrack.R
import com.example.pawtrack.UserDog
import com.example.pawtrack.viewmodel.AuthViewModel
import com.example.pawtrack.viewmodel.DogViewModel
import com.google.firebase.auth.FirebaseAuth
import com.example.pawtrack.ui.theme.Coffee
import com.example.pawtrack.ui.theme.MainColor
import kotlinx.coroutines.launch


@Composable
fun AddDogPage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel, dogViewModel: DogViewModel) {
    val auth : FirebaseAuth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser?.uid.toString()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

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
            },

        ){ innerPadding ->
            AddDogField(innerPadding, navController, dogViewModel, currentUser)

        }

    }

}

fun getFileNameFromUri(context: android.content.Context, uri: Uri): String? {
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex != -1) {
                return it.getString(nameIndex)
            }
        }
    }
    return null
}


@Composable
fun AddDogField( innerPadding: PaddingValues, navController: NavController, dogViewModel: DogViewModel, currentUser : String) {
    val context = LocalContext.current
    var dogName by remember { mutableStateOf("") }
    var dogColor by remember { mutableStateOf("") }
    var dogPic by remember { mutableStateOf("") }
    var dogId by remember { mutableStateOf("") }
    var dogBreed by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var latestImage by remember { mutableStateOf<Bitmap?>(null) }
    val isFormValid = remember { mutableStateOf(false) }

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
    // Update isFormValid whenever any field changes
    LaunchedEffect(dogName, dogColor, dogId, dogBreed) {
        isFormValid.value = dogName.isNotBlank() &&
                dogColor.isNotBlank() &&
                dogId.isNotBlank() &&
                dogBreed.isNotBlank()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MainColor)
            .padding(innerPadding)
            .padding(10.dp)
            .verticalScroll(scrollState) ,

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier
                .width(250.dp)
                .height(250.dp),

            colors = CardDefaults.cardColors(
                contentColor = Coffee,
                containerColor = Coffee,
            ),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),


            ) {
            latestImage?.let { bitmap ->
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Dog Image",
                    modifier = Modifier
                        .size(250.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        // Dog Name TextField
        OutlinedTextField(
            value = dogName,
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
                focusedTextColor =  Coffee,
                unfocusedTextColor =  Coffee,
                cursorColor =  Coffee
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Dog Color TextField
        OutlinedTextField(
            value = dogColor,
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
                focusedTextColor =  Coffee,
                unfocusedTextColor =  Coffee,
                cursorColor =  Coffee
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Dog Id TextField
        OutlinedTextField(
            value = dogId,
            onValueChange = { dogId = it },
            label = { Text("Dog ID") },
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

        Spacer(modifier = Modifier.height(16.dp))
        Row (
            verticalAlignment = Alignment.CenterVertically
        ){
            if (dogPic.isEmpty()){
                Button(
                    onClick = {
                        imagePickerLauncher.launch("image/*")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Coffee,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .width(100.dp)
                        .height(44.dp)
                ) {
                    Text(text = "Select")
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
            OutlinedTextField(
                value = dogPic,
                onValueChange = { dogPic= it },
                label = { Text("Select Dog Picture") },
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
        Spacer(modifier = Modifier.height(16.dp))

        // Dog Id TextField
        OutlinedTextField(
            value = dogBreed,
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
                focusedTextColor =  Coffee,
                unfocusedTextColor =  Coffee,
                cursorColor =  Coffee
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center

        ){
            Button(
                onClick = {
                    navController.navigate(route = "home")
                    Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show()
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
                    latestImage?.let { bitmap ->
                        val filename = dogPic.substringBeforeLast(".")
                        val savedPath = saveImageToInternalStorage(context, bitmap, filename)
                        if (savedPath != null) {
                            val dog = UserDog(
                                dogName  = dogName,
                                dogColor = dogColor,
                                dogPic = savedPath,
                                dogId= dogId,
                                dogBreed = dogBreed
                            )
                            dogViewModel.addDog(dog, currentUser)
                            navController.navigate(route = "home")
                            Toast.makeText(context, "Add Dog Successfully", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                enabled = isFormValid.value,// Disable button if form is not valid
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




