package com.cevdetkilickeser.learnconnect.ui.presentation.coursedetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cevdetkilickeser.learnconnect.R
import com.cevdetkilickeser.learnconnect.data.entity.User
import com.cevdetkilickeser.learnconnect.data.entity.course.Comment
import com.cevdetkilickeser.learnconnect.data.entity.course.Course
import com.cevdetkilickeser.learnconnect.data.entity.course.Enrollment
import com.cevdetkilickeser.learnconnect.data.entity.course.Lesson
import com.cevdetkilickeser.learnconnect.data.entity.course.LessonStatus
import com.cevdetkilickeser.learnconnect.data.repository.CourseRepository
import com.cevdetkilickeser.learnconnect.data.repository.EnrollmentRepository
import com.cevdetkilickeser.learnconnect.data.repository.UserRepository
import com.cevdetkilickeser.learnconnect.data.room.CourseDao
import com.cevdetkilickeser.learnconnect.data.room.LessonDao
import com.cevdetkilickeser.learnconnect.data.room.UserDao
import com.cevdetkilickeser.learnconnect.ui.theme.LearnConnectTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailScreen(
    courseId: Int,
    userId: Int,
    navigateToWatchCourse: (String) -> Unit,
    viewModel: CourseDetailViewModel = hiltViewModel()
) {

    val course by viewModel.course.collectAsState()
    val commentList by viewModel.comments.collectAsState()
    val isEnrolled by viewModel.isEnrolled.collectAsState()
    var showCommentsSheet by remember { mutableStateOf(false) }

    LaunchedEffect(isEnrolled) {
        viewModel.getCourseById(courseId)
        viewModel.checkEnrollmentStatus(userId, courseId)
    }

    if (showCommentsSheet) {
        ModalBottomSheet(
            onDismissRequest = { showCommentsSheet = false },
            sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = true
            )
        ) {
            CommentSection(
                commentList = commentList,
                addComment = { commentText, rating ->
                    viewModel.addComment(userId, courseId, commentText, rating)
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
                text = course.courseName,
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
                            text = course.courseName
                        )
                    }
                }

                Column {
                    Text(
                        text = stringResource(id = R.string.author)
                    )

                    AuthorCart(
                        name = course.author,
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
                                if (isEnrolled) {
                                    navigateToWatchCourse(courseId.toString())
                                } else {
                                    viewModel.enrollToCourse(userId, courseId)
                                }
                            },
                            modifier = Modifier
                                .weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            if (isEnrolled) {
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
                                showCommentsSheet = true
                                viewModel.unEnroll(userId, courseId)
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

@Composable
fun AuthorCart(name: String, courseCount: String, studentCount: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.background(color = MaterialTheme.colorScheme.surface)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Course Image",
                modifier = Modifier
                    .padding(8.dp)
                    .size(64.dp)
                    .clip(CircleShape)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Row {
                    Text(text = courseCount, style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = studentCount, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CourseDetailPreview() {
    LearnConnectTheme(false) {
        val userDao = object : UserDao {
            override suspend fun isUserExists(email: String, password: String): Int? {
                TODO("Not yet implemented")
            }

            override suspend fun isEmailExists(email: String): Boolean {
                TODO("Not yet implemented")
            }

            override suspend fun addUser(user: User): Long {
                TODO("Not yet implemented")
            }

            override suspend fun getUserInfo(userId: Int): User {
                TODO("Not yet implemented")
            }

            override suspend fun changePassword(
                userId: Int,
                currentPassword: String,
                newPassword: String
            ): Int {
                TODO("Not yet implemented")
            }

        }
        val courseDao = object : CourseDao {
            override suspend fun getCategories(): List<String> {
                TODO("Not yet implemented")
            }

            override suspend fun getCourses(): List<Course> {
                TODO("Not yet implemented")
            }

            override suspend fun getCourseById(courseId: Int): Course {
                TODO("Not yet implemented")
            }

            override suspend fun getCoursesDone(userId: Int): List<Course> {
                TODO("Not yet implemented")
            }

            override suspend fun getCoursesInProgress(userId: Int): List<Course> {
                TODO("Not yet implemented")
            }

            override suspend fun addComment(comment: Comment) {
                TODO("Not yet implemented")
            }

            override suspend fun getComments(courseId: Int): List<Comment> {
                TODO("Not yet implemented")
            }

            override suspend fun checkEnrollmentStatus(userId: Int, courseId: Int): Boolean {
                TODO("Not yet implemented")
            }

            override suspend fun enrollToCourse(enrollment: Enrollment): Long {
                TODO("Not yet implemented")
            }

            override suspend fun unEnroll(userId: Int, courseId: Int) {
                TODO("Not yet implemented")
            }

            override suspend fun fillLessonStatusTable(lessonStatus: LessonStatus) {
                TODO("Not yet implemented")
            }

            override suspend fun cleanLessonStatusTable(userId: Int, courseId: Int) {
                TODO("Not yet implemented")
            }
        }
        val lessonDao = object : LessonDao {
            override suspend fun getLessonsByCourseId(courseId: Int): List<Lesson> {
                TODO("Not yet implemented")
            }
        }
        CourseDetailScreen(
            1,
            2,
            {},
            CourseDetailViewModel(
                UserRepository(userDao),
                CourseRepository(courseDao),
                EnrollmentRepository(courseDao, lessonDao)
            )
        )
    }
}