package com.cevdetkilickeser.learnconnect.ui.presentation.mycourses

import android.content.pm.ActivityInfo
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cevdetkilickeser.learnconnect.R
import com.cevdetkilickeser.learnconnect.collectWithLifecycle
import com.cevdetkilickeser.learnconnect.setScreenOrientation
import com.cevdetkilickeser.learnconnect.ui.presentation.home.CourseItem
import com.cevdetkilickeser.learnconnect.ui.presentation.mycourses.MyCoursesContract.UiAction
import com.cevdetkilickeser.learnconnect.ui.presentation.mycourses.MyCoursesContract.UiEffect
import com.cevdetkilickeser.learnconnect.ui.presentation.mycourses.MyCoursesContract.UiState
import kotlinx.coroutines.flow.Flow

@Composable
fun MyCoursesScreen(
    uiState: UiState,
    uiEffect: Flow<UiEffect>,
    uiAction: (UiAction) -> Unit,
    userId: Int,
    navigateToWatchCourse: (Int) -> Unit
) {

    val context = LocalContext.current
    uiEffect.collectWithLifecycle { effect ->
        when (effect) {
            is UiEffect.NavigateToWatchCourse -> navigateToWatchCourse(effect.courseId)
        }
    }

    LaunchedEffect(Unit) {
        setScreenOrientation(context, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        uiAction(UiAction.GetEnrolledCourses(userId))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp)
            ) {
                TabRow(
                    selectedTabIndex = uiState.selectedTabIndex,
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                    indicator = { tabPositions ->
                        SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[uiState.selectedTabIndex]),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                ) {
                    Tab(
                        selected = uiState.selectedTabIndex == CourseStatus.IN_PROGRESS.ordinal,
                        onClick = { uiAction(UiAction.TabSelected(CourseStatus.IN_PROGRESS.ordinal)) },
                        text = { Text(text = stringResource(id = R.string.inProgress)) }
                    )
                    Tab(
                        selected = uiState.selectedTabIndex == CourseStatus.DONE.ordinal,
                        onClick = { uiAction(UiAction.TabSelected(CourseStatus.DONE.ordinal)) },
                        text = { Text(text = stringResource(id = R.string.done)) }
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
                when (uiState.selectedTabIndex) {
                    CourseStatus.IN_PROGRESS.ordinal -> LazyColumn {
                        items(uiState.inProgressCourses) { course ->
                            CourseItem(
                                course = course,
                                onClickCourse = { uiAction(UiAction.CourseClicked(course.courseId)) }
                            )
                        }
                    }

                    CourseStatus.DONE.ordinal -> LazyColumn {
                        items(uiState.doneCourses) { course ->
                            CourseItem(
                                course = course,
                                onClickCourse = { uiAction(UiAction.CourseClicked(course.courseId)) }
                            )
                        }
                    }
                }
            }
        }
    }
}