package com.cevdetkilickeser.learnconnect.ui.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.cevdetkilickeser.learnconnect.R
import com.cevdetkilickeser.learnconnect.data.entity.course.Course

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