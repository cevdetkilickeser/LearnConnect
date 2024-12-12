package com.cevdetkilickeser.learnconnect.ui.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cevdetkilickeser.learnconnect.domain.repository.CourseRepositoryImpl
import com.cevdetkilickeser.learnconnect.ui.presentation.home.HomeContract.HomeAction
import com.cevdetkilickeser.learnconnect.ui.presentation.home.HomeContract.HomeEffect
import com.cevdetkilickeser.learnconnect.ui.presentation.home.HomeContract.HomeState
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

    private val _homeState = MutableStateFlow(HomeState())
    val homeState = _homeState.asStateFlow()

    private val _homeEffect by lazy { Channel<HomeEffect>() }
    val homeEffect: Flow<HomeEffect> by lazy { _homeEffect.receiveAsFlow() }

    init {
        getCategories()
        getCourses()
    }

    private fun getCategories() {
        viewModelScope.launch {
            val categoryList = courseRepository.getCategories()
            _homeState.update {
                it.copy(categoryList = categoryList)
            }
        }
    }

    private fun getCourses() {
        viewModelScope.launch {
            val courseList= courseRepository.getCourses()
            _homeState.update {
                it.copy(courseList = courseList)
            }
        }
    }

    fun onAction(homeAction: HomeAction) {
        when (homeAction) {
            is HomeAction.QueryChanged -> {
                updateHomeState {
                    val searchResults = courseRepository.getSearchResults(homeAction.query)
                    copy(query = homeAction.query, courseList = searchResults!!, selectedCategory = null)
                }
            }
            is HomeAction.CategorySelected -> {
                updateHomeState {
                    val filteredCourseList =
                        courseRepository.getFilteredCoursesByCategory(homeAction.selectedCategory)
                    copy(
                        courseList = filteredCourseList!!,
                        query = "",
                        selectedCategory = homeAction.selectedCategory
                    )
                }
            }
            is HomeAction.CourseClicked -> {
                viewModelScope.launch {
                    emitHomeEffect(HomeEffect.NavigateToCourseDetail(homeAction.courseId))
                }
            }
        }
    }

    private fun updateHomeState(block: HomeState.() -> HomeState) {
        _homeState.update(block)
    }

    private suspend fun emitHomeEffect(homeEffect: HomeEffect) {
        _homeEffect.send(homeEffect)
    }
}