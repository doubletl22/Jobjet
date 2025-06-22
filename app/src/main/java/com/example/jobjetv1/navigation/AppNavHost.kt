package com.example.jobjetv1.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.example.jobjetv1.data.prefs.UserPrefs
import com.example.jobjetv1.ui.view.loginscreen.*
import com.example.jobjetv1.ui.view.functionhomescreen.ApplicationScreen
import com.example.jobjetv1.ui.view.functionhomescreen.ApplicationSuccessScreen
import com.example.jobjetv1.ui.view.functionhomescreen.JobDetailScreen
import com.example.jobjetv1.ui.view.functionhomescreen.RecruitmentPostScreen
import com.example.jobjetv1.ui.view.functionprofilescreen.ChangePhoneScreen
import com.example.jobjetv1.ui.view.functionprofilescreen.EditAvatarScreen
import com.example.jobjetv1.ui.view.functionprofilescreen.EditProfileScreen
import com.example.jobjetv1.ui.view.functionprofilescreen.OtpChangePhoneScreen
import com.example.jobjetv1.ui.view.functionprofilescreen.SavedJobsScreen
import com.example.jobjetv1.viewmodel.*
import com.example.jobjetv1.ui.view.mainscreen.HomeScreen
import com.example.jobjetv1.ui.view.mainscreen.NotificationScreen
import com.example.jobjetv1.ui.view.mainscreen.ProfileScreen
import com.example.jobjetv1.ui.view.mainscreen.SearchScreen

