package com.example.jobjetv1.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.*
import com.example.jobjetv1.ui.view.loginscreen.LoginScreen
import com.example.jobjetv1.ui.view.loginscreen.OtpScreen
import com.example.jobjetv1.ui.view.loginscreen.SuccessScreen
import com.example.jobjetv1.viewmodel.OtpViewModel

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val otpViewModel = remember { OtpViewModel() }

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                viewModel = otpViewModel,
                onLoginSuccess = {
                    navController.navigate("otp")
                },
                onGoogleLogin = {
                    navController.navigate("success")
                }
            )
        }
        composable("otp") {
            OtpScreen(
                viewModel = otpViewModel,
                onOtpSuccess = { navController.navigate("success") },
                onChangePhone = { navController.popBackStack("login", false) }
            )
        }
        composable("success") {
            SuccessScreen(
                onContinue = { /* chuyển sang main app hoặc HomeScreen nếu muốn */ }
            )
        }
    }
}
