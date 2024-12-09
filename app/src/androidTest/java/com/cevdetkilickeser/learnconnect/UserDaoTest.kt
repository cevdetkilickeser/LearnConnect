package com.cevdetkilickeser.learnconnect

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.cevdetkilickeser.learnconnect.data.entity.User
import com.cevdetkilickeser.learnconnect.data.room.AppDatabase
import com.cevdetkilickeser.learnconnect.data.room.UserDao
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@SmallTest
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class UserDaoTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var dao : UserDao
    private lateinit var database: AppDatabase

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.userDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun signUp() = runTest {
        val email = "email"
        val password = "password"
        val user = User(0,email,password)
        val result = dao.addUser(user)
        assertThat(result).isAtLeast(1)
    }

    @Test
    fun signUpWithExistsEmail() = runTest {
        val email = "email"
        val password = "password"
        val user = User(0,email,password)
        dao.addUser(user)
        val result = dao.isEmailExists(email)
        assertThat(result).isTrue()
    }

    @Test
    fun signUpWithUnExistsEmail() = runTest {
        val email = "email"
        val password = "password"
        val user = User(0,email,password)
        dao.addUser(user)
        val result = dao.isEmailExists("new_email")
        assertThat(result).isFalse()
    }

    @Test
    fun signInWithCorrectEmailAndCorrectPassword() = runTest {
        val email = "email"
        val password = "password"
        val user = User(0,email,password)
        dao.addUser(user)
        val result = dao.isUserExists(email, password)
        assertThat(result).isNotNull()
    }

    @Test
    fun signInWithWrongEmailAndCorrectPassword() = runTest {
        val email = "email"
        val password = "password"
        val user = User(0,email,password)
        dao.addUser(user)
        val result = dao.isUserExists("", password)
        assertThat(result).isNull()
    }

    @Test
    fun signInWithCorrectEmailAndWrongPassword() = runTest {
        val email = "email"
        val password = "password"
        val user = User(0,email,password)
        dao.addUser(user)
        val result = dao.isUserExists("", password)
        assertThat(result).isNull()
    }

    @Test
    fun signInWithWrongEmailAndWrongPassword() = runTest {
        val email = "email"
        val password = "password"
        val user = User(0,email,password)
        dao.addUser(user)
        val result = dao.isUserExists("", "")
        assertThat(result).isNull()
    }

    @Test
    fun getUserInfoWithCorrectId() = runTest {
        val email = "email"
        val password = "password"
        val user = User(1,email,password)
        dao.addUser(user)
        val result = dao.getUserInfo(1)
        assertThat(result).isEqualTo(user)
    }

    @Test
    fun getUserInfoWithWrongId() = runTest {
        val email = "email"
        val password = "password"
        val user = User(1,email,password)
        dao.addUser(user)
        val result = dao.getUserInfo(2)
        assertThat(result).isNotEqualTo(user)
    }

    @Test
    fun changePassword() = runTest {
        val email = "email"
        val password = "password"
        val newPassword = "new_password"
        val user = User(1,email,password)
        dao.addUser(user)
        val result = dao.changePassword(1, password, newPassword)
        assertThat(result).isEqualTo(1)
    }

    @Test
    fun changeName() = runTest {
        val email = "email"
        val password = "password"
        val name = "name"
        val user = User(1,email,password)
        dao.addUser(user)
        val result = dao.changeName(1, name)
        assertThat(result).isEqualTo(1)
    }
}