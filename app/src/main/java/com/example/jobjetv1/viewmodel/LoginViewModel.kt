package com.example.jobjetv1.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.example.jobjetv1.repository.AuthRepository
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

// Lớp sealed để quản lý các sự kiện điều hướng hoặc hiển thị lỗi
sealed class LoginEvent {
    data class NavigateToOtp(val verificationId: String) : LoginEvent()
    data class ShowError(val message: String) : LoginEvent()
    object VerificationCompleted : LoginEvent() // Xử lý trường hợp tự động xác thực
    object Idle : LoginEvent()
}

class LoginViewModel : ViewModel() {

    private val authRepository = AuthRepository()

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber = _phoneNumber.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _event = MutableStateFlow<LoginEvent>(LoginEvent.Idle)
    val event = _event.asStateFlow()

    fun signInWithGoogle(idToken: String) {
        _isLoading.value = true
        authRepository.signInWithGoogle(idToken) { isSuccess, exception ->
            _isLoading.value = false
            if (isSuccess) {
                _event.value = LoginEvent.VerificationCompleted
            } else {
                _event.value = LoginEvent.ShowError(exception?.message ?: "Đăng nhập Google thất bại.")
            }
        }
    }

    fun onPhoneNumberChanged(newNumber: String) {
        _phoneNumber.value = newNumber
    }

    fun sendOtp(activity: Activity) {
        if (phoneNumber.value.isBlank() || phoneNumber.value.length < 9) {
            _event.value = LoginEvent.ShowError("Số điện thoại không hợp lệ.")
            return
        }

        _isLoading.value = true
        // Thêm mã vùng +84 cho Việt Nam
        val fullPhoneNumber = "+84${phoneNumber.value}"

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                _isLoading.value = false
                _event.value = LoginEvent.VerificationCompleted
            }

            override fun onVerificationFailed(e: com.google.firebase.FirebaseException) {
                _isLoading.value = false
                _event.value = LoginEvent.ShowError("Lỗi: ${e.localizedMessage}")
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                _isLoading.value = false
                _event.value = LoginEvent.NavigateToOtp(verificationId)
            }
        }
        authRepository.sendOtp(fullPhoneNumber, activity, callbacks)
    }

    fun onEventHandled() {
        _event.value = LoginEvent.Idle
    }
}