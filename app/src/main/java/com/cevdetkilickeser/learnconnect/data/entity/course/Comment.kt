package com.cevdetkilickeser.learnconnect.data.entity.course

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.cevdetkilickeser.learnconnect.data.entity.User

@Entity(
    tableName = "comments",
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
data class Comment(
    @PrimaryKey(autoGenerate = true) val commentId: Int = 0,
    val courseId: Int,
    @Embedded val user: User,
    val commentDate: String,
    val comment: String,
    val rate: Int
)
