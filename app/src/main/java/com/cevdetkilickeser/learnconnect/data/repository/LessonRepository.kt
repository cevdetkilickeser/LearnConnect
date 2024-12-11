package com.cevdetkilickeser.learnconnect.data.repository

import com.cevdetkilickeser.learnconnect.data.entity.course.LessonStatus

interface LessonRepository {

    suspend fun getLessonsByUserIdAndCourseId(userId: Int, courseId: Int): List<LessonStatus>

    suspend fun updateLessonIsWatchedStatus(userId: Int, courseId: Int, lessonNumber: Int)

    suspend fun saveCurrentPositionToRoom(
        userId: Int,
        courseId: Int,
        lessonNumber: Int,
        lastPosition: Long
    )

    suspend fun getLastPosition(userId: Int, courseId: Int, lessonNumber: Int): Long

    suspend fun getLastWatchedLessonNumber(userId: Int, courseId: Int): Int

    suspend fun updateIsLastWatched(userId: Int, courseId: Int, lessonNumber: Int)

    suspend fun saveDownloadedFilePath(
        userId: Int,
        courseId: Int,
        lessonNumber: Int,
        downloadedUrl: String?
    )
}