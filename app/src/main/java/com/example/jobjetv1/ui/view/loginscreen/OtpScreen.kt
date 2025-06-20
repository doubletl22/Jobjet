package com.example.jobjetv1.ui.view.loginscreen

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jobjetv1.viewmodel.OtpViewModel

@Composable
fun OtpScreen(
    viewModel: OtpViewModel,
    onOtpSuccess: () -> Unit,
    onChangePhone: () -> Unit,
) {
    val state = viewModel.uiState
    val activity = LocalContext.current as Activity

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(40.dp))
        Text("Xác minh số điện thoại", fontWeight = FontWeight.Bold, fontSize = 24.sp)
        Text("Vui lòng nhập mã xác thực đã gửi đến", fontSize = 15.sp, color = Color.Gray)
        Text(state.phone, fontSize = 16.sp, color = Color.Black, fontWeight = FontWeight.Medium, modifier = Modifier.padding(top = 2.dp, bottom = 18.dp))

        OutlinedTextField(
            value = state.otp,
            onValueChange = {
                if (it.length <= 6 && it.all { c -> c.isDigit() }) viewModel.onOtpChanged(it)
            },
            label = { Text("Nhập mã OTP") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )

        // Đếm ngược gửi lại mã
        if (viewModel.resendSeconds > 0) {
            Text("Gửi lại mã sau: ${viewModel.resendSeconds}s", color = Color.Gray, fontSize = 14.sp, modifier = Modifier.padding(bottom = 14.dp))
        } else {
            TextButton(onClick = {
                viewModel.resendOtp(activity)
            }) {
                Text("Gửi lại mã", color = Color(0xFF2196F3), fontWeight = FontWeight.Medium)
            }
        }

        Button(
            onClick = {
                viewModel.verifyOtp(onSuccess = onOtpSuccess)
            },
            enabled = state.otp.length in 4..6 && !state.isLoading,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3), contentColor = Color.White)
        ) {
            Text("XÁC NHẬN", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        TextButton(onClick = onChangePhone, modifier = Modifier.padding(top = 8.dp)) {
            Text("Thay đổi số điện thoại", color = Color(0xFF2196F3), fontSize = 15.sp)
        }
        Spacer(Modifier.height(8.dp))
        if (state.isLoading) CircularProgressIndicator()
        state.errorMessage?.let { Text(it, color = Color.Red, fontSize = 14.sp, modifier = Modifier.padding(top = 4.dp)) }
        Spacer(Modifier.height(24.dp))
        Text(
            "Bằng cách đăng nhập, bạn đồng ý với Điều khoản sử dụng và Chính sách bảo mật của chúng tôi",
            color = Color.Gray, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}
