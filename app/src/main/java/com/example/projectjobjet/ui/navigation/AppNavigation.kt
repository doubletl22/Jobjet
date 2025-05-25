package com.example.projectjobjet.ui.navigation //

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
// import com.example.projectjobjet.ui.screens.auth.LoginScreen // Ví dụ import màn hình Login

// Định nghĩa các route (đường dẫn) cho màn hình
object AppDestinations {
    const val LOGIN_ROUTE = "login"
    // const val JOB_LIST_ROUTE = "job_list"
    // Thêm các route khác ở đây
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppDestinations.LOGIN_ROUTE) {
        composable(AppDestinations.LOGIN_ROUTE) {
            // LoginScreen(navController = navController) // Gọi Composable của màn hình Login
            // Hiện tại LoginScreen của bạn đang rỗng, nên có thể hiển thị tạm một Text
            androidx.compose.material3.Text("Login Screen Placeholder")
        }
        // composable(AppDestinations.JOB_LIST_ROUTE) {
        //     JobListScreen(navController = navController)
        // }
        // Thêm các màn hình khác ở đây
    }
}