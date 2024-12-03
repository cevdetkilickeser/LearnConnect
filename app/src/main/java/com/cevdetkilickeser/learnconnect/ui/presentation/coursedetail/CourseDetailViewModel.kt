package com.cevdetkilickeser.learnconnect.ui.presentation.coursedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cevdetkilickeser.learnconnect.data.entity.course.Enrollment
import com.cevdetkilickeser.learnconnect.data.entity.course.Course
import com.cevdetkilickeser.learnconnect.data.repository.CourseRepository
import com.cevdetkilickeser.learnconnect.data.repository.EnrollmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseDetailViewModel @Inject constructor(
    private val courseRepository: CourseRepository,
    private val enrollmentRepository: EnrollmentRepository
) :
    ViewModel() {

    private val _course = MutableStateFlow(Course(0, "", "", ""))
    val course: StateFlow<Course> = _course

    private val _isEnrolled = MutableStateFlow(false)
    val isEnrolled: StateFlow<Boolean> = _isEnrolled

    fun getCourseById(id: Int) {
        viewModelScope.launch {
            val course = courseRepository.getCourseById(id)
            _course.value = course
        }
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
}