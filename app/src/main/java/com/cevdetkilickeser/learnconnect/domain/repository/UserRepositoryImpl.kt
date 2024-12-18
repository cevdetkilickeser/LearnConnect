package com.cevdetkilickeser.learnconnect.domain.repository

import android.net.Uri
import com.cevdetkilickeser.learnconnect.data.entity.User
import com.cevdetkilickeser.learnconnect.data.repository.UserRepository
import com.cevdetkilickeser.learnconnect.data.room.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val userDao: UserDao) : UserRepository {

    override suspend fun signUp(email: String, password: String): Int {
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

    override suspend fun signIn(email: String, password: String): Int {
        return withContext(Dispatchers.IO) {
            userDao.isUserExists(email, password) ?: -1
        }
    }

    override suspend fun getUserInfo(userId: Int): User {
        return withContext(Dispatchers.IO) {
            userDao.getUserInfo(userId)
        }
    }

    override suspend fun changePassword(
        userId: Int,
        currentPassword: String,
        newPassword: String
    ): Int {
        return withContext(Dispatchers.IO) {
            userDao.changePassword(userId, currentPassword, newPassword)
        }
    }

    override suspend fun changeName(userId: Int, name: String) {
        withContext(Dispatchers.IO) {
            userDao.changeName(userId, name)
        }
    }

    override suspend fun uploadImage(userId: Int, uri: Uri?) {
        uri?.let {
            withContext(Dispatchers.IO) {
                userDao.uploadImage(userId, it.toString())
            }
        }
    }
}