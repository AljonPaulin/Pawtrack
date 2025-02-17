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
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    private val _users = MutableStateFlow<List<UserCat>>(emptyList())
    val users: StateFlow<List<UserCat>> = _users

    private val _uniqueCat = MutableStateFlow<UserCat?>(null)
    val uniqueCat: StateFlow<UserCat?> = _uniqueCat

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
     fun fetchOneCat(catId : String) {
        val currentUser = auth.currentUser?.uid.toString()
        val shortUserId = if (currentUser.length >= 4) currentUser.substring(0, 4) else currentUser

        val catRef = database.child(shortUserId).child("cats").child(catId)
        Log.d("Previous", "Current User : $shortUserId")

        catRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    Log.d("Previous", "One Cat : $snapshot")
                    val cat: UserCat? = snapshot.getValue(UserCat::class.java)
                    if (cat != null) {
                        _uniqueCat.value = cat
                    }
                } else {
                    _uniqueCat.value = null
                    Log.d("FirebaseCheck", "Cat ID does not exist.")
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

    fun editCat(cat : UserCat?, userId : String, catId: String){
        val temp = if (userId.length >= 4) userId.substring(0, 4) else userId
        var shortUserId = temp
        val catsRef = database.child(shortUserId).child("cats")

        cat?.let {
            val catRef = catsRef.child(catId)
            catRef.setValue(it)
                .addOnSuccessListener {
                    _uniqueCat.value = cat
                    Log.d("Firebase", "Cat edit successfully for user!")

                }
                .addOnFailureListener { exception ->
                    Log.e("Firebase", "Error edit cat for user", exception)
                }
        } ?: Log.w("Firebase","Cat object is null. Nothing to edit.")
    }
    fun deleteCat(catId: String) {
        val currentUser = auth.currentUser?.uid.toString()
        val shortUserId = if (currentUser.length >= 4) currentUser.substring(0, 4) else currentUser

        val catRef = database.child(shortUserId).child("cats").child(catId)

        catRef.removeValue()
            .addOnSuccessListener {
                Log.d("Firebase", "Cat deleted successfully for user!")
                _users.value = _users.value.filter { it.catId != catId }
            }
            .addOnFailureListener { exception ->
                Log.e("Firebase", "Error deleting cat for user", exception)
            }
    }
}