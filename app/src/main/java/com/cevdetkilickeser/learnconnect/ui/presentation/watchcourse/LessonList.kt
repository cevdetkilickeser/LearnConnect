package com.cevdetkilickeser.learnconnect.ui.presentation.watchcourse

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.media3.exoplayer.ExoPlayer
import com.cevdetkilickeser.learnconnect.data.entity.course.LessonStatus
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LessonList(
    lessons: List<LessonStatus>,
    context: Context,
    storagePermission: PermissionState,
    viewModel: WatchCourseViewModel,
    exoPlayer: ExoPlayer,
    userId: Int,
    courseId: Int,
    currentPlayingLessonNumber: Int,
    isInternetAvailable: Boolean
) {
    LazyColumn {
        items(lessons) { lesson ->
            LessonItem(
                context = context,
                storagePermission = storagePermission,
                lesson = lesson,
                isDownloaded = lesson.isDownloaded,
                isPlayingNow = currentPlayingLessonNumber == lesson.lesson.lessonNumber,
                onClickLessonItem = {
                    viewModel.onClickLessonItem(
                        exoPlayer,
                        userId,
                        courseId,
                        lesson.lesson.lessonNumber
                    )
                },
                downloadVideo = {
                    if (isInternetAvailable) {
                        viewModel.downloadLesson(
                            userId,
                            courseId,
                            lesson.lesson.lessonNumber,
                            lesson.lesson.lessonUrl,
                            lesson.lesson.lessonTitle
                        )
                    } else {
                        Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            )
        }
    }
}