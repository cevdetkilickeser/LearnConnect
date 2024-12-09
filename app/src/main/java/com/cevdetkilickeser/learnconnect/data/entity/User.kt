package com.cevdetkilickeser.learnconnect.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val userId: Int = 0,
    val email: String,
    val password: String,
    val name: String? = null,
    val image: String? = null
)
