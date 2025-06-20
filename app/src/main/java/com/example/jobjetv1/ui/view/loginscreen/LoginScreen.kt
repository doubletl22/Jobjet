package com.example.jobjetv1.ui.view.loginscreen

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jobjetv1.viewmodel.OtpViewModel
import com.example.jobjetv1.R

@Composable
fun LoginScreen(
    viewModel: OtpViewModel,
    onLoginSuccess: () -> Unit,
    onGoogleLogin: () -> Unit
) {
    val state = viewModel.uiState
    val activity = LocalContext.current as Activity

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(36.dp))
        Image(
            painter = painterResource(id = R.drawable.image1), // Thay bằng logo của bạn
            contentDescription = null,
            modifier = Modifier.height(72.dp)
        )
        Spacer(Modifier.height(18.dp))
        Text("Chào mừng!", fontWeight = FontWeight.Bold, fontSize = 28.sp)
        Text("Đăng nhập để tiếp tục", fontSize = 16.sp, color = Color.Gray, modifier = Modifier.padding(vertical = 8.dp))

        OutlinedTextField(
            value = state.phone,
            onValueChange = { viewModel.onPhoneChanged(it) },
            label = { Text("Nhập số điện thoại của bạn") },
            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            singleLine = true
        )

        Spacer(Modifier.height(18.dp))
        Button(
            onClick = {
                viewModel.sendOtp(
                    activity,
                    onCodeSent = onLoginSuccess
                )
            },
            enabled = state.phone.length >= 10 && !state.isLoading,
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text("TIẾP TỤC", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
        Spacer(Modifier.height(16.dp))

        Text(
            "Hoặc",
            color = Color.Gray,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Spacer(Modifier.height(16.dp))

        OutlinedButton(
            onClick = onGoogleLogin,
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            Text("Đăng nhập với Google")
        }
        Spacer(Modifier.height(16.dp))
        Text(
            "Bằng cách đăng nhập, bạn đồng ý với Điều khoản sử dụng và Chính sách bảo mật của chúng tôi",
            color = Color.Gray,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        if (state.isLoading) CircularProgressIndicator(Modifier.padding(top = 16.dp))
        state.errorMessage?.let { Text(it, color = Color.Red, modifier = Modifier.padding(top = 12.dp)) }
    }
}
