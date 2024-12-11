package com.cevdetkilickeser.learnconnect.data.repository

import com.cevdetkilickeser.learnconnect.data.entity.course.Enrollment

interface EnrollmentRepository {

    suspend fun checkEnrollmentStatus(userId: Int, courseId: Int): Boolean

    suspend fun enrollToCourse(enrollment: Enrollment): Boolean

    suspend fun fillLessonStatusTable(userId: Int, courseId: Int)
}