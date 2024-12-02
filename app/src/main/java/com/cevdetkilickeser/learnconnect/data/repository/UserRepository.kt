package com.cevdetkilickeser.learnconnect.data.repository

import com.cevdetkilickeser.learnconnect.data.room.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepository @Inject constructor(private val userDao: UserDao) {

    suspend fun signIn(email: String, password: String): Int {
        return withContext(Dispatchers.IO) {
            userDao.isUserExists(email, password) ?: -1
        }
    }
}