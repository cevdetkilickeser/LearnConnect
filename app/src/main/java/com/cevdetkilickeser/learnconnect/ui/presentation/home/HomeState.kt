package com.cevdetkilickeser.learnconnect.ui.presentation.home

import com.cevdetkilickeser.learnconnect.data.entity.course.Course

data class HomeState(
    val categoryList: List<String> = emptyList(),
    val courseList: List<Course> = emptyList(),
    val query: String = "",
    val selectedCategory: String? = null
) {
}