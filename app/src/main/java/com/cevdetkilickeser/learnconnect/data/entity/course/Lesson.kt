package com.cevdetkilickeser.learnconnect.data.entity.course

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "lessons",
    foreignKeys = [
        ForeignKey(
            entity = Course::class,
            parentColumns = ["courseId"],
            childColumns = ["courseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("courseId")]
)
data class Lesson(
    @PrimaryKey(autoGenerate = true) val lessonId: Int,
    val courseId: Int,
    val lessonNumber: Int,
    val lessonTitle: String,
    val lessonDuration: Int,
    val lessonUrl: String,
    val filePath: String?
)