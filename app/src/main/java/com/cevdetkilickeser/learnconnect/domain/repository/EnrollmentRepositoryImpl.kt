package com.cevdetkilickeser.learnconnect.domain.repository

import com.cevdetkilickeser.learnconnect.data.entity.course.Enrollment
import com.cevdetkilickeser.learnconnect.data.entity.course.LessonStatus
import com.cevdetkilickeser.learnconnect.data.repository.EnrollmentRepository
import com.cevdetkilickeser.learnconnect.data.room.CourseDao
import com.cevdetkilickeser.learnconnect.data.room.LessonDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class EnrollmentRepositoryImpl @Inject constructor(
    private val courseDao: CourseDao,
    private val lessonDao: LessonDao
): EnrollmentRepository {
    override suspend fun checkEnrollmentStatus(userId: Int, courseId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            courseDao.checkEnrollmentStatus(userId, courseId)
        }
    }

    override suspend fun enrollToCourse(enrollment: Enrollment): Boolean {
        val enrollmentId = withContext(Dispatchers.IO) {
            courseDao.enrollToCourse(enrollment)
        }
        if (enrollmentId > 0) {
            fillLessonStatusTable(enrollment.userId, enrollment.courseId)
        }
        return enrollmentId > 0
    }

    override suspend fun fillLessonStatusTable(userId: Int, courseId: Int) {
        val lessons = lessonDao.getLessonsByCourseId(courseId)
        for (lesson in lessons) {
            val lessonStatus = LessonStatus(0, userId, lesson)
            courseDao.fillLessonStatusTable(lessonStatus)
        }
    }
}