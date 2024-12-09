package com.cevdetkilickeser.learnconnect.ui.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cevdetkilickeser.learnconnect.R
import com.cevdetkilickeser.learnconnect.data.entity.course.Course

@Composable
fun HomeScreen(
    navigateToCourseDetail: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val categoryList by viewModel.categoryList.collectAsState()
    val courseList by viewModel.courseList.collectAsState()
    var selectedCategory by remember { mutableStateOf<String?>(null) }
    var query by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text("Search...") },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search",
                            modifier = Modifier
                                .clip(shape = CircleShape)
                                .clickable {
                                    viewModel.getSearchResults(query)
                                    selectedCategory = null
                                }
                        )
                    },
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    shape = MaterialTheme.shapes.medium,
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Browse Categories",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                CategoryButtonsRow(
                    categoryList = categoryList,
                    selectedCategory = selectedCategory,
                    getFilteredCourseList = { label ->
                        query = ""
                        selectedCategory = label
                        viewModel.getFilteredCourses(label)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Explore",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                CourseItemList(
                    courseList = courseList,
                    navigateToCourseDetail = { courseId ->
                        navigateToCourseDetail(courseId)
                    }
                )
            }
        }
    }
}

@Composable
fun CategoryButtonsRow(
    categoryList: List<String>,
    selectedCategory: String?,
    getFilteredCourseList: (String?) -> Unit
) {

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(categoryList) { label ->
            val isSelected = selectedCategory == label
            FilterChip(
                selected = isSelected,
                onClick = {
                    getFilteredCourseList(if (isSelected) null else label)
                },
                label = { Text(label) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = Color.White
                ),
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun CourseItemList(
    courseList: List<Course>,
    navigateToCourseDetail: (String) -> Unit
) {
    LazyColumn {
        items(courseList) { course ->
            CourseItem(
                course = course,
                navigateToCourseDetail = { courseId ->
                    navigateToCourseDetail(courseId)
                }
            )
        }
    }
}

@Composable
fun CourseItem(
    course: Course,
    navigateToCourseDetail: (String) -> Unit
) {
    Card(
        onClick = { navigateToCourseDetail(course.courseId.toString()) },
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
                    .clip(MaterialTheme.shapes.small)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = course.courseName,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = course.author,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}