package com.cevdetkilickeser.learnconnect.data.entity.course

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.cevdetkilickeser.learnconnect.data.entity.User

@Entity(
    tableName = "lesson_status",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("userId")]
)
data class LessonStatus(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val userId: Int,
    @Embedded val lesson: Lesson,
    val lastPosition: Long = 0L,
    val isWatched: Boolean = false,
    val isLastWatched: Boolean = false,
    val isDownloaded: Boolean = false
)

