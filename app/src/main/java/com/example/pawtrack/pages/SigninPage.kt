package com.example.pawtrack.pages

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pawtrack.viewmodel.AuthState
import com.example.pawtrack.viewmodel.AuthViewModel

@Composable
fun SigninPage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var password_2 by remember { mutableStateOf("") }
    val sub_color = Color(red = 122, green = 188, blue = 0)
    val authState = authViewModel.authState.observeAsState()

    LaunchedEffect(authState.value) {
        when(authState.value){
            is AuthState.Authenticated -> navController.navigate("home")
            is AuthState.Error -> Toast.makeText(context,
                (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT).show()
            else -> Unit

        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(red = 226, green = 255, blue = 172))
            .padding(40.dp, 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "PawTrack",
            color = Color(red = 122, green = 188, blue = 0),
            textAlign = TextAlign.Center,
            fontSize = 50.sp,
            fontFamily = FontFamily.Cursive,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Email TextField
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                focusedLabelColor = sub_color,
                unfocusedLabelColor = sub_color,
                focusedIndicatorColor = sub_color,
                unfocusedIndicatorColor = sub_color,
                focusedTextColor =  sub_color,
                unfocusedTextColor =  sub_color,
                cursorColor =  sub_color
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                focusedLabelColor = sub_color,
                unfocusedLabelColor = sub_color,
                focusedIndicatorColor = sub_color,
                unfocusedIndicatorColor = sub_color,
                focusedTextColor =  sub_color,
                unfocusedTextColor =  sub_color,
                cursorColor =  sub_color
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password TextField
        var isPasswordVisible by remember { mutableStateOf(false) }

        OutlinedTextField(
            value = password_2,
            onValueChange = { text2 -> password_2 = text2 },
            label = { Text(text = "Password") },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                focusedLabelColor = sub_color,
                unfocusedLabelColor = sub_color,
                focusedIndicatorColor = sub_color,
                unfocusedIndicatorColor = sub_color,
                focusedTextColor =  sub_color,
                unfocusedTextColor =  sub_color,
                cursorColor =  sub_color

            ),
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                Text(
                    text = if (isPasswordVisible) "HIDE" else "SHOW",
                    fontSize = 15.sp,
                    color = sub_color,
                    modifier = Modifier.padding(end = 10.dp)
                        .clickable { isPasswordVisible = !isPasswordVisible }
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password TextField
        var isConfirmPasswordVisible by remember { mutableStateOf(false) }

        OutlinedTextField(
            value = password,
            onValueChange = { text2 -> password = text2 },
            label = { Text(text = "Confirm Password") },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                focusedLabelColor = sub_color,
                unfocusedLabelColor = sub_color,
                focusedIndicatorColor = sub_color,
                unfocusedIndicatorColor = sub_color,
                focusedTextColor =  sub_color,
                unfocusedTextColor =  sub_color,
                cursorColor =  sub_color

            ),
            visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                Text(
                    text = if (isConfirmPasswordVisible) "HIDE" else "SHOW",
                    fontSize = 15.sp,
                    color = sub_color,
                    modifier = Modifier.padding(end = 10.dp)
                        .clickable { isConfirmPasswordVisible = !isConfirmPasswordVisible }
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Login Button
        Button(
            onClick = {
                authViewModel.signin(email, password)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(red = 122, green = 188, blue = 0),
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = authState.value != AuthState.Loading
        ) {
            Text(text = "Sign In")
        }
        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = {
            navController.navigate(route = "login") },
            colors = ButtonDefaults.textButtonColors(
                contentColor =  Color(red = 122, green = 188, blue = 0)
            )
            ) {
            Text(text = "Already have an account, Login")
        }
    }
}