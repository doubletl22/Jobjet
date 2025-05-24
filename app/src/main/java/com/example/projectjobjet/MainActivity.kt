package com.example.projectjobjet // Hoặc package name bạn đã đặt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.projectjobjet.ui.navigation.AppNavigation // Báo đỏ do chưa code file
import com.example.projectjobjet.ui.theme.ProjectJobJetTheme // Báo đỏ do thiếu file

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // ProjectJobJetTheme sẽ áp dụng chủ đề chung cho toàn bộ ứng dụng.
            // Bạn cần định nghĩa ProjectJobJetTheme trong package ui.theme
            ProjectJobJetTheme {
                // Surface là một container cơ bản trong Material Design.
                Surface(
                    modifier = Modifier.fillMaxSize(), // Chiếm toàn bộ không gian màn hình
                    color = MaterialTheme.colorScheme.background // Màu nền từ theme
                ) {
                    // AppNavigation là Composable chính quản lý các màn hình
                    // và luồng điều hướng của ứng dụng.
                    // Bạn cần định nghĩa AppNavigation trong package ui.navigation
                    AppNavigation()
                }
            }
        }
    }
}

// Một Composable xem trước đơn giản (tùy chọn, hữu ích cho việc thiết kế)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ProjectJobJetTheme {
        // Bạn có thể hiển thị AppNavigation hoặc một Composable đơn giản ở đây để xem trước
        // Ví dụ, nếu AppNavigation phức tạp để xem trước ngay:
        // Greeting("Android Preview")
        // Hoặc nếu AppNavigation đã sẵn sàng:
        AppNavigation()
    }
}

/*
// Nếu bạn chưa có AppNavigation, bạn có thể bắt đầu với một Composable đơn giản:
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

// Và gọi nó trong setContent và DefaultPreview:
// setContent {
//     ProjectJobJetTheme {
//         Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
//             Greeting("ProjectJobJet")
//         }
//     }
// }
// @Preview(showBackground = true)
// @Composable
// fun DefaultPreview() {
//     ProjectJobJetTheme {
//         Greeting("Android Preview")
//     }
// }
*/