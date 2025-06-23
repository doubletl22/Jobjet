package com.example.jobjetv1.viewmodel

import android.app.Activity
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.jobjetv1.data.model.AuthUiState
import com.example.jobjetv1.data.prefs.UserPrefs
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit
import com.example.jobjetv1.repository.UserRepository


class AuthViewModel : ViewModel() {
    private val userRepository = UserRepository()
    private var verificationId: String? = null
    var uiState by mutableStateOf(AuthUiState())
        private set
    fun setVerificationId(id: String) {
        verificationId = id
    }
    fun onPhoneChanged(phone: String) { uiState = uiState.copy(phone = phone) }
    fun onOtpChanged(otp: String) { uiState = uiState.copy(otp = otp) }
    fun setError(msg: String?) { uiState = uiState.copy(errorMessage = msg) }
    fun setLoading(b: Boolean) { uiState = uiState.copy(isLoading = b) }

    fun sendOtp(
        activity: Activity,
        onCodeSent: (verificationId: String) -> Unit,
        onFailed: (String) -> Unit
    ) {
        val phone = uiState.phone
        if (!(phone.length == 10 && phone.all { it.isDigit() })) {
            setError("Số điện thoại phải có đúng 10 số.")
            onFailed("Số điện thoại không hợp lệ")
            return
        }
        setLoading(true)
        setError(null)
        val auth = FirebaseAuth.getInstance()
        val firebasePhone = "+84" + phone.substring(1)
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(firebasePhone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) { setLoading(false) }
                override fun onVerificationFailed(e: FirebaseException) {
                    setLoading(false); setError(e.localizedMessage); onFailed(e.localizedMessage ?: "Gửi OTP thất bại")
                }
                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    setLoading(false); this@AuthViewModel.verificationId = verificationId; onCodeSent(verificationId)
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyOtp(
        context: android.content.Context,
        onSuccess: () -> Unit,
        onFailed: (String) -> Unit
    ) {
        val id = verificationId
        val code = uiState.otp
        if (id.isNullOrBlank() || code.length < 4) { onFailed("Thiếu mã xác thực"); return }
        setLoading(true)
        val credential = PhoneAuthProvider.getCredential(id, code)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                setLoading(false)
                if (task.isSuccessful) {
                    UserPrefs.setLoggedIn(context, true)
                    onSuccess()
                } else {
                    setError(task.exception?.localizedMessage ?: "Mã OTP không đúng hoặc đã hết hạn."); onFailed(task.exception?.localizedMessage ?: "Lỗi OTP")
                }
            }
    }
}
