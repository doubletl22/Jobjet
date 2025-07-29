package com.example.jobjetv1.ui.view.functionhomescreen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.example.jobjetv1.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationSuccessScreen(
    jobTitle: String = "Công việc",
    onBackToHome: () -> Unit,
    onViewApplications: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ứng tuyển thành công") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Success icon
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(
                        Color(0xFF4CAF50).copy(alpha = 0.1f),
                        RoundedCornerShape(60.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(60.dp),
                    tint = Color(0xFF4CAF50)
                )
            }
            
            Spacer(Modifier.height(32.dp))
            
            Text(
                text = "🎉 Ứng tuyển thành công!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            
            Spacer(Modifier.height(16.dp))
            
            Text(
                text = "Đơn ứng tuyển cho vị trí \"$jobTitle\" đã được gửi thành công!",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = Color(0xFF424242),
                lineHeight = 24.sp
            )
            
            Spacer(Modifier.height(12.dp))
            
            Text(
                text = "Nhà tuyển dụng sẽ liên hệ với bạn trong thời gian sớm nhất.",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = Color.Gray,
                lineHeight = 20.sp
            )
            
            Spacer(Modifier.height(48.dp))
            
            // Action buttons
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onBackToHome,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2196F3)
                    )
                ) {
                    Icon(Icons.Default.Home, contentDescription = null, tint = Color.White)
                    Spacer(Modifier.width(8.dp))
                    Text("Về trang chủ", color = Color.White, fontWeight = FontWeight.Bold)
                }
                
                OutlinedButton(
                    onClick = onViewApplications,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFF2196F3))
                ) {
                    Icon(Icons.Default.List, contentDescription = null, tint = Color(0xFF2196F3))
                    Spacer(Modifier.width(8.dp))
                    Text("Xem đơn ứng tuyển", color = Color(0xFF2196F3), fontWeight = FontWeight.Bold)
                }
            }
            
            Spacer(Modifier.height(32.dp))
            
            // Tips card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_lightbulb_24),
                        contentDescription = null,
                        tint = Color(0xFFFF9800),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Lời khuyên",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Hãy chuẩn bị sẵn sàng để trả lời cuộc gọi từ nhà tuyển dụng và tìm hiểu thêm về công ty.",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    }
}
