package com.example.jobjetv1.view

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.jobjetv1.viewmodel.OtpEvent
import com.example.jobjetv1.viewmodel.OtpViewModel
import com.example.jobjetv1.viewmodel.OtpViewModelFactory
import kotlinx.coroutines.delay

@Composable
fun OtpScreen(
    navController: NavController,
    verificationId: String?,
    phoneNumber: String?
) {
    // Nếu verificationId null, không thể làm gì, có thể quay lại
    if (verificationId == null) {
        // Có thể hiển thị thông báo lỗi và quay lại màn hình trước
        navController.popBackStack()
        return
    }

    // Sử dụng Factory để tạo ViewModel với verificationId
    val otpViewModel: OtpViewModel = viewModel(
        factory = OtpViewModelFactory(verificationId)
    )

    val context = LocalContext.current
    val otpCode by otpViewModel.otpCode.collectAsState()
    val isLoading by otpViewModel.isLoading.collectAsState()

    // Lắng nghe sự kiện từ ViewModel
    LaunchedEffect(key1 = context) {
        otpViewModel.event.collect { event ->
            when (event) {
                is OtpEvent.NavigateToSuccess -> {
                    Toast.makeText(context, "Xác thực thành công!", Toast.LENGTH_SHORT).show()
                    navController.navigate("success") {
                        popUpTo("login") { inclusive = true }
                    }
                    otpViewModel.onEventHandled()
                }
                is OtpEvent.ShowError -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                    otpViewModel.onEventHandled()
                }
                OtpEvent.Idle -> {}
            }
        }
    }

    // Giao diện (tương tự như trước, chỉ kết nối state)
    var countdown by remember { mutableStateOf(59) }
    LaunchedEffect(key1 = countdown) {
        if (countdown > 0) {
            delay(1000L)
            countdown--
        }
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Xác minh số điện thoại", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Vui lòng nhập mã xác thực đã gửi đến\n+84 ${phoneNumber ?: "..."}",
                textAlign = TextAlign.Center,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(48.dp))

            OutlinedTextField(
                value = otpCode,
                onValueChange = { otpViewModel.onOtpCodeChanged(it) },
                label = { Text("Nhập mã OTP") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { otpViewModel.verifyOtp() },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("XÁC NHẬN")
                }
            }

            // ... (Phần UI còn lại)
        }
    }
}