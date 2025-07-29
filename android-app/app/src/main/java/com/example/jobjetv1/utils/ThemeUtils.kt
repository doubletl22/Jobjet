package com.example.jobjetv1.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

object ThemeUtils {
    // Primary color palette
    val PrimaryBlue = Color(0xFF1976D2)
    val PrimaryBlueLight = Color(0xFF42A5F5)
    val PrimaryBlueDark = Color(0xFF0D47A1)
    
    // Secondary colors
    val SecondaryGreen = Color(0xFF4CAF50)
    val SecondaryOrange = Color(0xFFFF9800)
    val SecondaryPurple = Color(0xFF9C27B0)
    val SecondaryTeal = Color(0xFF009688)
    
    // Background colors
    val BackgroundLight = Color(0xFFF5F5F5)
    val BackgroundCard = Color(0xFFFFFFFF)
    val BackgroundAccent = Color(0xFFF8F9FA)
    
    // Text colors
    val TextPrimary = Color(0xFF212121)
    val TextSecondary = Color(0xFF757575)
    val TextHint = Color(0xFFBDBDBD)
    
    // Status colors
    val SuccessColor = Color(0xFF4CAF50)
    val WarningColor = Color(0xFFFF9800)
    val ErrorColor = Color(0xFFF44336)
    val InfoColor = Color(0xFF2196F3)
    
    // Category colors mapping
    val categoryColors = mapOf(
        "Công nghệ" to Color(0xFF2196F3),
        "Marketing" to Color(0xFFE91E63),
        "Bán hàng" to Color(0xFF4CAF50),
        "Thiết kế" to Color(0xFF9C27B0),
        "Tài chính" to Color(0xFFFF9800),
        "Giáo dục" to Color(0xFF795548),
        "Y tế" to Color(0xFFF44336),
        "Chăm sóc khách hàng" to Color(0xFF009688)
    )    // Category icons mapping
    val categoryIcons = mapOf(
        "Công nghệ" to Icons.Default.Computer,
        "Marketing" to Icons.Default.Campaign,
        "Bán hàng" to Icons.Default.ShoppingCart,
        "Thiết kế" to Icons.Default.Palette,
        "Tài chính" to Icons.Default.AccountBalance,
        "Giáo dục" to Icons.Default.School,
        "Y tế" to Icons.Default.LocalHospital,
        "Chăm sóc khách hàng" to Icons.Default.SupportAgent,
        "Kho bãi" to Icons.Default.Inventory,
        "Nhà hàng" to Icons.Default.Restaurant,
        "Văn phòng" to Icons.Default.BusinessCenter,
        "Giao hàng" to Icons.Default.LocalShipping
    )
    
    fun getCategoryColor(category: String): Color {
        return categoryColors[category] ?: PrimaryBlue
    }
    
    fun getCategoryIcon(category: String): ImageVector {
        return categoryIcons[category] ?: Icons.Default.Work
    }
}
