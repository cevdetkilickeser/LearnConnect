package com.cevdetkilickeser.learnconnect.data.repository

import android.net.Uri
import com.cevdetkilickeser.learnconnect.data.entity.User

interface UserRepository {

    suspend fun signUp(email: String, password: String): Int

    suspend fun signIn(email: String, password: String): Int

    suspend fun getUserInfo(userId: Int): User

    suspend fun changePassword(userId: Int, currentPassword: String, newPassword: String): Int

    suspend fun changeName(userId: Int, name: String)

    suspend fun uploadImage(userId: Int, uri: Uri?)
}