@Composable
fun AppNavHost() {
    val context = LocalContext.current
    val navController = rememberNavController()
    var startDestination by remember { mutableStateOf<String?>(null) }
    
    // Tạo single instance của SavedJobsViewModel để chia sẻ state
    val savedJobsViewModel: SavedJobsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    
    // Tạo single instance của HomeViewModel để chia sẻ state và reactive updates
    val homeViewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

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
                    viewModel = homeViewModel, // Sử dụng shared HomeViewModel
                    savedJobsViewModel = savedJobsViewModel, // Truyền SavedJobsViewModel
                    selectedTab = 0,
                    onTabSelected = {
                        when (it) {
                            0 -> {} // Đã ở Home
                            1 -> navController.navigate("search") { launchSingleTop = true }
                            2 -> navController.navigate("profile") { launchSingleTop = true }
                            3 -> navController.navigate("notifications") { launchSingleTop = true }
                        }
                    },
                    onJobClick = { job ->
                        navController.navigate("job_detail/${job.id}")
                    }
                )
            }
            composable("job_detail/{jobId}") { backStackEntry ->
                val jobId = backStackEntry.arguments?.getString("jobId")
                val job = homeViewModel.getJobById(jobId ?: "")

                JobDetailScreen(
                    job = job,
                    savedJobsViewModel = savedJobsViewModel,
                    onBack = { navController.popBackStack() },
                    onApply = {
                        // Navigate to application screen
                        navController.navigate("application/${jobId}")
                    }
                )
            }
            composable("application/{jobId}") { backStackEntry ->
                val jobId = backStackEntry.arguments?.getString("jobId")
                val job = homeViewModel.getJobById(jobId ?: "")

                ApplicationScreen(
                    job = job,
                    onBack = { navController.popBackStack() },
                    onSubmit = { applicationData ->
                        // Process application (save to database, send to server, etc.)
                        navController.navigate("application_success/${job?.title ?: "Công việc"}") {
                            popUpTo("job_detail/${jobId}") { inclusive = true }
                        }
                    }
                )
            }
            composable("application_success/{jobTitle}") { backStackEntry ->
                val jobTitle = backStackEntry.arguments?.getString("jobTitle") ?: "Công việc"

                ApplicationSuccessScreen(
                    jobTitle = jobTitle,
                    onBackToHome = {
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    },
                    onViewApplications = {
                        // Navigate to applications list if implemented
                        navController.navigate("home")
                    }
                )
            }
            composable("search") {
                SearchScreen(
                    onTabSelected = {
                        when (it) {
                            0 -> navController.navigate("home") { launchSingleTop = true }
                            2 -> navController.navigate("profile") { launchSingleTop = true }
                            3 -> navController.navigate("notifications") { launchSingleTop = true }
                        }
                    }
                )
            }
            composable("profile") {
                ProfileScreen(
                    savedJobsViewModel = savedJobsViewModel,
                    onEditProfile = { navController.navigate("edit_profile") },
                    onRecruitClick = { navController.navigate("recruitment_post") },
                    onSavedJobsClick = { navController.navigate("saved_jobs") },
                    onTabSelected = {
                        when (it) {
                            0 -> navController.navigate("home") { launchSingleTop = true }
                            1 -> navController.navigate("search") { launchSingleTop = true }
                            3 -> navController.navigate("notifications") { launchSingleTop = true }
                        }
                    }
                )
            }
            composable("edit_profile") {
                EditProfileScreen(
                    onChangeAvatar = { navController.navigate("edit_avatar") },
                    onChangeNumberPhone = { navController.navigate("change_phone") },
                    onBack = { navController.popBackStack() },
                    onDone = { navController.navigate("profile") },
                    onDelete = { /* xử lý xoá hồ sơ */ },
                    onFieldClick = { field ->
                    }
                )
            }
            composable("edit_avatar") {
                EditAvatarScreen(
                    onBack = { navController.popBackStack() },
                    onTakePhoto = { /* mở camera */ },
                    onPickGallery = { /* mở thư viện */ },
                    onDeletePhoto = { /* xóa avatar */ }
                )
            }
            composable("change_phone") {
                val otpViewModel: OtpViewModel = viewModel()
                ChangePhoneScreen(
                    viewModel = otpViewModel,
                    currentPhone = "0912 345 678",
                    onBack = { navController.popBackStack() },
                    onOtpSent = { navController.navigate("otp/change_phone") }
                )
            }

            composable("otp/change_phone") {
                val otpViewModel: OtpViewModel = viewModel()
                OtpChangePhoneScreen(
                    viewModel = otpViewModel,
                    phone = otpViewModel.uiState.phone,
                    onBack = { navController.popBackStack() },
                    onOtpSuccess = {
                        navController.popBackStack("edit_profile", inclusive = false)
                    }
                )
            }

            composable("saved_jobs") {
                SavedJobsScreen(
                    viewModel = savedJobsViewModel,
                    onBack = { navController.popBackStack() },
                    onJobClick = { job ->
                        navController.navigate("job_detail/${job.id}")
                    }
                )
            }
            composable("notifications") {
                NotificationScreen(
                    onTabNavSelected = {
                        when (it) {
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
                    onSubmit = { submittedJobPost ->
                        // Navigate to success screen sau khi thêm job thành công
                        navController.navigate("recruitment_success/${submittedJobPost.jobTitle}") {
                            popUpTo("profile") { inclusive = false }
                        }
                    }
                )
            }
            composable("recruitment_success/{jobTitle}") { backStackEntry ->
                val jobTitle = backStackEntry.arguments?.getString("jobTitle") ?: "Công việc"
                
                // Enhanced success screen with job title
                androidx.compose.foundation.layout.Column(
                    modifier = androidx.compose.ui.Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
                ) {
                    Text(
                        "🎉 Đăng bài thành công!",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = androidx.compose.ui.graphics.Color(0xFF2E7D32)
                    )
                    
                    androidx.compose.foundation.layout.Spacer(androidx.compose.ui.Modifier.height(16.dp))
                    
                    Text(
                        "Bài đăng \"$jobTitle\" đã được đăng và sẽ xuất hiện ở trang chủ ngay bây giờ!",
                        fontSize = 16.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        color = androidx.compose.ui.graphics.Color(0xFF424242)
                    )
                    
                    androidx.compose.foundation.layout.Spacer(androidx.compose.ui.Modifier.height(32.dp))
                    
                    androidx.compose.material3.Button(
                        onClick = {
                            navController.navigate("home") {
                                popUpTo("home") { inclusive = true }
                            }
                        }
                    ) {
                        Text("Về trang chủ")
                    }
                }
            }
        }
    }
}
