package com.example.pawtrack.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pawtrack.UserDog
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

class DogViewModel : ViewModel() {
    private val database= FirebaseDatabase.getInstance().getReference("users")
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    private val _users = MutableStateFlow<List<UserDog>>(emptyList())
    val users: StateFlow<List<UserDog>> = _users

    private val _uniqueDog = MutableStateFlow<UserDog?>(null)
    val uniqueDog: StateFlow<UserDog?> = _uniqueDog

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
        val currentUser = auth.currentUser?.uid.toString()
        val shortUserId = if (currentUser.length >= 4) currentUser.substring(0, 4) else currentUser

        val userRef = database.child(shortUserId)

        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(intialSnapshot: DataSnapshot) {
                if (intialSnapshot.exists()) {
                    val dogsRef = database.child(shortUserId).child("dogs")

                    dogsRef.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val userList = mutableListOf<UserDog>()
                            for (userSnapshot in snapshot.children) {
                                val user : UserDog? = userSnapshot.getValue(UserDog::class.java)
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
     fun fetchOneDog(dogId : String) {
        val currentUser = auth.currentUser?.uid.toString()
        val shortUserId = if (currentUser.length >= 4) currentUser.substring(0, 4) else currentUser

        val dogRef = database.child(shortUserId).child("dogs").child(dogId)
        Log.d("Previous", "Current User : $shortUserId")

        dogRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    Log.d("Previous", "One Dog : $snapshot")
                    val dog: UserDog? = snapshot.getValue(UserDog::class.java)
                    if (dog != null) {
                        _uniqueDog.value = dog
                    }
                } else {
                    _uniqueDog.value = null
                    Log.d("FirebaseCheck", "Dog ID does not exist.")
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error: ${error.message}")
            }
        })


    }

    fun addDog(dog : UserDog?, userId : String){
        val temp = if (userId.length >= 4) userId.substring(0, 4) else userId
        var shortUserId = temp
        val dogsRef = database.child(shortUserId).child("dogs")

        dog?.let {
            val dogId = it.dogId?.ifEmpty {
                UUID.randomUUID().toString()
            }
            val dogRef = dogId?.let { it1 -> dogsRef.child(it1) }
            dogRef?.setValue(it)
                ?.addOnSuccessListener {
                    _users.value += dog
                    Log.d("Firebase", "Dog added successfully for user!")

                }
                ?.addOnFailureListener { exception ->
                    Log.e("Firebase", "Error adding dog for user", exception)
                }
        } ?: Log.w("Firebase","Dog object is null. Nothing to add.")
    }

    fun editDog(dog : UserDog?, userId : String, dogId: String){
        val temp = if (userId.length >= 4) userId.substring(0, 4) else userId
        var shortUserId = temp
        val dogsRef = database.child(shortUserId).child("dogs")

        dog?.let {
            val dogRef = dogsRef.child(dogId)
            dogRef.setValue(it)
                .addOnSuccessListener {
                    _uniqueDog.value = dog
                    Log.d("Firebase", "Dog edit successfully for user!")

                }
                .addOnFailureListener { exception ->
                    Log.e("Firebase", "Error edit dog for user", exception)
                }
        } ?: Log.w("Firebase","Dog object is null. Nothing to edit.")
    }
    fun deleteDog(dogId: String) {
        val currentUser = auth.currentUser?.uid.toString()
        val shortUserId = if (currentUser.length >= 4) currentUser.substring(0, 4) else currentUser

        val dogRef = database.child(shortUserId).child("dogs").child(dogId)

        dogRef.removeValue()
            .addOnSuccessListener {
                Log.d("Firebase", "Dog deleted successfully for user!")
                _users.value = _users.value.filter { it.dogId != dogId }
            }
            .addOnFailureListener { exception ->
                Log.e("Firebase", "Error deleting dog for user", exception)
            }
    }
}