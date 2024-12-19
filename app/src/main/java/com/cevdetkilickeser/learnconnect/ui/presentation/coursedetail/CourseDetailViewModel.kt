package com.cevdetkilickeser.learnconnect.ui.presentation.coursedetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cevdetkilickeser.learnconnect.data.entity.course.Comment
import com.cevdetkilickeser.learnconnect.data.entity.course.Enrollment
import com.cevdetkilickeser.learnconnect.domain.repository.CourseRepositoryImpl
import com.cevdetkilickeser.learnconnect.domain.repository.EnrollmentRepositoryImpl
import com.cevdetkilickeser.learnconnect.domain.repository.UserRepositoryImpl
import com.cevdetkilickeser.learnconnect.ui.presentation.coursedetail.CourseDetailContract.UiAction
import com.cevdetkilickeser.learnconnect.ui.presentation.coursedetail.CourseDetailContract.UiEffect
import com.cevdetkilickeser.learnconnect.ui.presentation.coursedetail.CourseDetailContract.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CourseDetailViewModel @Inject constructor(
    private val userRepository: UserRepositoryImpl,
    private val courseRepository: CourseRepositoryImpl,
    private val enrollmentRepository: EnrollmentRepositoryImpl,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _uiEffect by lazy { Channel<UiEffect>() }
    val uiEffect: Flow<UiEffect> by lazy { _uiEffect.receiveAsFlow() }

    init {
        val args = savedStateHandle.get<Int>("userId")
        args?.let {
            updateUiState { copy(userId = it) }
            getCourseById(it)
        }
    }

    fun onAction(uiAction: UiAction) {
        val userId = uiState.value.userId ?: 0
        when (uiAction) {
            is UiAction.EnrollClicked -> enrollToCourse(userId, uiAction.courseId)
            is UiAction.PlayClicked -> viewModelScope.launch {
                emitUiEffect(
                    UiEffect.NavigateToWatchCourse(
                        uiAction.courseId.toString()
                    )
                )
            }

            UiAction.CommentsClicked -> updateUiState { copy(showCommentsSheet = true) }
            UiAction.CommentsDismissed -> updateUiState { copy(showCommentsSheet = false) }
            is UiAction.SendClicked -> addComment(
                userId,
                uiAction.courseId,
                uiAction.commentText,
                uiAction.rating
            )
        }
    }

    private fun getCourseById(courseId: Int) {
        viewModelScope.launch {
            val course = courseRepository.getCourseById(courseId)
            updateUiState { copy(course = course) }
            getComments(courseId)
        }
    }

    private suspend fun getComments(courseId: Int) {
        val commentList = courseRepository.getComments(courseId)
        updateUiState { copy(commentList = commentList) }
    }

    private fun enrollToCourse(userId: Int, courseId: Int) {
        viewModelScope.launch {
            val enrollment = Enrollment(0, userId, courseId)
            val result = enrollmentRepository.enrollToCourse(enrollment)
            updateUiState { copy(isEnrolled = result) }
        }
    }

    private fun addComment(userId: Int, courseId: Int, commentText: String, rating: Int) {
        viewModelScope.launch {
            val user = userRepository.getUserInfo(userId)
            val date = getCurrentDateTime()
            val commentObject = Comment(0, courseId, user, date, commentText, rating)
            courseRepository.addComment(commentObject)
            getComments(courseId)
        }
    }

    private fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy, HH:mm", Locale.getDefault())
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }

    private fun updateUiState(block: UiState.() -> UiState) {
        _uiState.update(block)
    }

    private suspend fun emitUiEffect(uiEffect: UiEffect) {
        _uiEffect.send(uiEffect)
    }
}