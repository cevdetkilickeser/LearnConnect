package com.cevdetkilickeser.learnconnect.domain.repository

import com.cevdetkilickeser.learnconnect.data.entity.course.LessonStatus
import com.cevdetkilickeser.learnconnect.data.repository.LessonRepository
import com.cevdetkilickeser.learnconnect.data.room.LessonDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LessonRepositoryImpl @Inject constructor(private val lessonDao: LessonDao) :
    LessonRepository {

    override suspend fun getLessonsByUserIdAndCourseId(userId: Int, courseId: Int): List<LessonStatus> {
        return withContext(Dispatchers.IO) {
            lessonDao.getLessonsByUserIdAndCourseId(userId, courseId)
        }
    }

    override suspend fun updateLessonIsWatchedStatus(userId: Int, courseId: Int, lessonNumber: Int) {
        return withContext(Dispatchers.IO) {
            lessonDao.updateLessonIsWatchedStatus(userId, courseId, lessonNumber)
        }
    }

    override suspend fun saveCurrentPositionToRoom(
        userId: Int,
        courseId: Int,
        lessonNumber: Int,
        lastPosition: Long
    ) {
        withContext(Dispatchers.IO) {
            lessonDao.saveCurrentPositionToRoom(userId, courseId, lessonNumber, lastPosition)
        }
    }

    override suspend fun getLastPosition(userId: Int, courseId: Int, lessonNumber: Int): Long {
        return withContext(Dispatchers.IO) {
            lessonDao.getLastPosition(userId, courseId, lessonNumber)
        } ?: 0L
    }

    override suspend fun getLastWatchedLessonNumber(userId: Int, courseId: Int): Int {
        return withContext(Dispatchers.IO) {
            lessonDao.getLastWatchedLessonNumber(userId, courseId)
        } ?: 1
    }

    override suspend fun updateIsLastWatched(userId: Int, courseId: Int, lessonNumber: Int) {
        withContext(Dispatchers.IO) {
            lessonDao.updateIsLastWatched(userId, courseId, lessonNumber)
        }
    }

    override suspend fun saveDownloadedFilePath(
        userId: Int,
        courseId: Int,
        lessonNumber: Int,
        downloadedUrl: String?
    ) {
        if (downloadedUrl != null) {
            lessonDao.saveDownloadedFilePath(userId, courseId, lessonNumber, downloadedUrl)
        }
    }
}