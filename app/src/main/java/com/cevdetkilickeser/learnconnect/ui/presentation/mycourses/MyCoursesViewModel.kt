package com.cevdetkilickeser.learnconnect.ui.presentation.mycourses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cevdetkilickeser.learnconnect.data.entity.course.Course
import com.cevdetkilickeser.learnconnect.data.repository.CourseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MyCoursesViewModel @Inject constructor(
    private val courseRepository: CourseRepository
) : ViewModel() {

    private val _inProgressCourses = MutableStateFlow<List<Course>>(emptyList())
    val inProgressCourses: StateFlow<List<Course>> = _inProgressCourses

    private val _doneCourses = MutableStateFlow<List<Course>>(emptyList())
    val doneCourses: StateFlow<List<Course>> = _doneCourses

    fun getEnrolledCourses(userId: Int) {
        viewModelScope.launch {
            _doneCourses.value = courseRepository.getEnrolledCoursesDone(userId)
            _inProgressCourses.value = courseRepository.getEnrolledCoursesInProgress(userId)
        }
    }
}