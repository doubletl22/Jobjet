package com.example.jobjetv1.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class JobCategory(
    val icon: ImageVector = Icons.Default.Work,
    val label: String,
    val description: String = "",
    val color: Color = Color(0xFF1976D2),
    val jobCount: Int = 0,
    val isPopular: Boolean = false,
    val salary: String = "",
    val keywords: List<String> = emptyList()
)