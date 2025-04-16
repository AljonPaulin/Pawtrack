package com.example.pawtrack.pages

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pawtrack.R
import com.example.pawtrack.ui.theme.AlertColor
import com.example.pawtrack.ui.theme.Coffee
import com.example.pawtrack.ui.theme.MainColor
import com.example.pawtrack.viewmodel.AuthState
import com.example.pawtrack.viewmodel.AuthViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AccountPage(modifier: Modifier = Modifier, navController: NavController,  authViewModel: AuthViewModel ) {
    val context = LocalContext.current
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val authState = authViewModel.authState.observeAsState()
    var isContentReady by remember { mutableStateOf(false) }

    // Check if user not Authenticated then it will go back to welcome page
    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> {
                navController.navigate("welcome")
            }
            else ->  {
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
                                tint = AlertColor,
                                modifier = Modifier.size(35.dp)
                            )
                            Text(
                                text = "Home",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = AlertColor
                            )

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
                                color = AlertColor
                            )

                        }
                    }
                    TextButton( onClick = {
                        navController.navigate("account")

                    }) {
                        Column (
                            horizontalAlignment = Alignment.CenterHorizontally,

                            ) {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = "Account Icon",
                                tint = Coffee,
                                modifier = Modifier.size(35.dp)
                            )
                            Text(
                                text = "Account",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Coffee
                            )

                        }
                    }

                }
            }

        ) { innerPadding ->

            if(isContentReady) {
                    ContentAccountPage(innerPadding, authViewModel, navController)
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
fun ContentAccountPage( innerPadding: PaddingValues, authViewModel: AuthViewModel, navController: NavController){
    val context = LocalContext.current
    var isEditingName by remember { mutableStateOf(false) }
    var isEditingEmail by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf(authViewModel.getUserCredentials().name) }
    var email by remember { mutableStateOf(authViewModel.getUserCredentials().email) }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        // Profile Image
        Image(
            painter = painterResource(id = R.drawable.dog), // Replace with your image
            contentDescription = "User Profile",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .border(2.dp, Color.Gray, CircleShape)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // User Name
        Text(
            text = name,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Account Details Section
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 10.dp)
            ){
                AccountDetailRow("Name", name, isEditingName) { name = it }
                if (!isEditingName) {
                    Button(
                        onClick = {
                            isEditingName = !isEditingName
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Coffee,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Edit")
                    }
                }
            }
            if (isEditingName) {
                Spacer(modifier = Modifier.height(20.dp))
                Row {
                    Button(
                        onClick = {
                            isEditingName = !isEditingName
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Coffee,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    Button(
                        onClick = {
                            isEditingName = !isEditingName
                            authViewModel.updateUserProfileName(name)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Coffee,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Save")
                    }

                }


            }
            Spacer(modifier = Modifier.height(16.dp))
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 10.dp)
            ){
                AccountDetailRow("Email", email, isEditingEmail) { email = it }
                if (!isEditingEmail) {
                    Button(
                        onClick = {
                            isEditingEmail = !isEditingEmail
                            Toast.makeText(context, "You want to edit the email then you must also fill your password", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Coffee,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Edit")
                    }
                }
            }
            if (isEditingEmail) {
                AccountDetailRow("Password", password, isEditingEmail) { password = it }
                Spacer(modifier = Modifier.height(20.dp))
                Row {
                    Button(
                        onClick = {
                            isEditingEmail = !isEditingEmail
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Coffee,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    Button(
                        onClick = {
                            isEditingEmail = !isEditingEmail
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Coffee,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Save")
                    }

                }

            }
        }
    }

}

@Composable
fun AccountDetailRow(label: String, value: String, isEditing: Boolean, onValueChange: (String) -> Unit) {
    Column(
        modifier = Modifier.width(if (isEditing) 350.dp else 230.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = label, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = if (isEditing) Coffee else Color.Black)
        if (isEditing) {
            TextField(
                value = value,
                onValueChange = onValueChange,
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
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        } else {
            Text(
                text = value,
                fontSize = 16.sp,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
        HorizontalDivider(color = if (isEditing) Coffee else Color.Black )
    }
}
