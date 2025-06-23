package com.example.jobjetv1.ui.view.mainscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.jobjetv1.R
import com.example.jobjetv1.data.model.BankInfo
import com.example.jobjetv1.ui.view.BottomNavBar
import com.example.jobjetv1.viewmodel.ProfileUiState
import com.example.jobjetv1.viewmodel.ProfileViewModel
import com.example.jobjetv1.viewmodel.SavedJobsViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    savedJobsViewModel: SavedJobsViewModel,
    onEditProfile: () -> Unit = {},
    onRecruitClick: () -> Unit = {},
    onSavedJobsClick: () -> Unit = {},
    selectedTab: Int = 2,
    onTabSelected: (Int) -> Unit = {},
    onLogout: () -> Unit,
    onAddBank: () -> Unit = {},
    onChangeMainBank: () -> Unit = {}
) {
    val uiState = viewModel.uiState

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hồ sơ của tôi", fontWeight = FontWeight.Bold, fontSize = 19.sp) },
                actions = {
                    IconButton(onClick = onEditProfile) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            BottomNavBar(selectedIndex = selectedTab, onTabSelected = onTabSelected)
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color.White)
                    .padding(horizontal = 16.dp)
            ) {
                item {
                    Spacer(Modifier.height(16.dp))
                    
                    // Profile Header
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Avatar
                        val painter = if (!uiState.userProfile?.photoUrl.isNullOrEmpty())
                            rememberAsyncImagePainter(uiState.userProfile?.photoUrl)
                        else painterResource(id = R.drawable.outline_person_24)
                        
                        Image(
                            painter = painter,
                            contentDescription = "Avatar",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        )
                        
                        Spacer(Modifier.height(8.dp))
                        
                        // Name
                        Text(
                            text = uiState.userProfile?.displayName ?: "Chưa cập nhật",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(Modifier.height(4.dp))
                        
                        // Phone
                        Text(
                            text = uiState.userProfile?.phoneNumber ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                    
                    Spacer(Modifier.height(24.dp))
                    
                    // Profile Info Card
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            ProfileInfoRow("Email", uiState.userProfile?.email ?: "Chưa cập nhật")
                            Spacer(Modifier.height(12.dp))
                            ProfileInfoRow("Ngày sinh", uiState.userProfile?.dateOfBirth ?: "Chưa cập nhật")
                            Spacer(Modifier.height(12.dp))
                            ProfileInfoRow("Giới tính", uiState.userProfile?.gender ?: "Chưa cập nhật")
                            Spacer(Modifier.height(12.dp))
                            ProfileInfoRow("CMND/CCCD", uiState.userProfile?.idNumber ?: "Chưa cập nhật")
                            Spacer(Modifier.height(12.dp))
                            ProfileInfoRow("Địa chỉ", uiState.userProfile?.address ?: "Chưa cập nhật")
                        }
                    }
                    
                    Spacer(Modifier.height(16.dp))
                    
                    // Actions
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            // Recruitment Button
                            ListItem(
                                headlineContent = { Text("Đăng bài tuyển dụng") },
                                leadingContent = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.outline_person_24),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                },
                                modifier = Modifier.clickable { onRecruitClick() }
                            )
                            // Saved Jobs Button
                            ListItem(
                                headlineContent = { Text("Việc làm đã lưu") },
                                leadingContent = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.baseline_bookmark_24),
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                },
                                modifier = Modifier.clickable { onSavedJobsClick() }
                            )
                        }
                    }
                    Card(
                        shape = RoundedCornerShape(13.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(14.dp)) {
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Ngân hàng liên kết", fontWeight = FontWeight.Medium)
                                TextButton(onClick = onAddBank, contentPadding = PaddingValues(0.dp)) {
                                    Text("Thêm mới", fontSize = 14.sp, color = Color(0xFF2196F3))
                                }
                            }
                            Card(
                                shape = RoundedCornerShape(11.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 6.dp)
                            ) {
                                Row(
                                    Modifier.padding(11.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(painterResource(R.drawable.outline_person_24), contentDescription = null, tint = Color(0xFF2196F3), modifier = Modifier.size(26.dp))
                                    Spacer(Modifier.width(8.dp))
                                    Column(Modifier.weight(1f)) {
                                        Text("Vietcombank", fontWeight = FontWeight.Bold)
                                        Text("**** 5678", color = Color.Gray, fontSize = 14.sp)
                                    }
                                    Column(horizontalAlignment = Alignment.End) {
                                        Text("Mặc định", color = Color(0xFF2196F3), fontWeight = FontWeight.Medium, fontSize = 14.sp)
                                        Text("2,500,000 VND", fontWeight = FontWeight.Bold, color = Color.Black)
                                    }
                                }
                            }
                        }
                    }
                }
                item{
                    Button(
                        onClick = { viewModel.logout(onLogout) }, // ACTION
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) { // CONTENT starts here
                        Text("Đăng xuất", color = MaterialTheme.colorScheme.onError)
                    }
                }
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
private fun ProfileInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}
