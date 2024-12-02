package com.cevdetkilickeser.learnconnect.data.repository

import com.cevdetkilickeser.learnconnect.data.entity.User
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

    suspend fun signUp(email: String, password: String): Int {
        val user = User(email = email, password = password)
        return withContext(Dispatchers.IO) {
            if (!userDao.isEmailExists(user.email)) {
                val userId = userDao.addUser(user)
                userId.toInt()
            } else {
                -1
            }
        }
    }
}