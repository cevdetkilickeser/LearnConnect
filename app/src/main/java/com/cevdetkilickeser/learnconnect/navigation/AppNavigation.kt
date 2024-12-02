package com.cevdetkilickeser.learnconnect.navigation

import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cevdetkilickeser.learnconnect.ui.presentation.signin.SignInScreen

@Composable
fun AppNavigation(
    sharedPref: SharedPreferences,
    startDestination: String,
    navController: NavHostController,
    modifier: Modifier
) {
    fun saveUserIdToSharedPref(userId: Int) {
        sharedPref.edit().putInt("userId", userId).apply()
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.SignIn.route) {
            SignInScreen(
                saveUserIdToShared = { userId ->
                    saveUserIdToSharedPref(userId)
                },
                navigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                },
                navigateToSignUp = { navController.navigate(Screen.SignUp.route) }
            )
        }
    }
}