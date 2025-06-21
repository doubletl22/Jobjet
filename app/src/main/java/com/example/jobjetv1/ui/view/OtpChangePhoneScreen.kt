package com.example.jobjetv1.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.example.jobjetv1.viewmodel.OtpViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpChangePhoneScreen(
    viewModel: OtpViewModel,
    phone: String,
    onBack: () -> Unit = {},
    onOtpSuccess: () -> Unit,
    onResend: (() -> Unit)? = null
) {
    val uiState = viewModel.uiState
    var timeLeft by remember { mutableStateOf(viewModel.resendSeconds) }
    // Update time left mỗi giây
    LaunchedEffect(uiState.isOtpSent) {
        if (uiState.isOtpSent) {
            while (timeLeft > 0) {
                delay(1000)
                timeLeft -= 1
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại")
                    }
                },
                title = { Text("Xác minh số điện thoại", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(22.dp))
            Text(
                "Vui lòng nhập mã xác thực đã gửi đến",
                color = Color.Gray,
                fontSize = 15.sp
            )
            Text(
                phone,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 22.dp)
            )

            // 4 ô nhập OTP
            OutlinedTextField(
                value = uiState.otp,
                onValueChange = { newOtp ->
                    if (newOtp.length <= 6 && newOtp.all(Char::isDigit)) {
                        viewModel.onOtpChanged(newOtp)
                    }
                },
                label = { Text("Nhập mã OTP") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Blue,
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color.Blue,
                    focusedLabelColor = Color.Blue
                )
            )
            Text(
                if (timeLeft > 0) "Gửi lại mã sau: ${timeLeft}s"
                else "Bạn chưa nhận được mã?",
                color = Color.Gray, fontSize = 14.sp
            )
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = {
                    viewModel.verifyOtp(
                        onSuccess = onOtpSuccess,
                        onError = { }
                    )
                },
                enabled = uiState.otp.length == 4 && !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1EA0F9))
            ) {
                Text("Xác nhận", fontWeight = FontWeight.Bold, color = Color.White)
            }
            if (uiState.isLoading) CircularProgressIndicator(modifier = Modifier.padding(top = 14.dp))
            uiState.errorMessage?.let {
                Text(it, color = Color.Red, fontSize = 13.sp, modifier = Modifier.padding(top = 10.dp))
            }
            Spacer(Modifier.height(8.dp))
            if (timeLeft == 0) {
                TextButton(onClick = { onResend?.invoke() }) {
                    Text("Gửi lại mã OTP", color = Color(0xFF1EA0F9), fontSize = 15.sp)
                }
            }
            Spacer(Modifier.height(12.dp))
            TextButton(onClick = onBack) {
                Text("Thay đổi số điện thoại", color = Color(0xFF1EA0F9), fontSize = 15.sp)
            }
        }
    }
}

@Composable
fun OtpBox(char: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = char,
        onValueChange = onValueChange,
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(
            textAlign = TextAlign.Center,
            fontSize = 24.sp
        ),
        modifier = Modifier
            .width(54.dp)
            .height(54.dp)
    )
}
