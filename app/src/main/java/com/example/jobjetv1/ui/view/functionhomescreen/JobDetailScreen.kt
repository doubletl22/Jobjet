package com.example.jobjetv1.ui.view.functionhomescreen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import android.util.Log
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.jobjetv1.data.model.Job
import com.example.jobjetv1.viewmodel.SavedJobsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobDetailScreen(
    job: Job?,
    savedJobsViewModel: SavedJobsViewModel,
    onBack: () -> Unit,
    onApply: () -> Unit = {}
) {
    if (job == null) {
        // Hiển thị loading hoặc error state
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Không tìm thấy thông tin công việc", fontSize = 16.sp)
                Spacer(Modifier.height(16.dp))
                Button(onClick = onBack) {
                    Text("Quay lại")
                }
            }        }
        return
    }    // Kiểm tra trạng thái đã lưu của job
    val isJobSaved = savedJobsViewModel.isJobSaved(job.id)
    
    Log.d("JobDetailScreen", "Job ${job.title} saved status: $isJobSaved")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chi tiết công việc") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại")
                    }
                },                actions = {                    IconButton(
                        onClick = {
                            Log.d("JobDetailScreen", "Bookmark clicked for job: ${job.title}, current saved status: $isJobSaved")
                            if (isJobSaved) {
                                savedJobsViewModel.removeSavedJob(job.id)
                            } else {
                                savedJobsViewModel.saveJob(job)
                            }
                        }
                    ) {Icon(
                            if (isJobSaved) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = if (isJobSaved) "Bỏ lưu công việc" else "Lưu công việc",
                            tint = if (isJobSaved) Color(0xFF2196F3) else Color.Gray
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            // Bottom buttons
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onApply,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2196F3)
                        )
                    ) {
                        Icon(Icons.Default.Send, contentDescription = null, tint = Color.White)
                        Spacer(Modifier.width(8.dp))
                        Text("Ứng tuyển ngay", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header với icon và thông tin cơ bản
            item {
                JobHeaderCard(job = job)
            }
            
            // Mô tả công việc
            item {
                JobDescriptionCard(job = job)
            }
            
            // Thông tin lương và địa điểm
            item {
                JobInfoCard(job = job)
            }
            
            // Yêu cầu công việc
            item {
                JobRequirementsCard()
            }
            
            // Thông tin công ty
            item {
                CompanyInfoCard()
            }
            
            // Spacing cho bottom bar
            item {
                Spacer(Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun JobHeaderCard(job: Job) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Company icon
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(job.wageColor.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = job.iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = job.wageColor
                )
            }
            
            Spacer(Modifier.height(16.dp))
            
            // Job title
            Text(
                text = job.title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            
            Spacer(Modifier.height(8.dp))
            
            // Company name (giả sử có thể extract từ address hoặc hardcode)
            Text(
                text = "Công ty ABC", // Có thể cải thiện bằng cách thêm field company vào Job model
                fontSize = 16.sp,
                color = Color.Gray
            )
            
            Spacer(Modifier.height(12.dp))
            
            // Location
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color(0xFF2196F3),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = job.address,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun JobDescriptionCard(job: Job) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Mô tả công việc",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(Modifier.height(12.dp))
            
            Text(
                text = job.description,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = Color(0xFF424242)
            )
        }
    }
}

@Composable
fun JobInfoCard(job: Job) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Thông tin chi tiết",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(Modifier.height(16.dp))
            
            // Mức lương
            JobInfoRow(
                icon = Icons.Default.AttachMoney,
                title = "Mức lương",
                content = job.wage,
                iconColor = Color(0xFF4CAF50)
            )
            
            Spacer(Modifier.height(12.dp))
            
            // Loại hình công việc
            JobInfoRow(
                icon = Icons.Default.Work,
                title = "Loại hình",
                content = "Bán thời gian",
                iconColor = Color(0xFF2196F3)
            )
            
            Spacer(Modifier.height(12.dp))
            
            // Thời gian làm việc
            JobInfoRow(
                icon = Icons.Default.Schedule,
                title = "Thời gian",
                content = "8:00 - 17:00",
                iconColor = Color(0xFFFF9800)
            )
            
            Spacer(Modifier.height(12.dp))
            
            // Kinh nghiệm
            JobInfoRow(
                icon = Icons.Default.Star,
                title = "Kinh nghiệm",
                content = "Không yêu cầu",
                iconColor = Color(0xFF9C27B0)
            )
        }
    }
}

@Composable
fun JobInfoRow(
    icon: ImageVector,
    title: String,
    content: String,
    iconColor: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(iconColor.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
        }
        
        Spacer(Modifier.width(12.dp))
        
        Column {
            Text(
                text = title,
                fontSize = 12.sp,
                color = Color.Gray
            )
            Text(
                text = content,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun JobRequirementsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Yêu cầu công việc",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(Modifier.height(12.dp))
            
            val requirements = listOf(
                "Có thể làm việc theo ca",
                "Chịu được áp lực công việc",
                "Có khả năng giao tiếp tốt",
                "Chăm chỉ, có trách nhiệm"
            )
            
            requirements.forEach { requirement ->
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF2196F3))
                            .padding(top = 6.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(
                        text = requirement,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
fun CompanyInfoCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "Thông tin công ty",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(Modifier.height(16.dp))
            
            Row {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFF5F5F5)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Business,
                        contentDescription = null,
                        tint = Color(0xFF2196F3),
                        modifier = Modifier.size(30.dp)
                    )
                }
                
                Spacer(Modifier.width(12.dp))
                
                Column {
                    Text(
                        text = "Công ty ABC",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Công ty TNHH",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "100-500 nhân viên",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
            
            Spacer(Modifier.height(16.dp))
            
            Text(
                text = "Công ty chuyên về dịch vụ logistics và warehouse. Môi trường làm việc chuyên nghiệp, chế độ đào tạo tốt.",
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = Color(0xFF424242)
            )
        }
    }
}
