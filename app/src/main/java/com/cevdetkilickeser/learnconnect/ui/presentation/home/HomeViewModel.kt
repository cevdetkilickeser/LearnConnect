package com.cevdetkilickeser.learnconnect.ui.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cevdetkilickeser.learnconnect.domain.repository.CourseRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val courseRepository: CourseRepositoryImpl) :
    ViewModel() {

    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()

    init {
        getCategories()
        getFilteredCoursesByCategory(null)
    }

    private fun getCategories() {
        viewModelScope.launch {
            val categoryList = courseRepository.getCategories()
            _homeState.update {
                it.copy(categoryList = categoryList)
            }
        }
    }

    fun getFilteredCoursesByCategory(category: String?) {
        viewModelScope.launch {
            courseRepository.getCourses()
            val filteredCourseList = courseRepository.getFilteredCoursesByCategory(category)
            _homeState.update {
                it.copy(courseList = filteredCourseList!!, query = "", selectedCategory = category)
            }
        }
    }

    fun getSearchResults(query: String) {
        viewModelScope.launch {
            courseRepository.getCourses()
            val searchResults = courseRepository.getSearchResults(query)
            _homeState.update {
                it.copy(query = query, courseList = searchResults!!, selectedCategory = null)
            }
        }
    }
}