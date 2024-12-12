package com.cevdetkilickeser.learnconnect.ui.presentation.home

import com.cevdetkilickeser.learnconnect.data.entity.course.Course

object HomeContract {
    data class HomeState(
        val categoryList: List<String> = emptyList(),
        val courseList: List<Course> = emptyList(),
        val query: String = "",
        val selectedCategory: String? = null
    )

    sealed interface HomeAction {
        data class QueryChanged(val query: String) : HomeAction
        data class CategorySelected(val selectedCategory: String?) : HomeAction
        data class CourseClicked(val courseId: String) : HomeAction
    }

    sealed class HomeEffect {
        data class NavigateToCourseDetail(val courseId: String) : HomeEffect()
    }
}