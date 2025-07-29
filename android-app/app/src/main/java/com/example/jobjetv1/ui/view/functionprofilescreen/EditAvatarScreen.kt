package com.example.jobjetv1.ui.view.functionprofilescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import coil.compose.rememberAsyncImagePainter
import com.example.jobjetv1.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAvatarScreen(
    avatarUrl: String? = null,
    onBack: () -> Unit = {},
    onTakePhoto: () -> Unit = {},
    onPickGallery: () -> Unit = {},
    onDeletePhoto: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại")
                    }
                },
                title = { Text("Cập nhật ảnh đại diện", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF7F8FC)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))
            // Avatar hình tròn lớn
            Box(
                Modifier
                    .size(154.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = if (avatarUrl != null)
                        rememberAsyncImagePainter(avatarUrl)
                    else
                        painterResource(R.drawable.images),
                    contentDescription = null,
                    modifier = Modifier
                        .size(146.dp)
                        .clip(CircleShape)
                        .border(4.dp, Color(0xFFDAE5F0), CircleShape)
                )
            }
            Spacer(Modifier.height(10.dp))
            Text("Ảnh đại diện hiện tại", color = Color.Gray, fontSize = 15.sp)
            Spacer(Modifier.height(22.dp))

            // Nút chụp ảnh mới
            EditAvatarButton(
                text = "Chụp ảnh mới",
                icon = R.drawable.baseline_camera_24, // Đặt icon camera vào drawable
                onClick = onTakePhoto,
                background = Color(0xFFE8EFFB)
            )
            Spacer(Modifier.height(10.dp))
            // Nút chọn từ thư viện
            EditAvatarButton(
                text = "Chọn từ thư viện",
                icon = R.drawable.outline_photo_album_24, // Đặt icon gallery vào drawable
                onClick = onPickGallery,
                background = Color(0xFFE8EFFB)
            )
            Spacer(Modifier.height(10.dp))
            // Nút xóa ảnh
            EditAvatarButton(
                text = "Xóa ảnh hiện tại",
                icon = R.drawable.outline_delete_24, // Đặt icon thùng rác vào drawable
                onClick = onDeletePhoto,
                background = Color(0xFFFCECEC),
                contentColor = Color(0xFFD82D2D)
            )

            Spacer(Modifier.height(22.dp))
            // Ghi chú
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8EFFB)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .padding(horizontal = 28.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    "Ảnh đại diện sẽ hiển thị trong hồ sơ.",
                    color = Color(0xFF5680B2),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(13.dp)
                )
            }
        }
    }
}

@Composable
fun EditAvatarButton(
    text: String,
    icon: Int,
    onClick: () -> Unit,
    background: Color = Color(0xFFE8EFFB),
    contentColor: Color = Color(0xFF163366)
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = background, contentColor = contentColor),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp)
            .height(44.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        Icon(painterResource(icon), contentDescription = null, modifier = Modifier.size(22.dp))
        Spacer(Modifier.width(13.dp))
        Text(text, fontWeight = FontWeight.Medium, fontSize = 16.sp)
    }
}
