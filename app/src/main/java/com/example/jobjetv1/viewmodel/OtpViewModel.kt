package com.example.jobjetv1.viewmodel

import android.app.Activity
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jobjetv1.data.model.AuthUiState
import com.example.jobjetv1.data.prefs.UserPrefs
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

class OtpViewModel : ViewModel() {
    var uiState by mutableStateOf(AuthUiState())
        private set

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var resendJob: Job? = null

    fun onPhoneChanged(newPhone: String) {
        uiState = uiState.copy(phone = newPhone)
    }
    fun onOtpChanged(newOtp: String) {
        uiState = uiState.copy(otp = newOtp)
    }

    fun sendOtp(
        activity: Activity,
        onCodeSent: (() -> Unit)? = null,
        onFailed: ((String) -> Unit)? = null
    ) {
        uiState = uiState.copy(isLoading = true, errorMessage = null)
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(uiState.phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    // Auto complete (hiếm khi dùng ở VN)
                }
                override fun onVerificationFailed(e: FirebaseException) {
                    uiState = uiState.copy(isLoading = false, errorMessage = e.localizedMessage)
                    onFailed?.invoke(e.localizedMessage ?: "Gửi OTP thất bại")
                }
                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    uiState = uiState.copy(
                        isOtpSent = true,
                        isLoading = false,
                        verificationId = verificationId,
                        errorMessage = null
                    )
                    onCodeSent?.invoke()
                    startResendTimer()
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    // Xác thực OTP
    fun verifyOtp(
        onSuccess: () -> Unit,
        onError: ((String) -> Unit)? = null
    ) {
        val id = uiState.verificationId
        val code = uiState.otp
        if (id.isNullOrBlank() || code.length < 4) {
            onError?.invoke("Thiếu mã xác thực")
            return
        }
        uiState = uiState.copy(isLoading = true, errorMessage = null)
        val credential = PhoneAuthProvider.getCredential(id, code)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                uiState = uiState.copy(isLoading = false)
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    val msg = task.exception?.localizedMessage ?: "Mã OTP không đúng hoặc đã hết hạn."
                    uiState = uiState.copy(errorMessage = msg)
                    onError?.invoke(msg)
                }
            }
    }

    // Đếm ngược gửi lại OTP
    var resendSeconds by mutableStateOf(59)
        private set

    private fun startResendTimer() {
        resendJob?.cancel()
        resendSeconds = 59
        resendJob = CoroutineScope(Dispatchers.Main).launch {
            while (resendSeconds > 0) {
                delay(1000)
                resendSeconds--
            }
        }
    }

    fun resendOtp(
        activity: Activity,
        onResend: (() -> Unit)? = null
    ) {
        sendOtp(activity, onCodeSent = onResend)
    }
    fun onLoginSuccess(context: Context, onNavigateHome: () -> Unit) {
        viewModelScope.launch {
            UserPrefs.setLoggedIn(context, true)
            // Lưu thêm thông tin khác nếu cần, ví dụ số điện thoại
            // UserPrefs.setPhone(context, uiState.phone)
            onNavigateHome()
        }
    }

}
