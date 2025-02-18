package com.example.pawtrack.pages

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pawtrack.R
import com.example.pawtrack.UserCat
import com.example.pawtrack.viewmodel.AuthState
import com.example.pawtrack.viewmodel.AuthViewModel
import com.example.pawtrack.viewmodel.CatViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import com.example.pawtrack.ui.theme.Coffee
import com.example.pawtrack.ui.theme.MainColor
import com.example.pawtrack.ui.theme.TextSubColor
import com.example.pawtrack.ui.theme.SubColor
import com.example.pawtrack.ui.theme.CaramelColor
import com.example.pawtrack.ui.theme.AlertColor
import kotlinx.coroutines.launch

@Composable
fun EditCatPage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel, catViewModel: CatViewModel, catId: String) {
    val auth : FirebaseAuth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser?.uid.toString()
    val authState = authViewModel.authState.observeAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current


    // Check ff user not Authenticated then it will go back to welcome page
    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> {
                navController.navigate("welcome")
            }
            else -> {
                catViewModel.fetchOneCat(catId)
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
            EditCatField(innerPadding, navController, catViewModel, currentUser, catId)

        }
    }


}

@Composable
fun EditCatField(innerPadding: PaddingValues, navController: NavController, catViewModel: CatViewModel, currentUser : String, catId: String) {
    val context = LocalContext.current
    val uniqueCat by catViewModel.uniqueCat.collectAsState()
    var catName by remember { mutableStateOf(uniqueCat?.catName) }
    var catColor by remember { mutableStateOf(uniqueCat?.catColor) }
    var catBreed by remember { mutableStateOf(uniqueCat?.catBreed) }
    val subColor = Color(red = 122, green = 188, blue = 0)

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
        catName?.let {
            OutlinedTextField(
                value = it,
                onValueChange = { catName = it },
                label = { Text("Cat Name") },
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

        // Cat Color TextField
        catColor?.let {
            OutlinedTextField(
                value = it,
                onValueChange = { catColor = it },
                label = { Text("Cat Color") },
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

        // Cat Id TextField
        catBreed?.let {
            OutlinedTextField(
                value = it,
                onValueChange = { catBreed = it },
                label = { Text("Cat Breed") },
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
                    navController.navigate("catTrack/${catId}")
                    Toast.makeText(context, "Unsuccessful to Edit Cat", Toast.LENGTH_SHORT).show()
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
                    val cat = UserCat(
                        catName  = catName,
                        catColor = catColor,
                        catId= catId,
                        catBreed = catBreed
                    )
                    catViewModel.editCat(cat, currentUser, catId)
                    navController.navigate("catTrack/${catId}")
                    Toast.makeText(context, "Edit Cat Successfully", Toast.LENGTH_SHORT).show()
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