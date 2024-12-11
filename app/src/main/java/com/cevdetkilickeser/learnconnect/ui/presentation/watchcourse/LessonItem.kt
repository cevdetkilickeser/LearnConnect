package com.cevdetkilickeser.learnconnect.ui.presentation.watchcourse

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cevdetkilickeser.learnconnect.data.entity.course.LessonStatus
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted

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