package com.cevdetkilickeser.learnconnect.navigation

sealed class Screen(val route: String) {
    data object SignIn: Screen("sign_in")
    data object SignUp: Screen("sign_up")
    data object Home: Screen("home")
    data object Profile: Screen("profile")
}