package com.cevdetkilickeser.learnconnect.data.repository

import com.cevdetkilickeser.learnconnect.data.entity.course.Comment
import com.cevdetkilickeser.learnconnect.data.entity.course.Course
import com.cevdetkilickeser.learnconnect.data.room.CourseDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CourseRepository @Inject constructor(private val courseDao: CourseDao) {

    private var courseList: List<Course>? = null

    suspend fun getCategories(): List<String> {
        return withContext(Dispatchers.IO) {
            courseDao.getCategories()
        }
    }

    suspend fun getCourses(): List<Course> {
        if (courseList == null) {
            courseList = withContext(Dispatchers.IO) {
                courseDao.getCourses()
            }
        }
        return courseList!!
    }

    fun getFilteredCourses(category: String?): List<Course>? {
        return category?.let {
            courseList?.filter { it.category == category } ?: emptyList()
        } ?: courseList
    }

    fun getSearchResults(query: String?): List<Course>? {
        return if (!query.isNullOrEmpty()) {
            courseList?.filter { it.courseName.contains(query, ignoreCase = true) } ?: emptyList()
        } else {
            courseList
        }
    }

    suspend fun getCourseById(id: Int): Course {
        return withContext(Dispatchers.IO) {
            courseDao.getCourseById(id)
        }
    }

    suspend fun getComments(courseId: Int): List<Comment> {
        return withContext(Dispatchers.IO) {
            courseDao.getComments(courseId)
        }
    }

    suspend fun addComment(comment: Comment) {
        withContext(Dispatchers.IO) {
            courseDao.addComment(comment)
        }
    }

    suspend fun getEnrolledCoursesDone(userId: Int): List<Course> {
        return withContext(Dispatchers.IO) {
            courseDao.getCoursesDone(userId)
        }
    }

    suspend fun getEnrolledCoursesInProgress(userId: Int): List<Course> {
        return withContext(Dispatchers.IO) {
            courseDao.getCoursesInProgress(userId)
        }
    }
}