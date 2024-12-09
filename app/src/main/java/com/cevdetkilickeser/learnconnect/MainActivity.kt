package com.cevdetkilickeser.learnconnect

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cevdetkilickeser.learnconnect.navigation.AppNavigation
import com.cevdetkilickeser.learnconnect.navigation.MyBottomBar
import com.cevdetkilickeser.learnconnect.navigation.MyTopBar
import com.cevdetkilickeser.learnconnect.navigation.Screen
import com.cevdetkilickeser.learnconnect.ui.theme.LearnConnectTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@Suppress("DEPRECATION")
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var sharedPref: SharedPreferences

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (Build.VERSION.SDK_INT >= 30) {
            this.window?.apply {
                WindowCompat.setDecorFitsSystemWindows(this, false)
                insetsController?.apply {
                    hide(WindowInsets.Type.systemBars())
                    systemBarsBehavior =
                        WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                }
            }
        } else {
            this.window.decorView.systemUiVisibility =
                (View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }

        val userId = sharedPref.getInt("userId", -1)
        val startDestination = if (userId == -1) Screen.SignIn.route else Screen.Home.route

        setContent {
            val systemTheme = isSystemInDarkTheme()
            var isDarkTheme by remember { mutableStateOf(systemTheme) }

            LearnConnectTheme(isDarkTheme) {
                val viewModel: MainViewModel = hiltViewModel()
                val navController = rememberNavController()
                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = currentBackStackEntry?.destination?.route
                val name by viewModel.name.collectAsState()

                LaunchedEffect(Unit) {
                    viewModel.getUserInfo(userId)
                }

                Scaffold(
                    topBar = {
                        if (
                            currentDestination != Screen.SignIn.route &&
                            currentDestination != Screen.SignUp.route &&
                            currentDestination != Screen.Profile.route &&
                            currentDestination != Screen.WatchCourse.route &&
                            currentDestination != null
                        ) {
                            MyTopBar(
                                onBackClick = {
                                    if (currentDestination == "home") {
                                        this.finish()
                                    } else {
                                        navController.popBackStack()
                                    }
                                },
                                currentDestination = currentDestination,
                                username = name
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
                                onHomeClick = {
                                    if (currentDestination != Screen.Home.route) {
                                        navController.navigate(Screen.Home.route) {
                                            popUpTo(0) { inclusive = true }
                                            launchSingleTop = true
                                        }
                                    }
                                },
                                onMyCoursesClick = {
                                    if (currentDestination != Screen.MyCourses.route) {
                                        navController.navigate(Screen.MyCourses.route) {
                                            popUpTo(Screen.MyCourses.route) {
                                                inclusive = false
                                            }
                                            launchSingleTop = true
                                        }
                                    }
                                },
                                onProfileClick = {
                                    if (currentDestination != Screen.Profile.route) {
                                        navController.navigate(Screen.Profile.route) {
                                            popUpTo(Screen.MyCourses.route) {
                                                inclusive = false
                                            }
                                            launchSingleTop = true
                                        }
                                    }
                                }
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
                        updateTopBarName = { viewModel.getUserInfo(userId) },
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    )
                }
            }
        }
    }
}