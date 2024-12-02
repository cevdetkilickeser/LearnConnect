package com.cevdetkilickeser.learnconnect

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.cevdetkilickeser.learnconnect.navigation.AppNavigation
import com.cevdetkilickeser.learnconnect.navigation.Screen
import com.cevdetkilickeser.learnconnect.ui.theme.LearnConnectTheme
import javax.inject.Inject

class MainActivity : ComponentActivity() {
    @Inject
    lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val userId = sharedPref.getInt("userId", -1)
        val startDestination = if (userId == -1) Screen.SignIn.route else Screen.Home.route

        setContent {
            val systemTheme = isSystemInDarkTheme()
            var isDarkTheme by remember { mutableStateOf(systemTheme) }

            LearnConnectTheme {
                val navController = rememberNavController()

                Scaffold { innerPadding ->
                    AppNavigation(
                        isDarkTheme = isDarkTheme,
                        changeAppTheme = { isDarkTheme = !isDarkTheme },
                        sharedPref = sharedPref,
                        startDestination = startDestination,
                        navController = navController,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }
            }
        }
    }
}