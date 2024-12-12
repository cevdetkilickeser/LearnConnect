package com.cevdetkilickeser.learnconnect.ui.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cevdetkilickeser.learnconnect.domain.repository.CourseRepositoryImpl
import com.cevdetkilickeser.learnconnect.ui.presentation.home.HomeContract.UiAction
import com.cevdetkilickeser.learnconnect.ui.presentation.home.HomeContract.UiEffect
import com.cevdetkilickeser.learnconnect.ui.presentation.home.HomeContract.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val courseRepository: CourseRepositoryImpl) :
    ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEffect by lazy { Channel<UiEffect>() }
    val uiEffect: Flow<UiEffect> by lazy { _uiEffect.receiveAsFlow() }

    init {
        getCategories()
        getCourses()
    }

    private fun getCategories() {
        viewModelScope.launch {
            val categoryList = courseRepository.getCategories()
            _uiState.update {
                it.copy(categoryList = categoryList)
            }
        }
    }

    private fun getCourses() {
        viewModelScope.launch {
            val courseList= courseRepository.getCourses()
            _uiState.update {
                it.copy(courseList = courseList)
            }
        }
    }

    fun onAction(uiAction: UiAction) {
        when (uiAction) {
            is UiAction.QueryChanged -> {
                updateHomeState {
                    val searchResults = courseRepository.getSearchResults(uiAction.query)
                    copy(query = uiAction.query, courseList = searchResults!!, selectedCategory = null)
                }
            }
            is UiAction.CategorySelected -> {
                updateHomeState {
                    val filteredCourseList =
                        courseRepository.getFilteredCoursesByCategory(uiAction.selectedCategory)
                    copy(
                        courseList = filteredCourseList!!,
                        query = "",
                        selectedCategory = uiAction.selectedCategory
                    )
                }
            }
            is UiAction.CourseClicked -> {
                viewModelScope.launch {
                    emitHomeEffect(UiEffect.NavigateToCourseDetail(uiAction.courseId))
                }
            }
        }
    }

    private fun updateHomeState(block: UiState.() -> UiState) {
        _uiState.update(block)
    }

    private suspend fun emitHomeEffect(uiEffect: UiEffect) {
        _uiEffect.send(uiEffect)
    }
}