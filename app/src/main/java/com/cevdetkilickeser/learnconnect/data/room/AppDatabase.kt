package com.cevdetkilickeser.learnconnect.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cevdetkilickeser.learnconnect.data.entity.User

@Database(
    entities = [User::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}