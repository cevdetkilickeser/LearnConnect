package com.cevdetkilickeser.learnconnect.ui.presentation.watchcourse

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.cevdetkilickeser.learnconnect.data.entity.course.LessonStatus
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.R)
@Composable
fun WatchCourseScreen(
    userId: Int,
    courseId: Int,
    viewModel: WatchCourseViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val exoPlayer = remember { ExoPlayer.Builder(context).build() }
    val isInternetAvailable = ObserveConnectivityStatus(context = context)
    val isFullScreen by viewModel.isFullScreen.collectAsState()
    val lessons by viewModel.lessons.collectAsState()
    val isBuffering by viewModel.isBuffering.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()
    val currentPlayingLessonNumber by viewModel.currentPlayingLessonNumber.collectAsState()
    var isPlaylistAdded by remember { mutableStateOf(false) }

    val storagePermission = rememberPermissionState(
        if (Build.VERSION.SDK_INT >= 33) Manifest.permission.READ_MEDIA_IMAGES
        else Manifest.permission.READ_EXTERNAL_STORAGE
    )

    LaunchedEffect(courseId) {
        viewModel.getLessonsByUserIdAndCourseId(userId, courseId)
    }

    LaunchedEffect(lessons) {
        if (lessons.isNotEmpty() && !isPlaylistAdded) {
            viewModel.addPlaylistToExoPlayer(exoPlayer, userId, courseId)
            isPlaylistAdded = true
        }
    }

    LaunchedEffect(isFullScreen) {
        if (isFullScreen) {
            setScreenOrientation(context, ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE)
        } else {
            setScreenOrientation(context, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        }
    }

    DisposableEffect(Unit) {
        viewModel.startPlayerListener(exoPlayer, userId, courseId)
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> exoPlayer.pause()
                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            exoPlayer.release()
        }
    }

    Column {
        ShowInternetConnection(isInternetAvailable)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(if (!isFullScreen) 0.4f else 1f)
        ) {
            AndroidView(
                factory = { context ->
                    PlayerView(context).apply {
                        player = exoPlayer
                        useController = true
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
            IconButton(
                onClick = { viewModel.changeFullScreenStatus(isPlaying) },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .background(
                        color = Color.Black.copy(alpha = 0.5f),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Fullscreen,
                    contentDescription = null,
                    tint = Color.White
                )
            }
            if (isBuffering) {
                CircularProgressIndicator(
                    color = Color.White, modifier = Modifier
                        .size(64.dp)
                        .align(Alignment.Center)
                )
            }
        }
        if (!isFullScreen) {
            LessonList(
                lessons = lessons,
                context = context,
                storagePermission = storagePermission,
                viewModel = viewModel,
                exoPlayer = exoPlayer,
                userId = userId,
                courseId = courseId,
                currentPlayingLessonNumber = currentPlayingLessonNumber ?: 1,
                isInternetAvailable = isInternetAvailable
            )
        }
    }
}

fun setScreenOrientation(context: Context, orientation: Int) {
    val activity = context as? Activity
    activity?.requestedOrientation = orientation
}

@Composable
fun ShowInternetConnection(isInternetAvailable: Boolean) {
    if (!isInternetAvailable) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Red)
        ) {
            Text(text = "No Internet Connection", color = Color.White)
        }
    }
}

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

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LessonItem(
    context: Context,
    storagePermission: PermissionState,
    lesson: LessonStatus,
    isDownloaded: Boolean,
    isPlayingNow: Boolean,
    onClickLessonItem: () -> Unit,
    downloadVideo: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClickLessonItem() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = lesson.lesson.lessonNumber.toString(),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = lesson.lesson.lessonTitle,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(2f),
            color = if (isPlayingNow) Color.Green else Color.Unspecified
        )
        if (isDownloaded) {
            IconButton(
                onClick = {
                    Toast.makeText(context, "Already Downloaded", Toast.LENGTH_SHORT).show()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = null,
                    tint = Color.Green
                )
            }
        } else {
            IconButton(
                onClick = {
                    if (storagePermission.status.isGranted) {
                        downloadVideo()
                    } else {
                        storagePermission.launchPermissionRequest()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Download,
                    contentDescription = null
                )
            }
        }
    }
}