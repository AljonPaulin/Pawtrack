package com.example.pawtrack.pages

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
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
import androidx.compose.material3.AlertDialog
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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
import com.example.pawtrack.ui.theme.Coffee
import com.example.pawtrack.ui.theme.MainColor
import com.example.pawtrack.ui.theme.TextSubColor
import com.example.pawtrack.ui.theme.AlertColor
import com.example.pawtrack.ui.theme.CaramelColor
import com.google.firebase.Firebase
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.auth
import java.util.concurrent.TimeUnit


@Composable
fun SigninPage(modifier: Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {
    val context = LocalContext.current
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var password_2 by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf(false) }
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
            .background(MainColor)
            .padding(40.dp, 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "PawTrack",
            color = Coffee,
            textAlign = TextAlign.Center,
            fontSize = 50.sp,
            fontFamily = FontFamily.Cursive,
            modifier = Modifier
                .fillMaxWidth()
        )

        Text(
            text = "Create your account to start tracking your dog activities",
            color = Coffee,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Name TextField
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
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

        // Phone TextField
        OutlinedTextField(
            value = phone,
            onValueChange = {
                // Accept only digits
                if (it.all { char -> char.isDigit() }) {
                    phone = it
                } },
            label = { Text("Phone Number") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
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

        // Password TextField
        var isPasswordVisible by remember { mutableStateOf(false) }

        OutlinedTextField(
            value = password,
            onValueChange = { text1 -> password = text1 },
            label = { Text(text = "Password") },
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
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                Text(
                    text = if (isPasswordVisible) "HIDE" else "SHOW",
                    fontSize = 15.sp,
                    color = Coffee,
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .clickable { isPasswordVisible = !isPasswordVisible }
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password TextField
        var isConfirmPasswordVisible by remember { mutableStateOf(false) }

        OutlinedTextField(
            value = password_2,
            onValueChange = { text2 -> password_2 = text2 },
            label = { Text(text = "Confirm Password") },
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
            visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                Text(
                    text = if (isConfirmPasswordVisible) "HIDE" else "SHOW",
                    fontSize = 15.sp,
                    color = Coffee,
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .clickable { isConfirmPasswordVisible = !isConfirmPasswordVisible }
                )
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Sign Button
        Button(
            onClick = {
                if(password != password_2) {
                    passwordError = true
                } else {
                    passwordError = false
                    if (phone.startsWith("0")){
                        val temp = phone.drop(1)
                        val fullPhone = "+63${temp}"
                        val randomCode = (1000..9999).random().toString()
                        authViewModel.pendingName = name
                        authViewModel.pendingEmail = email
                        authViewModel.pendingPassword = password
                        authViewModel.pendingPhone = phone
                        navController.navigate(route = "verify/${fullPhone}/${randomCode}")
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Coffee,
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
                contentColor =  Coffee
            )
            ) {
            Text(text = "Already have an account, Login")
        }
        if (passwordError) {
            Toast.makeText(context, "Password does not match", Toast.LENGTH_SHORT).show()
        }
    }
}

//Failure Code
fun otp(phone : String, auth: FirebaseAuth, context: Context){
    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Log.d("PhoneAuth", "Verification completed: ${credential.smsCode}")
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Log.e("PhoneAuth", "Verification failed: ${e.message}")
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            Log.d("PhoneAuth", "Code sent to user. ID: $verificationId")
        }
    }

    val options = PhoneAuthOptions.newBuilder(auth)
        .setPhoneNumber("+63$phone") // Phone number to verify
        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
        .setActivity(context as Activity) // Activity (for callback binding)
        .setCallbacks(callbacks)
        .build()
    Log.d("PhoneAuth", "Options: $options")
    PhoneAuthProvider.verifyPhoneNumber(options)

}

