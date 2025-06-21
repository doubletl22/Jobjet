package com.example.jobjetv1.ui.view.loginscreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.*
import com.example.jobjetv1.ui.theme.Blue
import com.example.jobjetv1.ui.theme.BlueLight
import com.example.jobjetv1.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpScreen(
    viewModel: AuthViewModel,
    verificationId: String,
    onOtpSuccess: () -> Unit
) {
    val state = viewModel.uiState
    val context = LocalContext.current

    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(48.dp))
        Text("Xác minh số điện thoại", fontWeight = FontWeight.Bold, fontSize = 22.sp)
        Text("Vui lòng nhập mã xác thực đã gửi đến", color = Color.Gray, fontSize = 15.sp)
        Spacer(Modifier.height(18.dp))
        OutlinedTextField(
            value = state.otp,
            onValueChange = { if (it.length <= 6 && it.all(Char::isDigit)) viewModel.onOtpChanged(it) },
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
        Spacer(Modifier.height(18.dp))
        Button(
            onClick = {
                viewModel.setVerificationId(verificationId)
                viewModel.verifyOtp(
                    context,
                    onSuccess = onOtpSuccess,
                    onFailed = { }
                )
            },
            enabled = state.otp.length in 4..6 && !state.isLoading,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Blue, // Màu nền của nút khi kích hoạt
                contentColor = Color.White, // Màu văn bản/biểu tượng trên nút khi kích hoạt
                disabledContainerColor = BlueLight, // Màu nền của nút khi bị vô hiệu hóa
                disabledContentColor = Color.Gray )
        ) {
            Text("XÁC NHẬN", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
        }
        if (state.isLoading) CircularProgressIndicator(Modifier.padding(top = 16.dp))
        state.errorMessage?.let { Text(it, color = Color.Red, modifier = Modifier.padding(top = 12.dp)) }
    }
}
