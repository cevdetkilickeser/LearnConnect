package com.cevdetkilickeser.learnconnect.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cevdetkilickeser.learnconnect.data.entity.course.Enrollment
import com.cevdetkilickeser.learnconnect.data.entity.course.LessonStatus

@Dao
interface EnrollmentDao {

    @Query("SELECT COUNT(*) FROM enrollments WHERE userId = :userId AND courseId = :courseId")
    suspend fun checkEnrollmentStatus(userId: Int, courseId: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun enrollToCourse(enrollment: Enrollment): Long

    @Query("DELETE FROM enrollments WHERE userId = :userId AND courseId = :courseId")
    suspend fun unEnroll(userId: Int, courseId: Int)

    @Insert
    suspend fun fillLessonStatusTable(lessonStatus: LessonStatus)

    @Query("DELETE FROM lesson_status WHERE userId = :userId AND courseId = :courseId")
    suspend fun cleanLessonStatusTable(userId: Int, courseId: Int)
}