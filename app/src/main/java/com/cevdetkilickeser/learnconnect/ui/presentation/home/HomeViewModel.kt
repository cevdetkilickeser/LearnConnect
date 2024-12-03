package com.cevdetkilickeser.learnconnect.ui.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cevdetkilickeser.learnconnect.data.entity.course.Course
import com.cevdetkilickeser.learnconnect.data.repository.CourseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val courseRepository: CourseRepository) :
    ViewModel() {

    private val _categoryList = MutableStateFlow<List<String>>(emptyList())
    val categoryList: StateFlow<List<String>> = _categoryList

    private val _courseList = MutableStateFlow<List<Course>>(emptyList())
    val courseList: StateFlow<List<Course>> = _courseList

    init {
        getCategories()
        getFilteredCourses(null)
    }

    private fun getCategories() {
        viewModelScope.launch {
            val categoryList = withContext(Dispatchers.IO) {
                courseRepository.getCategories()
            }
            _categoryList.value = categoryList
        }
    }

    fun getFilteredCourses(category: String?) {
        viewModelScope.launch {
            courseRepository.getCourses()
            val filteredCourseList = courseRepository.getFilteredCourses(category)
            _courseList.value = filteredCourseList!!
        }
    }

    fun getSearchResults(query:String?){
        viewModelScope.launch {
            courseRepository.getCourses()
            val searchResults = courseRepository.getSearchResults(query)
            _courseList.value = searchResults!!
        }
    }
}