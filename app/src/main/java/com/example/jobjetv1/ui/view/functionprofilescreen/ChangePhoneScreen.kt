package com.example.jobjetv1.ui.view.functionprofilescreen

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.example.jobjetv1.viewmodel.OtpViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePhoneScreen(
    viewModel: OtpViewModel,
    currentPhone: String,
    onBack: () -> Unit = {},
    onOtpSent: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as? Activity
    var newPhone by remember { mutableStateOf("") }
    val isValid = newPhone.length >= 9
    val uiState = viewModel.uiState

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại")
                    }
                },
                title = { Text("Thay đổi số điện thoại", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(Modifier.height(22.dp))
            Text("Số điện thoại hiện tại", color = Color.Gray)
            Text(
                currentPhone,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 18.dp)
            )
            Text("Nhập số điện thoại mới", color = Color.Gray)
            OutlinedTextField(
                value = newPhone,
                onValueChange = { if (it.length <= 11 && it.all(Char::isDigit)) newPhone = it },
                leadingIcon = { Text("+84", color = Color.Gray, fontSize = 16.sp) },
                placeholder = { Text("Nhập số điện thoại mới") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                "Bạn sẽ nhận được mã xác minh qua số điện thoại mới",
                color = Color.Gray,
                fontSize = 13.sp,
                modifier = Modifier.padding(top = 6.dp, bottom = 18.dp)
            )
            if (uiState.isLoading) CircularProgressIndicator(modifier = Modifier.padding(bottom = 10.dp))
            uiState.errorMessage?.let {
                Text(it, color = Color.Red, fontSize = 13.sp, modifier = Modifier.padding(bottom = 10.dp))
            }
            Spacer(Modifier.weight(1f))
            Button(
                onClick = {
                    if (activity != null) {
                        viewModel.onPhoneChanged("+84$newPhone")
                        viewModel.sendOtp(
                            activity = activity,
                            onCodeSent = { onOtpSent() },
                            onFailed = { /* Hiển thị lỗi đã có ở uiState.errorMessage */ }
                        )
                    }
                },
                enabled = isValid && !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1EA0F9))
            ) {
                Text("Tiếp tục", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}
