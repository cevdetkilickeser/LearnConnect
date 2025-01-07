package com.cevdetkilickeser.learnconnect.ui.presentation.coursedetail

import android.content.pm.ActivityInfo
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cevdetkilickeser.learnconnect.R
import com.cevdetkilickeser.learnconnect.collectWithLifecycle
import com.cevdetkilickeser.learnconnect.setScreenOrientation
import com.cevdetkilickeser.learnconnect.ui.presentation.coursedetail.CourseDetailContract.UiAction
import com.cevdetkilickeser.learnconnect.ui.presentation.coursedetail.CourseDetailContract.UiEffect
import com.cevdetkilickeser.learnconnect.ui.presentation.coursedetail.CourseDetailContract.UiState
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailScreen(
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    uiAction: (UiAction) -> Unit,
    userId: Int,
    courseId: Int,
    navigateToWatchCourse: (String) -> Unit
) {

    val context = LocalContext.current
    uiEffect.collectWithLifecycle { effect ->
        when (effect) {
            is UiEffect.NavigateToWatchCourse -> navigateToWatchCourse(effect.courseId)
        }
    }

    LaunchedEffect(Unit) {
        setScreenOrientation(context, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        uiAction(UiAction.GetCourseById(userId, courseId))
    }

    if (uiState.showCommentsSheet) {
        ModalBottomSheet(
            onDismissRequest = { uiAction(UiAction.CommentsDismissed) },
            sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = true
            )
        ) {
            CommentSection(
                commentList = uiState.commentList,
                addComment = { commentText, rating ->
                    uiAction(UiAction.SendClicked(courseId, commentText, rating))
                }
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary),

        ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f)
        ) {
            Image(
                modifier = Modifier
                    .fillMaxHeight(0.7f),
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Text(
                modifier = Modifier
                    .padding(16.dp),
                text = uiState.course?.courseName ?: "",
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontWeight = FontWeight.Bold
            )

        }
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.6f)
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(top = 32.dp, start = 32.dp, end = 32.dp)
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight(0.4f)
                ) {
                    Text(
                        text = stringResource(id = R.string.description),
                    )
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = uiState.course?.courseName ?: ""
                        )
                    }
                }

                Column {
                    Text(
                        text = stringResource(id = R.string.author)
                    )

                    AuthorCart(
                        name = uiState.course?.author ?: "",
                        courseCount = "14 courses",
                        studentCount = "100+ student"
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Button(
                            onClick = {
                                if (uiState.isEnrolled) {
                                    uiAction(UiAction.PlayClicked(courseId))
                                } else {
                                    uiAction(UiAction.EnrollClicked(courseId))
                                }
                            },
                            modifier = Modifier
                                .weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            if (uiState.isEnrolled) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = null
                                )
                            } else {
                                Text(text = stringResource(id = R.string.enroll))
                            }
                        }

                        Button(
                            onClick = {
                                uiAction(UiAction.CommentsClicked)
                            },
                            modifier = Modifier
                                .weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(text = stringResource(id = R.string.comments))
                        }
                    }
                }
            }
        }
    }
}