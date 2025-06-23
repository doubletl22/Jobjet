package com.example.jobjetv1.ui.view.functionprofilescreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jobjetv1.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    viewModel: ProfileViewModel,
    onBack: () -> Unit,
    onDone: () -> Unit,
    onChangeAvatar: () -> Unit,
    onChangeNumberPhone: () -> Unit
) {
    val uiState = viewModel.uiState
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại")
                    }
                },
                title = { Text("Hồ sơ cá nhân", fontWeight = FontWeight.Bold) },
                actions = {
                    TextButton(
                        onClick = {
                            // Save changes
                            val updates = mapOf(
                                "displayName" to (uiState.userProfile?.displayName ?: ""),
                                "email" to (uiState.userProfile?.email ?: ""),
                                "dateOfBirth" to (uiState.userProfile?.dateOfBirth ?: ""),
                                "gender" to (uiState.userProfile?.gender ?: ""),
                                "idNumber" to (uiState.userProfile?.idNumber ?: ""),
                                "address" to (uiState.userProfile?.address ?: ""),
                                "isLookingForJob" to (uiState.userProfile?.isLookingForJob ?: true)
                            )
                            viewModel.updateUserProfile(updates, onDone)
                        },
                        enabled = !uiState.isLoading
                    ) {
                        Text("Hoàn tất", color = Color(0xFF2196F3), fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFF6F8FA))
                    .verticalScroll(scrollState)
            ) {
                Spacer(Modifier.height(8.dp))

                // Profile Info Section
                ProfileEditItem(
                    label = "Họ và tên",
                    value = uiState.userProfile?.displayName ?: "Chưa cập nhật",
                    onClick = onChangeAvatar
                )

                ProfileEditItem(
                    label = "Số điện thoại",
                    value = uiState.userProfile?.phoneNumber ?: "Chưa cập nhật",
                    onClick = onChangeNumberPhone
                )

                ProfileEditItem(
                    label = "Email",
                    value = uiState.userProfile?.email ?: "Chưa cập nhật",
                    onClick = {}
                )

                ProfileEditItem(
                    label = "CMND/CCCD",
                    value = uiState.userProfile?.idNumber ?: "Chưa cập nhật",
                    onClick = {}
                )

                ProfileEditItem(
                    label = "Ngày sinh",
                    value = uiState.userProfile?.dateOfBirth ?: "Chưa cập nhật",
                    onClick = {}
                )

                ProfileEditItem(
                    label = "Giới tính",
                    value = uiState.userProfile?.gender ?: "Chưa cập nhật",
                    onClick = {}
                )

                ProfileEditItem(
                    label = "Địa chỉ",
                    value = uiState.userProfile?.address ?: "Chưa cập nhật",
                    onClick = {}
                )

                Spacer(Modifier.height(14.dp))

                // Settings Section
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 13.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Row(
                            Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Đang tìm việc", fontSize = 15.sp)
                            Switch(
                                checked = uiState.userProfile?.isLookingForJob ?: true,
                                onCheckedChange = { isLooking ->
                                    viewModel.updateUserProfile(
                                        mapOf("isLookingForJob" to isLooking)
                                    ) {}
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(22.dp))

                // Delete Profile Button
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 13.dp)
                ) {
                    Button(
                        onClick = {
                            // Handle delete profile
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFCECEC)),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("🗑  Xóa hồ sơ", color = Color(0xFFD82D2D), fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(Modifier.height(16.dp))
            }

            // Loading Indicator
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // Error Message
            uiState.errorMessage?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            }
        }
    }
}

@Composable
private fun ProfileEditItem(
    label: String,
    value: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 13.dp, vertical = 4.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                modifier = Modifier.weight(1f),
                fontSize = 15.sp
            )
            Text(
                text = value,
                color = Color(0xFF757575),
                fontSize = 15.sp
            )
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = null,
                tint = Color(0xFFCCCCCC),
                modifier = Modifier
                    .padding(start = 9.dp)
                    .size(19.dp)
                    .rotate(180f)
            )
        }
    }
}
