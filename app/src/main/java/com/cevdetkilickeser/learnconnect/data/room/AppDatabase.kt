package com.cevdetkilickeser.learnconnect.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.cevdetkilickeser.learnconnect.data.entity.User
import com.cevdetkilickeser.learnconnect.data.entity.course.Course
import com.cevdetkilickeser.learnconnect.data.entity.course.Enrollment
import com.cevdetkilickeser.learnconnect.data.entity.course.Lesson
import com.cevdetkilickeser.learnconnect.data.entity.course.LessonStatus

@Database(
    entities = [User::class, Course::class, Lesson::class, LessonStatus::class, Enrollment::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun courseDao(): CourseDao
    abstract fun lessonDao(): LessonDao
}