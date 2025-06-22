package com.example.jobjetv1.repository


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

// Trong UserRepository.kt hoặc một UserRepository mới
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
}