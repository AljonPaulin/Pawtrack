package com.example.pawtrack.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest

class AuthViewModel : ViewModel() {

    private val auth : FirebaseAuth = FirebaseAuth.getInstance()
    private val _authState = MutableLiveData<AuthState>()
    val authState : LiveData<AuthState> = _authState

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus(){
        if(auth.currentUser == null){
            _authState.value = AuthState.Unauthenticated
        }else{
            _authState.value = AuthState.Authenticated
        }
    }

    fun getUserCredentials(): UserCredential{
        val currentEmail = auth.currentUser?.email.toString()
        val name = auth.currentUser?.displayName.toString()
        return UserCredential(email = currentEmail, name = name)

    }

    fun login(email : String, password : String){
        if(email.isEmpty() || password.isEmpty()){
            _authState.value = AuthState.Error("Email or Password can't be empty")
            return
        }
        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {task->
                if(task.isSuccessful){
                    _authState.value = AuthState.Authenticated
                }else{
                    _authState.value = AuthState.Error(task.exception?.message?:"Something went wrong")
                }
            }
    }

    fun updateUserProfileName(newName: String) {
        val user = auth.currentUser
        if (user == null) {
            _authState.value = AuthState.Error("No user is signed in")
            return
        }
        _authState.value = AuthState.Loading

        val profileUpdates = userProfileChangeRequest {
            displayName = newName
        }

        user.updateProfile(profileUpdates)
            .addOnCompleteListener { updateTask ->
                if (updateTask.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error(updateTask.exception?.message ?: "Failed to update profile")
                }
            }
    }

    fun signin(name : String, email : String, password : String){
        if(email.isEmpty() || password.isEmpty() || name.isEmpty()){
            _authState.value = AuthState.Error("Name or Email or Password can't be empty")
            return
        }
        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {task->
                if(task.isSuccessful){
                    val user = auth.currentUser
                    val profileUpdates = userProfileChangeRequest {
                        displayName = name // Add the user's name
                    }
                    user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { updateTask ->
                            if (updateTask.isSuccessful) {
                                _authState.value = AuthState.Authenticated
                            } else {
                                _authState.value = AuthState.Error(updateTask.exception?.message ?: "Failed to update profile")
                            }
                        }
                }else{
                    _authState.value = AuthState.Error(task.exception?.message?:"Something went wrong")
                }
            }
    }
    fun signout(){
        auth.signOut()
        _authState.value = AuthState.Unauthenticated
    }

}

sealed class AuthState{
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message : String) : AuthState()
}

data class UserCredential(
    val email : String,
    val name : String
)
