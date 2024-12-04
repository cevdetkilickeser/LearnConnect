package com.cevdetkilickeser.learnconnect.ui.presentation.mycourses

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cevdetkilickeser.learnconnect.data.entity.course.Course
import com.cevdetkilickeser.learnconnect.data.repository.CourseRepository
import com.cevdetkilickeser.learnconnect.data.room.CourseDao
import com.cevdetkilickeser.learnconnect.ui.presentation.home.CourseItem
import com.cevdetkilickeser.learnconnect.ui.theme.LearnConnectTheme

@Composable
fun MyCoursesScreen(
    userId: Int,
    navigateToWatchCourse: (Int) -> Unit,
    viewModel: MyCoursesViewModel = hiltViewModel()
) {

    val inProgressCourses by viewModel.inProgressCourses.collectAsState()
    val doneCourses by viewModel.doneCourses.collectAsState()
    val selectedTabIndex = remember { mutableIntStateOf(CourseStatus.IN_PROGRESS.ordinal) }

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
                        text = { Text(text = "InProgress") }
                    )
                    Tab(
                        selected = selectedTabIndex.intValue == CourseStatus.DONE.ordinal,
                        onClick = { selectedTabIndex.intValue = CourseStatus.DONE.ordinal },
                        text = { Text(text = "Done") }
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
                navigateToCourseDetail = { navigateToWatchCourse(course.id) }
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
                navigateToCourseDetail = { navigateToWatchCourse(course.id) }
            )
        }
    }
}

enum class CourseStatus {
    IN_PROGRESS, DONE
}

@Preview(showBackground = true)
@Composable
fun MyCoursesPreview() {
    LearnConnectTheme(false) {
        val courseDao = object : CourseDao {
            override suspend fun getCategories(): List<String> {
                TODO("Not yet implemented")
            }

            override suspend fun getCourses(): List<Course> {
                TODO("Not yet implemented")
            }

            override suspend fun getCourseById(id: Int): Course {
                TODO("Not yet implemented")
            }

            override suspend fun getCoursesDone(userId: Int): List<Course> {
                TODO("Not yet implemented")
            }

            override suspend fun getCoursesInProgress(userId: Int): List<Course> {
                TODO("Not yet implemented")
            }

        }
        MyCoursesScreen(
            1, {},
            MyCoursesViewModel(
                CourseRepository(courseDao)
            )
        )
    }
}