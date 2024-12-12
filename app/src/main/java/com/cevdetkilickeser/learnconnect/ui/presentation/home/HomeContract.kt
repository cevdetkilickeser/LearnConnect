package com.cevdetkilickeser.learnconnect.ui.presentation.home

import com.cevdetkilickeser.learnconnect.data.entity.course.Course

object HomeContract {
    data class UiState(
        val categoryList: List<String> = emptyList(),
        val courseList: List<Course> = emptyList(),
        val query: String = "",
        val selectedCategory: String? = null
    )

    sealed interface UiAction {
        data class QueryChanged(val query: String) : UiAction
        data class CategorySelected(val selectedCategory: String?) : UiAction
        data class CourseClicked(val courseId: String) : UiAction
    }

    sealed class UiEffect {
        data class NavigateToCourseDetail(val courseId: String) : UiEffect()
    }
}