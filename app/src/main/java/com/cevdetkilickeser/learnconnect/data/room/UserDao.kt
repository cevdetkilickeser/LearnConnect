package com.cevdetkilickeser.learnconnect.data.room

import androidx.room.Query

interface UserDao {

    @Query("SELECT id FROM users WHERE email = :email AND password = :password")
    suspend fun isUserExists(email: String, password: String): Int?
}