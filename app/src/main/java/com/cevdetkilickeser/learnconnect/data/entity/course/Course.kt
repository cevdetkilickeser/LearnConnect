package com.cevdetkilickeser.learnconnect.data.entity.course

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "courses")
data class Course(
    @PrimaryKey val courseId: Int,
    val courseName: String,
    val author: String,
    val category: String
)
