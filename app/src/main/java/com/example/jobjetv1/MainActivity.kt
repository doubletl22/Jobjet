package com.example.jobjetv1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.jobjetv1.navigation.AppNavigation
import com.example.jobjetv1.ui.theme.JobJetTheme // Đảm bảo bạn đã tạo Theme này
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JobJetTheme { // Áp dụng theme của bạn
                // --- KẾT NỐI FIREBASE TẠI ĐÂY ---
                // Khởi tạo FirebaseApp.initializeApp(this) nếu cần thiết
                AppNavigation()
            }
        }
    }
}