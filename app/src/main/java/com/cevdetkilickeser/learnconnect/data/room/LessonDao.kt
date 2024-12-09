package com.cevdetkilickeser.learnconnect.data.room

import androidx.room.Dao
import androidx.room.Query
import com.cevdetkilickeser.learnconnect.data.entity.course.Lesson
import com.cevdetkilickeser.learnconnect.data.entity.course.LessonStatus

@Dao
interface LessonDao {

    @Query("SELECT * FROM lessons WHERE courseId = :courseId")
    suspend fun getLessonsByCourseId(courseId: Int): List<Lesson>

    @Query("SELECT * FROM lesson_status WHERE userId = :userId AND courseId = :courseId")
    suspend fun getLessonsByUserIdAndCourseId(userId: Int, courseId: Int): List<LessonStatus>

    @Query("UPDATE lesson_status SET isWatched = 1 WHERE userId = :userId AND courseId = :courseId AND lessonNumber = :lessonNumber")
    suspend fun updateLessonIsWatchedStatus(userId: Int, courseId: Int, lessonNumber: Int)

    @Query("UPDATE lesson_status SET lastPosition = :lastPosition WHERE userId = :userId AND courseId = :courseId AND lessonNumber = :lessonNumber")
    suspend fun saveCurrentPositionToRoom(userId: Int, courseId: Int, lessonNumber: Int, lastPosition: Long)

    @Query("SELECT lastPosition FROM lesson_status WHERE userId = :userId AND courseId = :courseId AND lessonNumber = :lessonNumber")
    suspend fun getLastPosition(userId: Int, courseId: Int, lessonNumber: Int): Long?

    @Query("SELECT lessonNumber FROM lesson_status WHERE userId = :userId AND courseId = :courseId AND isLastWatched = 1")
    suspend fun getLastWatchedLessonNumber(userId: Int, courseId: Int): Int?

    @Query(
        """
    UPDATE lesson_status
    SET isLastWatched = CASE
        WHEN lessonNumber = :lessonNumber THEN 1
        ELSE 0
    END
    WHERE userId = :userId AND courseId = :courseId
"""
    )
    suspend fun updateIsLastWatched(userId: Int, courseId: Int, lessonNumber: Int)

    @Query("UPDATE lesson_status SET filePath = :downloadedUrl, isDownloaded = 1 WHERE userId = :userId AND courseId = :courseId AND lessonNumber = :lessonNumber")
    suspend fun saveDownloadedFilePath(userId: Int, courseId: Int, lessonNumber: Int, downloadedUrl: String)
}