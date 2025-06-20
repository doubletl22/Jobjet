package com.example.jobjetv1.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.*
import com.example.jobjetv1.data.prefs.UserPrefs
import com.example.jobjetv1.ui.view.loginscreen.*
import com.example.jobjetv1.ui.view.*
import com.example.jobjetv1.viewmodel.*

@Composable
fun AppNavHost() {
    val context = LocalContext.current
    val navController = rememberNavController()
    var startDestination by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        startDestination = if (UserPrefs.isLoggedIn(context)) "home" else "login"
    }

    if (startDestination == null) {
        androidx.compose.material3.CircularProgressIndicator()
    } else {
        NavHost(navController = navController, startDestination = startDestination!!) {
            composable("login") {
                val viewModel: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
                LoginScreen(
                    viewModel = viewModel,
                    onLoginSuccess = { phone, verificationId ->
                        navController.navigate("otp/$verificationId")
                    }
                )
            }
            composable("otp/{verificationId}") { backStackEntry ->
                val verificationId = backStackEntry.arguments?.getString("verificationId") ?: ""
                val viewModel: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
                OtpScreen(
                    viewModel = viewModel,
                    verificationId = verificationId,
                    onOtpSuccess = {
                        navController.navigate("success") { popUpTo(0) }
                    }
                )
            }
            composable("success") { SuccessScreen { navController.navigate("home") { popUpTo(0) } } }
            composable("home") {
                HomeScreen(
                    selectedTab = 0,
                    onTabSelected = {
                        when(it) {
                            0 -> {} // Đã ở Home
                            1 -> navController.navigate("search") { launchSingleTop = true }
                            2 -> navController.navigate("profile") { launchSingleTop = true }
                            3 -> navController.navigate("notifications") { launchSingleTop = true }
                        }
                    }
                )
            }
            composable("search") {
                SearchScreen(
                    onTabSelected = {
                        when(it) {
                            0 -> navController.navigate("home") { launchSingleTop = true }
                            2 -> navController.navigate("profile") { launchSingleTop = true }
                            3 -> navController.navigate("notifications") { launchSingleTop = true }
                        }
                    }
                )
            }
            composable("profile") {
                ProfileScreen(
                    onRecruitClick = { navController.navigate("recruitment_post") },
                    onTabSelected = {
                        when(it) {
                            0 -> navController.navigate("home") { launchSingleTop = true }
                            1 -> navController.navigate("search") { launchSingleTop = true }
                            3 -> navController.navigate("notifications") { launchSingleTop = true }
                        }
                    }
                )
            }
            composable("notifications") {
                NotificationScreen(
                    onTabNavSelected = {
                        when(it) {
                            0 -> navController.navigate("home") { launchSingleTop = true }
                            1 -> navController.navigate("search") { launchSingleTop = true }
                            2 -> navController.navigate("profile") { launchSingleTop = true }
                        }
                    }
                )
            }
            composable("recruitment_post") {
                RecruitmentPostScreen(
                    onBack = { navController.popBackStack() },
                    onSubmit = { /* Gửi API hoặc lưu lên server */ }
                )
            }
        }
    }
}
