package com.cevdetkilickeser.learnconnect.ui.presentation.mycourses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cevdetkilickeser.learnconnect.domain.repository.CourseRepositoryImpl
import com.cevdetkilickeser.learnconnect.ui.presentation.mycourses.MyCoursesContract.UiAction
import com.cevdetkilickeser.learnconnect.ui.presentation.mycourses.MyCoursesContract.UiEffect
import com.cevdetkilickeser.learnconnect.ui.presentation.mycourses.MyCoursesContract.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MyCoursesViewModel @Inject constructor(
    private val courseRepository: CourseRepositoryImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    private val _uiEffect by lazy { Channel<UiEffect>() }
    val uiEffect: Flow<UiEffect> by lazy { _uiEffect.receiveAsFlow() }

    fun onAction(uiAction: UiAction) {
        when (uiAction) {
            is UiAction.CourseClicked -> viewModelScope.launch {
                emitUiEffect(
                    UiEffect.NavigateToWatchCourse(
                        uiAction.courseId
                    )
                )
            }

            is UiAction.TabSelected -> updateUiState { copy(selectedTabIndex = uiAction.selectedTabIndex) }
        }
    }

    fun getEnrolledCourses(userId: Int) {
        viewModelScope.launch {
            val inProgressCourses = courseRepository.getEnrolledCoursesInProgress(userId)
            val doneCourses = courseRepository.getEnrolledCoursesDone(userId)
            updateUiState { copy(inProgressCourses = inProgressCourses, doneCourses = doneCourses) }
        }
    }

    private fun updateUiState(block: UiState.() -> UiState) {
        _uiState.update(block)
    }

    private suspend fun emitUiEffect(uiEffect: UiEffect) {
        _uiEffect.send(uiEffect)
    }
}