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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pawtrack.R
import com.example.pawtrack.compose.CatInfoBox
import com.example.pawtrack.viewmodel.AuthState
import com.example.pawtrack.viewmodel.AuthViewModel
import com.example.pawtrack.viewmodel.CatViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomePage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel, catViewModel: CatViewModel){
    val context = LocalContext.current
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val authState = authViewModel.authState.observeAsState()
    var isContentReady by remember { mutableStateOf(false) }

    // Check ff user not Authenticated then it will go back to welcome page
    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> {
                navController.navigate("welcome")
            }
            else -> {
                catViewModel.run()
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
            containerColor = Color(red = 226, green = 255, blue = 172),
            topBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp, top = 15.dp, end = 12.dp)

                ) {
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
                        onClick = { scope.launch { drawerState.open() } },
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
            },
            bottomBar = {
                Button(
                    onClick = {
                        navController.navigate(route = "addCat")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(red = 122, green = 188, blue = 0),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)


                ) {
                    Text(text = "ADD CAT")
                }
            }

        ) { innerPadding ->

            if (isContentReady) {
                ScafoldContent(innerPadding, catViewModel, navController)
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(60.dp, 60.dp),
                        color = Color(red = 122, green = 188, blue = 0),
                    )
                }
            }
        }
    }
}

@Composable
fun ScafoldContent(innerPadding: PaddingValues, catViewModel: CatViewModel, navController: NavController){
    val users by catViewModel.users.collectAsState(initial = emptyList())
    val isLoading by catViewModel.isLoading.collectAsState()
    Log.d("Previous", "Blanko $users")

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
        val catChunked by remember(users) {
            derivedStateOf {
                users.map { user ->
                    Cat(
                        catName = user.catName,
                        catColor = user.catColor,
                        catId = user.catId,
                        catBreed = user.catBreed,
                        imageRes = android.R.drawable.ic_menu_camera
                    )
                }.chunked(2)
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(red = 226, green = 255, blue = 172))

        ) {
            if (catChunked.isEmpty()){
                items(1) { _ ->
                    EmptyList()
                }
            } else {
                items(catChunked.size) { catPair ->
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (catChunked[catPair].size == 1) {
                            Spacer(modifier = Modifier.weight(1f))

                            val currentCat = catChunked[catPair][0]

                            for (cat in users) {
                                if (cat.catName == currentCat.catName) {
                                    CatInfoBox(
                                        catName = cat.catName,
                                        catId = cat.catId,
                                        catBreed = cat.catBreed,
                                        imageRes = android.R.drawable.ic_menu_camera,
                                        navController = navController
                                    )
                                }
                            }
                        } else {
                            Log.d("CatData", " Double $catChunked")
                            for (cat in catChunked[catPair]) {
                                CatInfoBox(
                                    catName = cat.catName,
                                    catId = cat.catId,
                                    catBreed = cat.catBreed,
                                    imageRes = cat.imageRes,
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

@Composable
fun DrawerMenu(authViewModel: AuthViewModel, context: Context) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(320.dp)
            .background(Color(red = 226, green = 255, blue = 172))
            .padding(16.dp)
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

@Composable
fun EmptyList(){
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,

    ){
        Spacer(modifier = Modifier.height(200.dp))
        Text(
            text = "Empty List of Cats",
            fontSize = 25.sp,
            color = Color(red = 122, green = 188, blue = 0),
        )
    }
}


data class Cat(
    val catName : String? = null,
    val catColor : String? = null,
    val catId : String? = null,
    val catBreed : String? = null,
    val imageRes: Int,
)

