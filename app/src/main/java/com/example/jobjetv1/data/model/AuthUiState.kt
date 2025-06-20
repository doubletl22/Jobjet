package com.example.jobjetv1.data.model

data class AuthUiState(
    val phone: String = "",
    val otp: String = "",
    val isOtpSent: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val verificationId: String? = null
)
