package com.example.pawtrack.model

import kotlinx.coroutines.delay

class UserCat {
    suspend fun fetchUserCat(): Cat {
        delay(3000)
        return Cat("mat", "Red")
    }
}