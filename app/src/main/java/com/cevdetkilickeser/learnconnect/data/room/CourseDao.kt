package com.cevdetkilickeser.learnconnect.data.room

import androidx.room.Dao
import androidx.room.Query
import com.cevdetkilickeser.learnconnect.data.entity.course.Course

@Dao
interface CourseDao {

    @Query("SELECT DISTINCT category FROM courses")
    suspend fun getCategories(): List<String>

    @Query("SELECT * FROM courses")
    suspend fun getCourses(): List<Course>

    @Query("SELECT * FROM courses WHERE id = :id")
    suspend fun getCourseById(id: Int): Course

}