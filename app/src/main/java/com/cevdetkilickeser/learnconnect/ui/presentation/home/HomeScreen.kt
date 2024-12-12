package com.cevdetkilickeser.learnconnect.ui.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.cevdetkilickeser.learnconnect.collectWithLifecycle
import com.cevdetkilickeser.learnconnect.ui.presentation.home.HomeContract.HomeAction
import com.cevdetkilickeser.learnconnect.ui.presentation.home.HomeContract.HomeEffect
import com.cevdetkilickeser.learnconnect.ui.presentation.home.HomeContract.HomeState
import kotlinx.coroutines.flow.Flow

@Composable
fun HomeScreen(
    homeState: HomeState,
    homeEffect: Flow<HomeEffect>,
    homeAction: (HomeAction) -> Unit,
    navigateToCourseDetail: (String) -> Unit,
) {

    homeEffect.collectWithLifecycle { effect ->
        when (effect) {
            is HomeEffect.NavigateToCourseDetail -> {
                navigateToCourseDetail(effect.courseId)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                OutlinedTextField(
                    value = homeState.query,
                    onValueChange = { homeAction(HomeAction.QueryChanged(it)) },
                    label = { Text("Search...") },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search",
                            modifier = Modifier.clip(shape = CircleShape)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = MaterialTheme.shapes.medium,
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Browse Categories",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(homeState.categoryList) { label ->
                        val isSelected = homeState.selectedCategory == label
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                val selectedCategory = if (isSelected) null else label
                                homeAction(HomeAction.CategorySelected((selectedCategory)))
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

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Explore",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyColumn {
                    items(homeState.courseList) { course ->
                        CourseItem(
                            course = course,
                            navigateToCourseDetail = { courseId ->
                                homeAction(HomeAction.CourseClicked(courseId))
                            }
                        )
                    }
                }
            }
        }
    }
}