package com.cevdetkilickeser.learnconnect.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cevdetkilickeser.learnconnect.data.entity.course.Comment
import com.cevdetkilickeser.learnconnect.data.entity.course.Course
import com.cevdetkilickeser.learnconnect.data.entity.course.Enrollment
import com.cevdetkilickeser.learnconnect.data.entity.course.LessonStatus

@Dao
interface CourseDao {

    @Query("SELECT DISTINCT category FROM courses")
    suspend fun getCategories(): List<String>

    @Query("SELECT * FROM courses")
    suspend fun getCourses(): List<Course>

    @Query("SELECT * FROM courses WHERE courseId = :id")
    suspend fun getCourseById(id: Int): Course

    @Query(
        """
    SELECT c.* 
    FROM courses c
    INNER JOIN enrollments e ON c.courseId = e.courseId
    LEFT JOIN lesson_status ls ON e.courseId = ls.courseId AND e.userId = ls.userId
    WHERE e.userId = :userId
    GROUP BY c.courseId
    HAVING COUNT(CASE WHEN ls.isWatched = 0 THEN 1 ELSE NULL END) = 0
"""
    )
    suspend fun getCoursesDone(userId: Int): List<Course>

    @Query(
        """
    SELECT c.* 
    FROM courses c
    INNER JOIN enrollments e ON c.courseId = e.courseId
    LEFT JOIN lesson_status ls ON e.courseId = ls.courseId AND e.userId = ls.userId
    WHERE e.userId = :userId
    GROUP BY c.courseId
    HAVING COUNT(CASE WHEN ls.isWatched = 0 THEN 1 ELSE NULL END) > 0
"""
    )
    suspend fun getCoursesInProgress(userId: Int): List<Course>

    @Insert
    suspend fun addComment(comment: Comment)

    @Query("SELECT * FROM comments WHERE courseId = :courseId")
    suspend fun getComments(courseId: Int): List<Comment>

    @Query("SELECT COUNT(*) FROM enrollments WHERE userId = :userId AND courseId = :courseId")
    suspend fun checkEnrollmentStatus(userId: Int, courseId: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun enrollToCourse(enrollment: Enrollment): Long

    @Insert
    suspend fun fillLessonStatusTable(lessonStatus: LessonStatus)

    @Query("DELETE FROM lesson_status WHERE userId = :userId AND courseId = :courseId")
    suspend fun cleanLessonStatusTable(userId: Int, courseId: Int)
}