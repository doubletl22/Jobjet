package com.example.jobjetv1.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.jobjetv1.ui.loginscreen.LoginScreen
import com.example.jobjetv1.ui.loginscreen.OtpScreen
import com.example.jobjetv1.ui.loginscreen.SuccessScreen
import com.example.jobjetv1.ui.mainscreen.JobListScreen // Import màn hình mới


@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController)
        }
        composable(
            route = "otp/{verificationId}/{phoneNumber}", // Thay đổi route
            arguments = listOf(
                navArgument("verificationId") { type = NavType.StringType },
                navArgument("phoneNumber") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            OtpScreen(
                navController = navController,
                verificationId = backStackEntry.arguments?.getString("verificationId"),
                phoneNumber = backStackEntry.arguments?.getString("phoneNumber")
            )
        }
        composable("success") {
            SuccessScreen(navController)
        }
        composable("job_list") { // Thêm route cho màn hình danh sách công việc
            JobListScreen()
        }
    }
}