package com.example.pawtrack.pages

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pawtrack.R
import com.example.pawtrack.compose.DogInfoBox
import com.example.pawtrack.viewmodel.AuthState
import com.example.pawtrack.viewmodel.AuthViewModel
import com.example.pawtrack.viewmodel.DogViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.pawtrack.ui.theme.Coffee
import com.example.pawtrack.ui.theme.MainColor
import com.example.pawtrack.ui.theme.TextSubColor
import com.example.pawtrack.ui.theme.AlertColor

@Composable
fun HomePage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel, dogViewModel: DogViewModel){
    val context = LocalContext.current
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val authState = authViewModel.authState.observeAsState()
    var isContentReady by remember { mutableStateOf(false) }

    // Check ff user not Authentidoged then it will go back to welcome page
    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> {
                navController.navigate("welcome")
            }
            else -> {
                dogViewModel.run()
                delay(2000L)
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

        Scaffold(
            containerColor = MainColor,
            topBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp, top = 15.dp, end = 12.dp)

                ) {
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
                Row (
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(horizontal = 30.dp)
                        .fillMaxWidth()
                ){
                    TextButton( onClick = {
                        navController.navigate(route = "home")
                    }) {
                        Column (
                            horizontalAlignment = Alignment.CenterHorizontally,

                            ) {
                            Icon(
                                imageVector = Icons.Filled.Home,
                                contentDescription = "Home Icon",
                                tint = Coffee,
                                modifier = Modifier.size(35.dp)
                            )
                            Text(
                                text = "Home",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Coffee)

                        }
                    }
                    TextButton( onClick = {
                        navController.navigate(route = "addDog")
                    }) {
                        Column (
                            horizontalAlignment = Alignment.CenterHorizontally,

                            ) {
                            Icon(
                                imageVector = Icons.Filled.Add,
                                contentDescription = "Add Icon",
                                tint = AlertColor,
                                modifier = Modifier.size(35.dp)
                            )
                            Text(
                                text = "Add Dog",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = AlertColor)

                        }
                    }
                    TextButton( onClick = {
                        navController.navigate("store")

                    }) {
                        Column (
                            horizontalAlignment = Alignment.CenterHorizontally,

                            ) {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = "Account Icon",
                                tint = AlertColor,
                                modifier = Modifier.size(35.dp)
                            )
                            Text(
                                text = "Account",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = AlertColor)

                        }
                    }

                }
            }

        ) { innerPadding ->

            if (isContentReady) {
                    ScafoldContent(innerPadding, dogViewModel, navController)
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
fun SearchBar() {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp) // Outer padding
    ) {
        BasicTextField(
            value = searchQuery,
            onValueChange = { newValue -> searchQuery = newValue },
            singleLine = true,
            decorationBox = { innerTextField ->
                Row(
                    Modifier
                        .background(Color.White, shape = RoundedCornerShape(15.dp))
                        .padding(horizontal = 8.dp)
                        .height(40.dp)  // Set your desired height
                        .fillMaxWidth(),

                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(Modifier.weight(1f)) {
                        if (searchQuery.text.isEmpty()) Text("Search...", color = Color.Gray)
                        innerTextField()
                    }
                }
            }
        )


    }
}

@Composable
fun ScafoldContent(innerPadding: PaddingValues, dogViewModel: DogViewModel, navController: NavController){
    val users by dogViewModel.users.collectAsState(initial = emptyList())
    val isLoading by dogViewModel.isLoading.collectAsState()

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(60.dp, 60.dp),
                color = Color(red = 122, green = 188, blue = 0),
            )
        }
    } else {
        val dogChunked by remember(users) {
            derivedStateOf {
                users.map { user ->
                    Dog(
                        dogName = user.dogName,
                        dogColor = user.dogColor,
                        dogId = user.dogId,
                        dogBreed = user.dogBreed,
                        dogPic = user.dogPic
                    )
                }.chunked(2)
            }
        }
        Column (
            modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {
            SearchBar()
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MainColor)

            ) {
                if (dogChunked.isEmpty()){
                    items(1) { _ ->
                        EmptyList()
                    }
                } else {
                    items(dogChunked.size) { dogPair ->

                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            if (dogChunked[dogPair].size == 1) {
                                Spacer(modifier = Modifier.weight(1f))

                                val currentDog = dogChunked[dogPair][0]

                                for (dog in users) {
                                    if (dog.dogName == currentDog.dogName) {
                                        DogInfoBox(
                                            dogName = dog.dogName,
                                            dogId = dog.dogId,
                                            dogBreed = dog.dogBreed,
                                            dogPic = dog.dogPic,
                                            navController = navController
                                        )
                                    }
                                }
                            } else {
                                Log.d("DogData", " Double $dogChunked")
                                for (dog in dogChunked[dogPair]) {
                                    DogInfoBox(
                                        dogName = dog.dogName,
                                        dogId = dog.dogId,
                                        dogBreed = dog.dogBreed,
                                        dogPic = dog.dogPic,
                                        navController = navController
                                    )
                                }
                            }

                        }

                    }
                }


            }

        }
    }
}

@Composable
fun DrawerMenu(authViewModel: AuthViewModel, context: Context) {

    CompositionLocalProvider(LocalContentColor provides Coffee) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(320.dp)
                .background(MainColor)
                .padding(16.dp),

            ) {
            Text(
                text = "Menu",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Account",
                fontSize = 20.sp,
                modifier = Modifier.clickable { /* Handle Click */ }
            )
            Spacer(modifier = Modifier.height(8.dp)) // Adds space between items
            Text(
                text = "Logout",
                fontSize = 20.sp,
                modifier = Modifier.clickable {
                    authViewModel.signout()
                    Toast.makeText(context, "Log out Successfully", Toast.LENGTH_SHORT).show()}
            )
            Spacer(modifier = Modifier.height(8.dp)) // Adds space between items
            Text(
                text = "History",
                fontSize = 20.sp,
                modifier = Modifier.clickable { /* Handle Click */ }
            )
            Spacer(modifier = Modifier.height(8.dp)) // Adds space between items
            Text(
                text = "Help",
                fontSize = 20.sp,
                modifier = Modifier.clickable { /* Handle Click */ }
            )

        }
    }
}


@Composable
fun EmptyList(){
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,

    ){
        Spacer(modifier = Modifier.height(200.dp))
        Text(
            text = "Empty List of Dogs",
            fontSize = 25.sp,
            color = Color(red = 122, green = 188, blue = 0),
        )
    }
}


data class Dog(
    val dogName : String? = null,
    val dogColor : String? = null,
    val dogId : String? = null,
    val dogBreed : String? = null,
    val dogPic: String? = null,
)

