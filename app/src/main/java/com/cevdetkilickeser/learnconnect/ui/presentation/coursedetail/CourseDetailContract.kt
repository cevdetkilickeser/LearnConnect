package com.cevdetkilickeser.learnconnect.ui.presentation.coursedetail

import com.cevdetkilickeser.learnconnect.data.entity.course.Comment
import com.cevdetkilickeser.learnconnect.data.entity.course.Course

object CourseDetailContract {
    data class UiState(
        val userId: Int? = null,
        val course: Course? = null,
        val commentList: List<Comment> = emptyList(),
        val isEnrolled: Boolean = false,
        val showCommentsSheet: Boolean = false
    )

    sealed interface UiAction {
        data class GetCourseById(val userId: Int, val courseId: Int) : UiAction
        data class EnrollClicked(val courseId: Int) : UiAction
        data class PlayClicked(val courseId: Int) : UiAction
        data object CommentsClicked : UiAction
        data object CommentsDismissed : UiAction
        data class SendClicked(val courseId: Int, val commentText: String, val rating: Int) : UiAction
    }

    sealed class UiEffect {
        data class NavigateToWatchCourse(val courseId: String) : UiEffect()
    }
}