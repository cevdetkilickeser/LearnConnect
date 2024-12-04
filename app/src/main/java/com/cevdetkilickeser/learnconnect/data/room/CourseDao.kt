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

    @Query("""
    SELECT c.* 
    FROM courses c
    INNER JOIN enrollments e ON c.id = e.courseId
    LEFT JOIN lesson_status ls ON e.courseId = ls.courseId AND e.userId = ls.userId
    WHERE e.userId = :userId
    GROUP BY c.id
    HAVING COUNT(CASE WHEN ls.isWatched = 0 THEN 1 ELSE NULL END) = 0
""")
    suspend fun getCoursesDone(userId: Int): List<Course>

    @Query("""
    SELECT c.* 
    FROM courses c
    INNER JOIN enrollments e ON c.id = e.courseId
    LEFT JOIN lesson_status ls ON e.courseId = ls.courseId AND e.userId = ls.userId
    WHERE e.userId = :userId
    GROUP BY c.id
    HAVING COUNT(CASE WHEN ls.isWatched = 0 THEN 1 ELSE NULL END) > 0
""")
    suspend fun getCoursesInProgress(userId: Int): List<Course>

}