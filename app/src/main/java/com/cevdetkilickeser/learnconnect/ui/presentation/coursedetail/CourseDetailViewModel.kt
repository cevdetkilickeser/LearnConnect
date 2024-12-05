package com.cevdetkilickeser.learnconnect.ui.presentation.coursedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cevdetkilickeser.learnconnect.data.entity.course.Comment
import com.cevdetkilickeser.learnconnect.data.entity.course.Enrollment
import com.cevdetkilickeser.learnconnect.data.entity.course.Course
import com.cevdetkilickeser.learnconnect.data.repository.CourseRepository
import com.cevdetkilickeser.learnconnect.data.repository.EnrollmentRepository
import com.cevdetkilickeser.learnconnect.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CourseDetailViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val courseRepository: CourseRepository,
    private val enrollmentRepository: EnrollmentRepository
) : ViewModel() {

    private val _course = MutableStateFlow(Course(0, "", "", ""))
    val course: StateFlow<Course> = _course

    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments: StateFlow<List<Comment>> = _comments

    private val _isEnrolled = MutableStateFlow(false)
    val isEnrolled: StateFlow<Boolean> = _isEnrolled

    fun getCourseById(courseId: Int) {
        viewModelScope.launch {
            val course = courseRepository.getCourseById(courseId)
            _course.value = course
            getComments(courseId)
        }
    }

    private suspend fun getComments(courseId: Int){
        val comments = courseRepository.getComments(courseId)
        _comments.value = comments
    }

    fun checkEnrollmentStatus(userId: Int, courseId: Int) {
        viewModelScope.launch {
            val isEnrolled = enrollmentRepository.checkEnrollmentStatus(userId, courseId)
            _isEnrolled.value = isEnrolled
        }

    }

    fun enrollToCourse(userId: Int, courseId: Int) {
        viewModelScope.launch {
            val enrollment = Enrollment(0, userId, courseId)
            _isEnrolled.value = enrollmentRepository.enrollToCourse(enrollment)
        }
    }

    fun unEnroll(userId: Int, courseId: Int) {
        viewModelScope.launch {
            enrollmentRepository.unEnroll(userId, courseId)
            _isEnrolled.value = false
        }

    }

    fun addComment(userId: Int, courseId: Int, comment: String, rate: Int) {
        viewModelScope.launch {
            val user = userRepository.getUserInfo(userId)
            val date = getCurrentDateTime()
            val commentObject = Comment(0, courseId, user, date, comment, rate)
            courseRepository.addComment(commentObject)
            getComments(courseId)
        }
    }

    private fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy, HH:mm", Locale.getDefault())
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }
}