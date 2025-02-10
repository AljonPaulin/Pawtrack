package com.example.pawtrack.model

import kotlinx.coroutines.delay

class UserRepository {
    suspend fun fetchUserData() : UserData{

        // mock api
        delay(2000)
        return UserData("aljon", 22)
    }
}