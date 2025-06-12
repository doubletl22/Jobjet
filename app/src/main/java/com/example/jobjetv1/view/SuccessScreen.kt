package com.example.jobjetv1.view // Đảm bảo package này khớp với dự án của bạn

// --- THÊM CÁC IMPORT NÀY VÀO ---
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.jobjetv1.R // Thay R bằng package của bạn nếu khác

@Composable
fun SuccessScreen(navController: NavController) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Đảm bảo bạn đã có file `ic_success_check.xml` hoặc `.png` trong thư mục res/drawable
            Image(
                painter = painterResource(id = R.drawable.images),
                contentDescription = "Success Check",
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text("Đăng nhập thành công!", fontSize = 24.sp)
            Text("Xin chào, chúc bạn một ngày tốt lành", color = Color.Gray, modifier = Modifier.padding(top = 8.dp))

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = {
                    // Điều hướng đến màn hình danh sách công việc
                    navController.navigate("job_list") {
                        popUpTo("success") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth() // Sửa lỗi: đây nên là fillMaxWidth
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("TIẾP TỤC")
            }
        }
    }
}