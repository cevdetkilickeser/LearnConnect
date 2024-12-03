package com.cevdetkilickeser.learnconnect.data.repository

import com.cevdetkilickeser.learnconnect.data.entity.course.Enrollment
import com.cevdetkilickeser.learnconnect.data.entity.course.LessonStatus
import com.cevdetkilickeser.learnconnect.data.room.EnrollmentDao
import com.cevdetkilickeser.learnconnect.data.room.LessonDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EnrollmentRepository @Inject constructor(
    private val enrollmentDao: EnrollmentDao,
    private val lessonDao: LessonDao
) {
    suspend fun checkEnrollmentStatus(userId: Int, courseId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            enrollmentDao.checkEnrollmentStatus(userId, courseId)
        }
    }

    suspend fun enrollToCourse(enrollment: Enrollment): Boolean {
        val enrollmentId = withContext(Dispatchers.IO) {
            enrollmentDao.enrollToCourse(enrollment)
        }
        if (enrollmentId > 0) {
            fillLessonStatusTable(enrollment.userId, enrollment.courseId)
        }
        return enrollmentId > 0
    }

    private suspend fun fillLessonStatusTable(userId: Int, courseId: Int) {
        val lessons = lessonDao.getLessonsByCourseId(courseId)
        for (lesson in lessons) {
            val lessonStatus = LessonStatus(0, userId, lesson)
            enrollmentDao.fillLessonStatusTable(lessonStatus)
        }
    }

    private suspend fun cleanLessonStatusTable(userId: Int, courseId: Int) {
        enrollmentDao.cleanLessonStatusTable(userId, courseId)
    }

    suspend fun unEnroll(userId: Int, courseId: Int) {
        return withContext(Dispatchers.IO) {
            enrollmentDao.unEnroll(userId, courseId)
            cleanLessonStatusTable(userId, courseId)
        }
    }
}