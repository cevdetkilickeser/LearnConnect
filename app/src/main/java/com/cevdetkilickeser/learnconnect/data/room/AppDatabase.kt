package com.cevdetkilickeser.learnconnect.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cevdetkilickeser.learnconnect.data.entity.User
import com.cevdetkilickeser.learnconnect.data.entity.course.Comment
import com.cevdetkilickeser.learnconnect.data.entity.course.Course
import com.cevdetkilickeser.learnconnect.data.entity.course.Enrollment
import com.cevdetkilickeser.learnconnect.data.entity.course.Lesson
import com.cevdetkilickeser.learnconnect.data.entity.course.LessonStatus

@Database(
    entities = [Course::class, Lesson::class, Comment::class, User::class, Enrollment::class, LessonStatus::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun courseDao(): CourseDao
    abstract fun lessonDao(): LessonDao
}