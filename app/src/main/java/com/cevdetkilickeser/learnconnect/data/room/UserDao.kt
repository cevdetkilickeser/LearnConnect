package com.cevdetkilickeser.learnconnect.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.cevdetkilickeser.learnconnect.data.entity.User

@Dao
interface UserDao {

    @Insert
    suspend fun addUser(user: User): Long

    @Query("SELECT COUNT(*) FROM users WHERE email = :email")
    suspend fun isEmailExists(email: String): Boolean

    @Query("SELECT userId FROM users WHERE email = :email AND password = :password")
    suspend fun isUserExists(email: String, password: String): Int?

    @Query("SELECT * FROM users WHERE userId = :userId")
    suspend fun getUserInfo(userId: Int): User

    @Query(
        """
    UPDATE users 
    SET password = :newPassword 
    WHERE userId = :userId AND password = :currentPassword
"""
    )
    suspend fun changePassword(userId: Int, currentPassword: String, newPassword: String): Int

    @Query("UPDATE users SET name = :name WHERE userId = :userId")
    suspend fun changeName(userId: Int, name: String): Int

    @Query("UPDATE users SET image = :image WHERE userId = :userId")
    suspend fun uploadImage(userId: Int, image: String)

}