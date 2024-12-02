package com.cevdetkilickeser.learnconnect.data.room

import androidx.room.Insert
import androidx.room.Query
import com.cevdetkilickeser.learnconnect.data.entity.User

interface UserDao {

    @Query("SELECT id FROM users WHERE email = :email AND password = :password")
    suspend fun isUserExists(email: String, password: String): Int?

    @Query("SELECT COUNT(*) FROM users WHERE email = :email")
    suspend fun isEmailExists(email: String): Boolean

    @Insert
    suspend fun addUser(user: User): Long
}