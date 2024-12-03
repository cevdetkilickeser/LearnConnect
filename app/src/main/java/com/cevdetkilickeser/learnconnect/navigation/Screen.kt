package com.cevdetkilickeser.learnconnect.navigation

sealed class Screen(val route: String) {
    data object SignIn: Screen("sign_in")
    data object SignUp: Screen("sign_up")
    data object Home: Screen("home")
    data object Profile: Screen("profile")
    data object CourseDetail: Screen("course_detail{courseId}")
    data object WatchCourse: Screen("watch_course{courseId}")

    fun withArgs(vararg args: String): String {
        return route.replace("{courseId}", args[0])
    }
}