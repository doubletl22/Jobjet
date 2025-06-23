package com.example.jobjetv1.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.jobjetv1.repository.UserProfile
import com.example.jobjetv1.repository.UserRepository

data class ProfileUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userProfile: UserProfile? = null
)

class ProfileViewModel : ViewModel() {
    private val userRepository = UserRepository()
    
    var uiState by mutableStateOf(ProfileUiState())
        private set

    init {
        loadUserProfile()
    }

    private fun setLoading(isLoading: Boolean) {
        uiState = uiState.copy(isLoading = isLoading)
    }

    private fun setError(message: String?) {
        uiState = uiState.copy(errorMessage = message)
    }

    private fun setUserProfile(profile: UserProfile) {
        uiState = uiState.copy(userProfile = profile)
    }

    fun loadUserProfile() {
        setLoading(true)
        setError(null)

        userRepository.getUserProfile(
            onSuccess = { profile ->
                setUserProfile(profile)
                setLoading(false)
            },
            onError = { error ->
                setError(error)
                setLoading(false)
            }
        )
    }

    fun updateUserProfile(
        updates: Map<String, Any>,
        onSuccess: () -> Unit
    ) {
        setLoading(true)
        setError(null)

        userRepository.updateUserProfile(
            updates = updates,
            onSuccess = {
                loadUserProfile() // Reload profile after update
                onSuccess()
            },
            onError = { error ->
                setError(error)
                setLoading(false)
            }
        )
    }
}
