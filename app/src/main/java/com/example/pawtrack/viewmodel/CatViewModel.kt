package com.example.pawtrack.viewmodel

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pawtrack.UserCat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class CatViewModel : ViewModel() {
    private val database= FirebaseDatabase.getInstance().getReference("users")

    private val _users = MutableStateFlow<List<UserCat>>(emptyList())
    val users: StateFlow<List<UserCat>> = _users

    var isLoading = MutableStateFlow(true)

    fun run() {
        viewModelScope.launch {
            isLoading.value = true
            fetchUsers()
            isLoading.value = false
        }
    }

    private suspend fun fetchUsers() {
        delay(300)
        val auth : FirebaseAuth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser?.uid.toString()
        val shortUserId = if (currentUser.length >= 4) currentUser.substring(0, 4) else currentUser

        val userRef = database.child(shortUserId)
        Log.d("Previous", "Current User : $shortUserId")

        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(intialSnapshot: DataSnapshot) {
                if (intialSnapshot.exists()) {
                    Log.d("Previous", "Snapshot: $intialSnapshot")
                    val catsRef = database.child(shortUserId).child("cats")

                    catsRef.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val userList = mutableListOf<UserCat>()
                            for (userSnapshot in snapshot.children) {
                                val user : UserCat? = userSnapshot.getValue(UserCat::class.java)
                                if (user != null) {
                                    userList.add(user)
                                }
                            }
                            _users.value = userList
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.e("Firebase", "Error: ${error.message}")
                        }
                    })
                } else {
                    _users.value = emptyList()
                    Log.d("FirebaseCheck", "User ID does not exist.")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error: ${error.message}")
            }
        })


    }

    fun addCat(cat : UserCat?, userId : String){
        val temp = if (userId.length >= 4) userId.substring(0, 4) else userId
        var shortUserId = temp
        val catsRef = database.child(shortUserId).child("cats")

        cat?.let {
            val catId = it.catId?.ifEmpty {
                UUID.randomUUID().toString()
            }
            val catRef = catId?.let { it1 -> catsRef.child(it1) }
            catRef?.setValue(it)
                ?.addOnSuccessListener {
                    _users.value += cat
                    Log.d("Firebase", "Cat added successfully for user!")

                }
                ?.addOnFailureListener { exception ->
                    Log.e("Firebase", "Error adding cat for user", exception)
                }
        } ?: Log.w("Firebase","Cat object is null. Nothing to add.")
    }
}