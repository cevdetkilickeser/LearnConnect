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
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cevdetkilickeser.learnconnect.navigation.AppNavigation
import com.cevdetkilickeser.learnconnect.navigation.MyBottomBar
import com.cevdetkilickeser.learnconnect.navigation.MyTopBar
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
                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = currentBackStackEntry?.destination?.route

                Scaffold(
                    topBar = {
                        if (currentDestination != Screen.SignIn.route &&
                            currentDestination != Screen.SignUp.route &&
                            currentDestination != null
                        ) {
                            MyTopBar(
                                onBackClick = { navController.popBackStack() },
                                username = "Cevdet",
                                profileImage = painterResource(id = R.drawable.ic_launcher_foreground)
                            )
                        }
                    },
                    bottomBar = {
                        if (currentDestination != Screen.SignIn.route &&
                            currentDestination != Screen.SignUp.route &&
                            currentDestination != Screen.WatchCourse.route &&
                            currentDestination != null
                        ) {
                            MyBottomBar(
                                currentDestination = currentDestination,
                                onHomeClick = { navController.navigate(Screen.Home.route) },
                                onMyCoursesClick = { navController.navigate(Screen.MyCourses.route) },
                                onProfileClick = { navController.navigate(Screen.Profile.route) }
                            )
                        }
                    }
                ) { innerPadding ->
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