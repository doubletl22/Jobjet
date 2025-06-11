package com.example.jobjetv1.ui.theme// Hoặc package theme của bạn

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Định nghĩa Bảng màu cho chủ đề SÁNG (Light Theme)
private val LightColorScheme = lightColorScheme(
    primary = BluePrimary,           // Màu chính cho Button, link, các thành phần tương tác
    onPrimary = White,               // Màu chữ/icon trên nền màu chính (chữ trên Button)
    background = White,              // Màu nền chính của toàn bộ ứng dụng
    onBackground = DarkText,         // Màu chữ/icon trên nền chính
    surface = White,                 // Màu nền cho các bề mặt như Card, BottomSheet
    onSurface = DarkText,            // Màu chữ/icon trên các bề mặt
    surfaceVariant = LightGray,      // Màu nền cho các thành phần như ô OutlinedTextField
    onSurfaceVariant = TextGray,     // Màu chữ/icon phụ, ví dụ màu label của TextField
    secondary = BlueDarker,
    tertiary = BluePrimary,
    outline = TextGray,              // Màu viền cho OutlinedTextField
)

/*
// (Tùy chọn) Bạn có thể định nghĩa một DarkColorScheme nếu muốn hỗ trợ trong tương lai
private val DarkColorScheme = darkColorScheme(
    primary = BluePrimary,
    background = Color(0xFF121212),
    onBackground = White,
    surface = Color(0xFF1E1E1E),
    onSurface = White
)
*/

@Composable
fun JobJetTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // Mặc định sẽ theo chủ đề của hệ thống
    content: @Composable () -> Unit
) {
    // Chúng ta sẽ luôn dùng LightColorScheme trong trường hợp này
    // để đảm bảo theme luôn là màu trắng.
    val colorScheme = LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb() // Bạn có thể đổi thành White nếu muốn
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Typography được định nghĩa trong Type.kt
        content = content
    )
}