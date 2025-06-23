package com.example.jobjetv1.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

data class UserProfile(
    val uid: String = "",
    val phoneNumber: String = "",
    val displayName: String = "",
    val photoUrl: String = "",
    val dateOfBirth: String = "",
    val gender: String = "",
    val email: String = "",
    val idNumber: String = "",
    val address: String = "",
    val isLookingForJob: Boolean = true
)

class UserRepository {
    private val usersCollection = FirebaseFirestore.getInstance().collection("users")
    private val currentUser = FirebaseAuth.getInstance().currentUser

    fun createProfileIfNotExists(onComplete: (Boolean) -> Unit) {
        val user = currentUser ?: return onComplete(false)
        val userDocRef = usersCollection.document(user.uid)

        userDocRef.get().addOnSuccessListener { document ->
            if (!document.exists()) {
                // User chưa có profile, tạo mới
                val newUserProfile = hashMapOf(
                    "uid" to user.uid,
                    "phoneNumber" to user.phoneNumber,
                    "displayName" to "Người dùng mới",
                    "photoUrl" to "",
                    "createdAt" to FieldValue.serverTimestamp(),
                    "isLookingForJob" to true
                )
                userDocRef.set(newUserProfile)
                    .addOnSuccessListener { onComplete(true) }
                    .addOnFailureListener { onComplete(false) }
            } else {
                // User đã có profile
                onComplete(true)
            }
        }.addOnFailureListener {
            onComplete(false)
        }
    }

    fun getUserProfile(onSuccess: (UserProfile) -> Unit, onError: (String) -> Unit) {
        val user = currentUser ?: return onError("Không tìm thấy thông tin người dùng")
        
        usersCollection.document(user.uid)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val userProfile = UserProfile(
                        uid = document.getString("uid") ?: "",
                        phoneNumber = document.getString("phoneNumber") ?: "",
                        displayName = document.getString("displayName") ?: "",
                        photoUrl = document.getString("photoUrl") ?: "",
                        dateOfBirth = document.getString("dateOfBirth") ?: "",
                        gender = document.getString("gender") ?: "",
                        email = document.getString("email") ?: "",
                        idNumber = document.getString("idNumber") ?: "",
                        address = document.getString("address") ?: "",
                        isLookingForJob = document.getBoolean("isLookingForJob") ?: true
                    )
                    onSuccess(userProfile)
                } else {
                    onError("Không tìm thấy thông tin người dùng")
                }
            }
            .addOnFailureListener { e ->
                onError(e.localizedMessage ?: "Lỗi khi tải thông tin người dùng")
            }
    }

    fun updateUserProfile(
        updates: Map<String, Any>,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val user = currentUser ?: return onError("Không tìm thấy thông tin người dùng")
        
        usersCollection.document(user.uid)
            .update(updates)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onError(e.localizedMessage ?: "Lỗi khi cập nhật thông tin người dùng")
            }
    }
}
