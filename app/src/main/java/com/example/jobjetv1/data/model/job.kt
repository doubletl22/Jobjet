package com.example.jobjetv1.data.model

import androidx.compose.ui.graphics.Color

data class Job(
    val id: String,
    val title: String,
    val address: String,
    val description: String,
    val wage: String,      // ex: "31,250 VND/Giờ"
    val wageColor: Color,
    val iconRes: Int       // drawable resource id
)
