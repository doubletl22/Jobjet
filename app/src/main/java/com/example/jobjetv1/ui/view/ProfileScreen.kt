package com.example.jobjetv1.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import coil.compose.rememberAsyncImagePainter
import com.example.jobjetv1.data.model.ProfileUiState
import com.example.jobjetv1.data.model.SavedJob
import com.example.jobjetv1.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    profile: ProfileUiState = ProfileUiState(),
    onEditProfile: () -> Unit = {},
    onUpdateProfile: () -> Unit = {},
    onRecruitClick: () -> Unit = {},
    onAddBank: () -> Unit = {},
    onCheckIn: () -> Unit = {},
    onCheckOut: () -> Unit = {},
    onSavedJobClick: (SavedJob) -> Unit = {},
    onSeeAllSavedJobs: () -> Unit = {},
    onSeeHistory: () -> Unit = {},
    onChangeMainBank: () -> Unit = {},
    selectedTab: Int = 2,
    onTabSelected: (Int) -> Unit = {}
) {
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
                .padding(horizontal = 10.dp)
        ) {
            Spacer(Modifier.height(18.dp))
            // Avatar & Name
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                val painter = if (profile.avatarUrl != null)
                    rememberAsyncImagePainter(profile.avatarUrl)
                else painterResource(id = R.drawable.outline_output_24)
                Image(
                    painter = painter,
                    contentDescription = "Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.White, CircleShape)
                )
                Spacer(Modifier.height(8.dp))
                Text(profile.name, fontWeight = FontWeight.Bold, fontSize = 19.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFBC02D), modifier = Modifier.size(19.dp))
                    Spacer(Modifier.width(3.dp))
                    Text("${profile.rating}", fontSize = 15.sp, fontWeight = FontWeight.Medium)
                }
            }
            Spacer(Modifier.height(16.dp))
            // Progress
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                CircularProgressIndicator(
                    progress = profile.progress / 100f,
                    color = Color(0xFF2196F3),
                    trackColor = Color(0xFFE3E3E3),
                    modifier = Modifier.size(46.dp),
                    strokeWidth = 5.dp
                )
                Spacer(Modifier.width(15.dp))
                Column {
                    Text("Hoàn thiện hồ sơ của bạn", fontWeight = FontWeight.Medium, fontSize = 15.sp)
                    Button(
                        onClick = onUpdateProfile,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 3.dp),
                        modifier = Modifier.height(30.dp)
                    ) {
                        Text("Cập nhật", fontSize = 13.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Spacer(Modifier.height(15.dp))

            // Tuyển dụng
            Card(
                shape = RoundedCornerShape(13.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    Modifier
                        .clickable { onRecruitClick() }
                        .padding(15.dp)
                ) {
                    Text("Đăng bài tuyển dụng", color = Color(0xFF4A4A4A), fontWeight = FontWeight.Medium)
                }
            }
            Spacer(Modifier.height(13.dp))

            // Ngân hàng liên kết
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
                                Text(profile.mainBank.bankName, fontWeight = FontWeight.Bold)
                                Text(profile.mainBank.accNo, color = Color.Gray, fontSize = 14.sp)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                if (profile.mainBank.isDefault)
                                    Text("Mặc định", color = Color(0xFF2196F3), fontWeight = FontWeight.Medium, fontSize = 14.sp)
                                Text("${profile.mainBank.balance} VND", fontWeight = FontWeight.Bold, color = Color.Black)
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(13.dp))

            // Chấm công
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
                        Text("Chấm công", fontWeight = FontWeight.Medium)
                        TextButton(onClick = onSeeHistory, contentPadding = PaddingValues(0.dp)) {
                            Text("Lịch sử", fontSize = 14.sp, color = Color(0xFF2196F3))
                        }
                    }
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                Modifier
                                    .size(8.dp)
                                    .background(Color(0xFF44B000), CircleShape)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                if (profile.working) "Đang làm việc" else "Đã kết thúc",
                                color = Color(0xFF44B000),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Text("Thời gian làm việc", color = Color.Gray, fontSize = 14.sp)
                        Text(profile.workTime, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        OutlinedButton(
                            onClick = onCheckIn,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) { Text("Check in", fontWeight = FontWeight.Bold) }
                        Spacer(Modifier.width(10.dp))
                        Button(
                            onClick = onCheckOut,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
                        ) { Text("Check out", color = Color.White, fontWeight = FontWeight.Bold) }
                    }
                }
            }
            Spacer(Modifier.height(13.dp))

            // Việc làm đã lưu
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
                        Text("Việc làm đã lưu", fontWeight = FontWeight.Medium)
                        TextButton(onClick = onSeeAllSavedJobs, contentPadding = PaddingValues(0.dp)) {
                            Text("Xem tất cả", fontSize = 14.sp, color = Color(0xFF2196F3))
                        }
                    }
                    Column {
                        profile.savedJobs.forEach { job ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onSavedJobClick(job) }
                                    .padding(vertical = 6.dp)
                            ) {
                                Icon(painterResource(R.drawable.outline_person_24), contentDescription = null, tint = Color(0xFF2196F3), modifier = Modifier.size(24.dp))
                                Spacer(Modifier.width(8.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(job.title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                    Text(job.company, color = Color.Gray, fontSize = 13.sp)
                                    Text(job.address, color = Color.Gray, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                }
                                Spacer(Modifier.width(8.dp))
                                Text(job.wage, color = Color(0xFF43A047), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(20.dp))
        }
    }
}
