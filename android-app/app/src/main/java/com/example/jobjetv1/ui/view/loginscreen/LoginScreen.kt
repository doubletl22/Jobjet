package com.example.jobjetv1.ui.view.loginscreen

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jobjetv1.R
import com.example.jobjetv1.ui.theme.Blue
import com.example.jobjetv1.ui.theme.BlueLight
import com.example.jobjetv1.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: (phone: String, verificationId: String) -> Unit
) {
    val state = viewModel.uiState
    val activity = LocalContext.current as Activity

    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(48.dp))
        Image(
            painter = painterResource(R.drawable.logojobjet), // Make sure this drawable exists
            contentDescription = "Logo",
            modifier = Modifier.size(80.dp)
        )
        Spacer(Modifier.height(12.dp))
        Text("Chào mừng!", fontWeight = FontWeight.Bold, fontSize = 26.sp)
        Text("Đăng nhập để tiếp tục", color = Color.Gray, fontSize = 16.sp)
        Spacer(Modifier.height(30.dp))

        OutlinedTextField(
            value = state.phone,
            onValueChange = {
                if (it.length <= 10 && it.all { c -> c.isDigit() }) {
                    viewModel.onPhoneChanged(it)
                }
            },
            label = { Text("Nhập số điện thoại") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Blue,
                unfocusedBorderColor = Color.Gray,
                cursorColor = Blue,
                focusedLabelColor = Blue,
            )
        )
        Spacer(Modifier.height(18.dp))

        Button(
            onClick = {
                viewModel.sendOtp(
                    activity,
                    onCodeSent = { verificationId ->
                        onLoginSuccess(state.phone, verificationId)
                    },
                    onFailed = { /* ... */ }
                )
            },
            enabled = state.phone.length == 10 && !state.isLoading, // Note: This might be a bug if you also want to enable for "+84" numbers
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Blue,
                contentColor = Color.White,
                disabledContainerColor = BlueLight,
                disabledContentColor = Color.Gray
            )
        ) {
            Text("TIẾP TỤC", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
        } // <-- The incorrect comma that was here is now REMOVED.

        // Now this 'if' statement is a direct child of the Column, which is correct.
        if (state.phone.isNotEmpty() && state.phone.length != 10) {
            Text(
                "Số điện thoại phải có đúng 10 số.",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // This is also a correct child of the Column.
        if (state.isLoading) {
            CircularProgressIndicator(Modifier.padding(top = 16.dp))
        }

        state.errorMessage?.let {
            Text(it, color = Color.Red, modifier = Modifier.padding(top = 12.dp))
        }

        Spacer(Modifier.weight(1f))

        Text(
            "Bằng cách đăng nhập, bạn đồng ý với Điều khoản sử dụng và Chính sách bảo mật của chúng tôi",
            color = Color.Gray,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 10.dp)
        )
    }
}