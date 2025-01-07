package com.cevdetkilickeser.learnconnect.ui.presentation.mycourses

import com.cevdetkilickeser.learnconnect.data.entity.course.Course

object MyCoursesContract {
    data class UiState(
        val inProgressCourses: List<Course> = emptyList(),
        val doneCourses: List<Course> = emptyList(),
        val selectedTabIndex: Int = CourseStatus.IN_PROGRESS.ordinal
    )

    sealed interface UiAction {
        data class GetEnrolledCourses(val userId: Int) : UiAction
        data class TabSelected(val selectedTabIndex: Int) : UiAction
        data class CourseClicked(val courseId: Int) : UiAction
    }

    sealed class UiEffect {
        data class NavigateToWatchCourse(val courseId: Int) : UiEffect()
    }
}