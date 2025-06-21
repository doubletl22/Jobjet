package com.example.jobjetv1.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.example.jobjetv1.data.model.EditProfileUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    uiState: EditProfileUiState = EditProfileUiState(),
    onBack: () -> Unit = {},
    onDone: () -> Unit = {},
    onDelete: () -> Unit = {},
    onChangeAvatar: () -> Unit = {},
    onChangeNumberPhone: () -> Unit = {},
    onFieldClick: (String) -> Unit = {},
) {
    var state by remember { mutableStateOf(uiState) }
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
                    TextButton(onClick = onDone) {
                        Text("Hoàn tất", color = Color(0xFF2196F3), fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF6F8FA))
        ) {
            Spacer(Modifier.height(8.dp))
            // Thông tin cá nhân
            ProfileEditItem(
                label = "Thông tin cá nhân",
                value = state.name,
                onClick = onChangeAvatar
            )
            ProfileEditItem(
                label = "Số điện thoại",
                value = state.phone,
                onClick = onChangeNumberPhone
            )

            ProfileEditItem("CCCD", state.idCard) { onFieldClick("idCard") }
            ProfileEditItem("Ngày sinh", state.birth) { onFieldClick("birth") }
            ProfileEditItem("Giới tính", state.gender) { onFieldClick("gender") }
            ProfileEditItem("Nơi thường trú", state.address) { onFieldClick("address") }
            Spacer(Modifier.height(14.dp))
            // Cài đặt bổ sung
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
                        Text("Hồ sơ công khai", fontSize = 15.sp)
                        Switch(
                            checked = state.isPublic,
                            onCheckedChange = { state = state.copy(isPublic = it) },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF2196F3))
                        )
                    }
                    Spacer(Modifier.height(6.dp))
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Đang tìm việc", fontSize = 15.sp)
                        Switch(
                            checked = state.isLookingForJob,
                            onCheckedChange = { state = state.copy(isLookingForJob = it) },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF2196F3))
                        )
                    }
                }
            }
            Spacer(Modifier.height(22.dp))
            // Nút xoá hồ sơ
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 13.dp)
            ) {
                Button(
                    onClick = onDelete,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFCECEC)),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("🗑  Xóa hồ sơ", color = Color(0xFFD82D2D), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ProfileEditItem(label: String, value: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 13.dp, vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, modifier = Modifier.weight(1f), fontSize = 15.sp)
            Text(value, color = Color(0xFF757575), fontSize = 15.sp)
            Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color(0xFFCCCCCC), modifier = Modifier
                .padding(start = 9.dp)
                .size(19.dp)
                .rotate(180f))
        }
    }
}
