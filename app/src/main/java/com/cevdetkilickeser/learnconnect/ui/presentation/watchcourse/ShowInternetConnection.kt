package com.cevdetkilickeser.learnconnect.ui.presentation.watchcourse

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

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