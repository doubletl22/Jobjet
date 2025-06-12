package com.example.jobjetv1.viewmodel

import androidx.lifecycle.ViewModel
import com.example.jobjetv1.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

// Sự kiện cho màn hình OTP
sealed class OtpEvent {
    object NavigateToSuccess : OtpEvent()
    data class ShowError(val message: String) : OtpEvent()
    object Idle : OtpEvent()
}

class OtpViewModel(private val verificationId: String) : ViewModel() {

    private val authRepository = AuthRepository()

    private val _otpCode = MutableStateFlow("")
    val otpCode = _otpCode.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _event = MutableStateFlow<OtpEvent>(OtpEvent.Idle)
    val event = _event.asStateFlow()

    fun onOtpCodeChanged(newCode: String) {
        if (newCode.length <= 6) {
            _otpCode.value = newCode
        }
    }

    fun verifyOtp() {
        if (otpCode.value.length < 6) {
            _event.value = OtpEvent.ShowError("Vui lòng nhập đủ 6 chữ số.")
            return
        }

        _isLoading.value = true
        authRepository.verifyOtp(verificationId, otpCode.value) { isSuccess, exception ->
            _isLoading.value = false
            if (isSuccess) {
                _event.value = OtpEvent.NavigateToSuccess
            } else {
                _event.value = OtpEvent.ShowError(exception?.message ?: "Mã OTP không hợp lệ.")
            }
        }
    }

    fun onEventHandled() {
        _event.value = OtpEvent.Idle
    }
}