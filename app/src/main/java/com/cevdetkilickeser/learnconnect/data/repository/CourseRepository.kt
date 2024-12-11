package com.cevdetkilickeser.learnconnect.data.repository

import com.cevdetkilickeser.learnconnect.data.entity.course.Comment
import com.cevdetkilickeser.learnconnect.data.entity.course.Course

interface CourseRepository {

    var courseList: List<Course>?

    suspend fun getCategories(): List<String>

    suspend fun getCourses(): List<Course>

    fun getFilteredCourses(category: String?): List<Course>?

    fun getSearchResults(query: String?): List<Course>?

    suspend fun getCourseById(id: Int): Course

    suspend fun getComments(courseId: Int): List<Comment>

    suspend fun addComment(comment: Comment)

    suspend fun getEnrolledCoursesDone(userId: Int): List<Course>

    suspend fun getEnrolledCoursesInProgress(userId: Int): List<Course>
}