package com.cevdetkilickeser.learnconnect.domain.repository

import com.cevdetkilickeser.learnconnect.data.entity.course.Comment
import com.cevdetkilickeser.learnconnect.data.entity.course.Course
import com.cevdetkilickeser.learnconnect.data.repository.CourseRepository
import com.cevdetkilickeser.learnconnect.data.room.CourseDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CourseRepositoryImpl @Inject constructor(private val courseDao: CourseDao): CourseRepository {

    override var courseList: List<Course>? = null

    override suspend fun getCategories(): List<String> {
        return withContext(Dispatchers.IO) {
            courseDao.getCategories()
        }
    }

    override suspend fun getCourses(): List<Course> {
        if (courseList == null) {
            courseList = withContext(Dispatchers.IO) {
                courseDao.getCourses()
            }
        }
        return courseList!!
    }

    override fun getFilteredCoursesByCategory(category: String?): List<Course>? {
        return category?.let {
            courseList?.filter { it.category == category } ?: emptyList()
        } ?: courseList
    }

    override fun getSearchResults(query: String?): List<Course>? {
        return if (!query.isNullOrEmpty()) {
            courseList?.filter { it.courseName.contains(query, ignoreCase = true) } ?: emptyList()
        } else {
            courseList
        }
    }

    override suspend fun getCourseById(id: Int): Course {
        return withContext(Dispatchers.IO) {
            courseDao.getCourseById(id)
        }
    }

    override suspend fun getComments(courseId: Int): List<Comment> {
        return withContext(Dispatchers.IO) {
            courseDao.getComments(courseId)
        }
    }

    override suspend fun addComment(comment: Comment) {
        withContext(Dispatchers.IO) {
            courseDao.addComment(comment)
        }
    }

    override suspend fun getEnrolledCoursesDone(userId: Int): List<Course> {
        return withContext(Dispatchers.IO) {
            courseDao.getCoursesDone(userId)
        }
    }

    override suspend fun getEnrolledCoursesInProgress(userId: Int): List<Course> {
        return withContext(Dispatchers.IO) {
            courseDao.getCoursesInProgress(userId)
        }
    }
}