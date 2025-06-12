package com.example.jobjetv1.repository

import android.app.Activity
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class AuthRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun signInWithGoogle(idToken: String, onResult: (Boolean, Exception?) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        signInWithCredential(credential, onResult)
    }

    fun sendOtp(
        phoneNumber: String,
        activity: Activity,
        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    ) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)       // Số điện thoại để xác thực
            .setTimeout(60L, TimeUnit.SECONDS) // Thời gian chờ
            .setActivity(activity)             // Activity hiện tại
            .setCallbacks(callbacks)           // Callbacks để xử lý kết quả
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyOtp(verificationId: String, code: String, onResult: (Boolean, Exception?) -> Unit) {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithCredential(credential, onResult)
    }

    private fun signInWithCredential(credential: AuthCredential, onResult: (Boolean, Exception?) -> Unit) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Đăng nhập thành công
                    onResult(true, null)
                } else {
                    // Đăng nhập thất bại
                    onResult(false, task.exception)
                }
            }
    }

    fun getCurrentUser() = auth.currentUser

    fun signOut() = auth.signOut()
}