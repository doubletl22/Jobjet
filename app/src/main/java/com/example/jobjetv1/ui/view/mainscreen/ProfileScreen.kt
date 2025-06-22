package com.example.jobjetv1.ui.view.mainscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn // Import LazyColumn
import androidx.compose.foundation.lazy.items // Import items cho LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.KeyboardArrowRight
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
import android.util.Log
import coil.compose.rememberAsyncImagePainter
import com.example.jobjetv1.data.model.ProfileUiState
import com.example.jobjetv1.data.model.SavedJob
import com.example.jobjetv1.viewmodel.SavedJobsViewModel
import com.example.jobjetv1.R
import com.example.jobjetv1.ui.view.BottomNavBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    profile: ProfileUiState = ProfileUiState(),
    savedJobsViewModel: SavedJobsViewModel? = null,
    onEditProfile: () -> Unit = {},
    onUpdateProfile: () -> Unit = {},
    onRecruitClick: () -> Unit = {},
    onAddBank: () -> Unit = {},
    onCheckIn: () -> Unit = {},
    onCheckOut: () -> Unit = {},
    onSeeAllSavedJobs: () -> Unit = {},
    onSeeHistory: () -> Unit = {},
    onChangeMainBank: () -> Unit = {},
    selectedTab: Int = 2,
    onTabSelected: (Int) -> Unit = {},

    onSavedJobsClick: () -> Unit = {}
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
        LazyColumn( // Thay đổi từ Column sang LazyColumn
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
                .padding(horizontal = 10.dp) // padding này sẽ áp dụng cho toàn bộ LazyColumn
        ) {
            // Sử dụng 'item { ... }' cho mỗi phần tử UI cố định
            item {
                Spacer(Modifier.height(18.dp))
                // Avatar & Name
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
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
            }

            item {
                // Progress
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(Modifier.width(15.dp))
                    Text("Hoàn thiện hồ sơ của bạn", fontWeight = FontWeight.Medium, fontSize = 15.sp)

                    Button(
                        onClick = onUpdateProfile,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 3.dp),
                        modifier = Modifier
                            .height(30.dp)
                            .weight(1f, fill = false)
                    ) {
                        Text("Cập nhật", fontSize = 13.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(Modifier.height(15.dp))
            }

            item {
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
            }

            item {
                // Việc làm đã lưu
                val savedJobsCount = savedJobsViewModel?.getSavedJobsCount() ?: 0
                
                // Debug log để kiểm tra reactivity
                LaunchedEffect(savedJobsCount) {
                    Log.d("ProfileScreen", "Saved jobs count updated: $savedJobsCount")
                }
                
                Card(
                    shape = RoundedCornerShape(13.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        Modifier
                            .clickable { onSavedJobsClick() }
                            .padding(15.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_bookmark_24),
                            contentDescription = null,
                            tint = Color(0xFF2196F3),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "Việc làm đã lưu", 
                                color = Color(0xFF4A4A4A), 
                                fontWeight = FontWeight.Medium
                            )
                            if (savedJobsCount > 0) {
                                Text(
                                    "$savedJobsCount việc làm đã lưu",
                                    color = Color.Gray,
                                    fontSize = 12.sp
                                )
                            }
                        }
                        Icon(
                            Icons.Default.KeyboardArrowRight,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(Modifier.height(13.dp))
            }

            item {
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
            }

            item {
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
            }

            item {
                // Việc làm đã lưu - Phần đầu của Card
                val savedJobsCount = savedJobsViewModel?.getSavedJobsCount() ?: 0
                
                // Debug log để kiểm tra reactivity
                LaunchedEffect(savedJobsCount) {
                    Log.d("ProfileScreen", "Second card - Saved jobs count updated: $savedJobsCount")
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
                            Text("Việc làm đã lưu", fontWeight = FontWeight.Medium)
                            if (savedJobsCount > 0) {
                                TextButton(onClick = onSeeAllSavedJobs, contentPadding = PaddingValues(0.dp)) {
                                    Text("$savedJobsCount việc làm", fontSize = 14.sp, color = Color(0xFF2196F3))
                                }
                            } else {
                                Text("Chưa có việc làm nào", fontSize = 14.sp, color = Color.Gray)
                            }
                        }
                        // Thêm Spacer hoặc tách riêng để quản lý khoảng cách nếu cần
                    }
                }
            }

            // Danh sách các việc làm đã lưu
            savedJobsViewModel?.let { viewModel ->
                items(viewModel.savedJobs, key = { it.id }) { job ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { 
                                // Navigate to job detail - cần convert Job to SavedJob hoặc xử lý khác
                                // onSavedJobClick(job) 
                            }
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            painterResource(R.drawable.outline_person_24), 
                            contentDescription = null, 
                            tint = Color(0xFF2196F3), 
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Column(Modifier.weight(1f)) {
                            Text(job.title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text(job.address, color = Color.Gray, fontSize = 13.sp)
                            Text(
                                job.description, 
                                color = Color.Gray, 
                                fontSize = 13.sp, 
                                maxLines = 1, 
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Spacer(Modifier.width(8.dp))
                        Text(job.wage, color = job.wageColor, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
            }
            // Nếu bạn muốn có một Card riêng cho các savedJobs và chúng nằm trong đó,
            // bạn sẽ cần xử lý cấu trúc này cẩn thận hơn, có thể dùng một LazyColumn lồng bên trong Card
            // hoặc đơn giản hóa Card bọc bên ngoài.

            item {
                Spacer(Modifier.height(20.dp)) // Spacer cuối cùng
            }
        }
    }
}