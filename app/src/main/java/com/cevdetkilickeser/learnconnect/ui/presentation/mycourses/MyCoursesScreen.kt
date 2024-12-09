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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cevdetkilickeser.learnconnect.R
import com.cevdetkilickeser.learnconnect.data.entity.course.Course
import com.cevdetkilickeser.learnconnect.ui.presentation.home.CourseItem
import com.cevdetkilickeser.learnconnect.ui.presentation.watchcourse.setScreenOrientation

@Composable
fun MyCoursesScreen(
    userId: Int,
    navigateToWatchCourse: (Int) -> Unit,
    viewModel: MyCoursesViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val inProgressCourses by viewModel.inProgressCourses.collectAsState()
    val doneCourses by viewModel.doneCourses.collectAsState()
    val selectedTabIndex = remember { mutableIntStateOf(CourseStatus.IN_PROGRESS.ordinal) }


    LaunchedEffect(Unit) {
        setScreenOrientation(context, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    }

    LaunchedEffect(userId) {
        viewModel.getEnrolledCourses(userId)
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
                    selectedTabIndex = selectedTabIndex.intValue,
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                    indicator = { tabPositions ->
                        SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex.intValue]),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                ) {
                    Tab(
                        selected = selectedTabIndex.intValue == CourseStatus.IN_PROGRESS.ordinal,
                        onClick = { selectedTabIndex.intValue = CourseStatus.IN_PROGRESS.ordinal },
                        text = { Text(text = stringResource(id = R.string.inProgress)) }
                    )
                    Tab(
                        selected = selectedTabIndex.intValue == CourseStatus.DONE.ordinal,
                        onClick = { selectedTabIndex.intValue = CourseStatus.DONE.ordinal },
                        text = { Text(text = stringResource(id = R.string.done)) }
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
                when (selectedTabIndex.intValue) {
                    CourseStatus.IN_PROGRESS.ordinal -> InProgressCourses(
                        courseList = inProgressCourses,
                        navigateToWatchCourse = { courseId ->
                            navigateToWatchCourse(courseId)
                        }
                    )

                    CourseStatus.DONE.ordinal -> DoneCourses(
                        courseList = doneCourses,
                        navigateToWatchCourse = { courseId ->
                            navigateToWatchCourse(courseId)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun InProgressCourses(courseList: List<Course>, navigateToWatchCourse: (Int) -> Unit) {
    LazyColumn {
        items(courseList) { course ->
            CourseItem(
                course = course,
                navigateToCourseDetail = { navigateToWatchCourse(course.courseId) }
            )
        }
    }
}

@Composable
fun DoneCourses(courseList: List<Course>, navigateToWatchCourse: (Int) -> Unit) {
    LazyColumn {
        items(courseList) { course ->
            CourseItem(
                course = course,
                navigateToCourseDetail = { navigateToWatchCourse(course.courseId) }
            )
        }
    }
}

enum class CourseStatus {
    IN_PROGRESS, DONE
}