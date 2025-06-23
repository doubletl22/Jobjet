package com.example.jobjetv1.viewmodel

import android.net.Uri
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.jobjetv1.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*

data class ProfileInfoUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)

class ProfileInfoViewModel : ViewModel() {
    private val userRepository = UserRepository()
    private val storage = FirebaseStorage.getInstance()
    
    var uiState by mutableStateOf(ProfileInfoUiState())
        private set
    
    var fullName by mutableStateOf("")
        private set
    
    var dateOfBirth by mutableStateOf("")
        private set
    
    var gender by mutableStateOf("Nam")
        private set
    
    var phoneNumber by mutableStateOf(FirebaseAuth.getInstance().currentUser?.phoneNumber ?: "")
        private set
    
    var email by mutableStateOf("")
        private set
    
    var idNumber by mutableStateOf("")
        private set
    
    var address by mutableStateOf("")
        private set
    
    private var selectedImageUri by mutableStateOf<Uri?>(null)

    private fun setLoading(isLoading: Boolean) {
        uiState = uiState.copy(isLoading = isLoading)
    }

    private fun setError(message: String?) {
        uiState = uiState.copy(errorMessage = message)
    }

    private fun setSuccess(isSuccess: Boolean) {
        uiState = uiState.copy(isSuccess = isSuccess)
    }

    fun logout(onLogoutSuccess: () -> Unit) {
        FirebaseAuth.getInstance().signOut()
        onLogoutSuccess()
    }

    fun onFullNameChanged(value: String) {
        fullName = value
    }

    fun onDateOfBirthChanged(value: String) {
        dateOfBirth = value
    }

    fun onGenderChanged(value: String) {
        gender = value
    }

    fun onEmailChanged(value: String) {
        email = value
    }

    fun onIdNumberChanged(value: String) {
        idNumber = value
    }

    fun onAddressChanged(value: String) {
        address = value
    }

    fun onImageSelected(uri: Uri) {
        selectedImageUri = uri
    }

    fun saveProfile(onSuccess: () -> Unit) {
        if (!validateFields()) {
            return
        }

        setLoading(true)
        setError(null)
        
        val user = FirebaseAuth.getInstance().currentUser ?: run {
            setError("Không tìm thấy thông tin người dùng")
            setLoading(false)
            return
        }

        // First upload image if selected
        selectedImageUri?.let { uri ->
            val imageRef = storage.reference.child("profile_images/${user.uid}")
            imageRef.putFile(uri)
                .continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let { throw it }
                    }
                    imageRef.downloadUrl
                }
                .addOnSuccessListener { downloadUri ->
                    saveUserData(downloadUri.toString(), onSuccess)
                }
                .addOnFailureListener { e ->
                    setError("Lỗi tải ảnh lên: ${e.localizedMessage}")
                    setLoading(false)
                }
        } ?: saveUserData(null, onSuccess)
    }

    private fun validateFields(): Boolean {
        if (fullName.isBlank()) {
            setError("Vui lòng nhập họ và tên")
            return false
        }
        if (dateOfBirth.isBlank()) {
            setError("Vui lòng chọn ngày sinh")
            return false
        }
        if (email.isBlank()) {
            setError("Vui lòng nhập email")
            return false
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            setError("Email không hợp lệ")
            return false
        }
        if (idNumber.isBlank()) {
            setError("Vui lòng nhập số CMND/CCCD")
            return false
        }
        if (address.isBlank()) {
            setError("Vui lòng nhập địa chỉ")
            return false
        }
        return true
    }

    private fun saveUserData(photoUrl: String?, onSuccess: () -> Unit) {
        setLoading(true)
        val user = FirebaseAuth.getInstance().currentUser ?: return
        val userData = hashMapOf(
            "uid" to user.uid,
            "phoneNumber" to phoneNumber,
            "displayName" to fullName,
            "dateOfBirth" to dateOfBirth,
            "gender" to gender,
            "email" to email,
            "idNumber" to idNumber,
            "address" to address,
            "photoUrl" to (photoUrl ?: ""),
            "createdAt" to com.google.firebase.Timestamp.now(),
            "isLookingForJob" to true
        )

        FirebaseFirestore.getInstance()
            .collection("users")
            .document(user.uid)
            .set(userData)
            .addOnSuccessListener {
                setSuccess(true)
                setLoading(false)
                onSuccess()
            }
            .addOnFailureListener { e ->
                setError("Lỗi lưu thông tin: ${e.localizedMessage}")
                setLoading(false)
            }
    }
}
