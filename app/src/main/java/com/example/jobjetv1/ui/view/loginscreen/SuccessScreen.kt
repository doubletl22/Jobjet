package com.example.jobjetv1.ui.view.loginscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.example.jobjetv1.R

@Composable
fun SuccessScreen(onContinue: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(80.dp))
        Box(
            Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(Color(0xFF2196F3)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_check_circle_outline_24),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(54.dp)
            )
        }
        Spacer(Modifier.height(28.dp))
        Text("Đăng nhập thành công!", fontWeight = FontWeight.Bold, fontSize = 22.sp)
        Text("Xin chào, chúc bạn một ngày tốt lành", fontSize = 16.sp, color = Color.Gray)
        Spacer(Modifier.height(38.dp))
        Button(
            onClick = onContinue,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
        ) {
            Text("TIẾP TỤC", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}
