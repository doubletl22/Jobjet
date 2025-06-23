package com.example.jobjetv1.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import android.util.Log
import com.example.jobjetv1.data.model.JobPostUiState
import com.example.jobjetv1.data.model.WorkType
import com.example.jobjetv1.repository.JobsRepositoryFirestore

data class ValidationState(
    val companyNameError: String? = null,
    val jobTitleError: String? = null,
    val quantityError: String? = null,
    val wageError: String? = null,
    val descriptionError: String? = null,
    val requirementError: String? = null,
    val addressError: String? = null,
    val contactNameError: String? = null,
    val contactEmailError: String? = null,
    val contactPhoneError: String? = null
)

class RecruitmentViewModel : ViewModel() {
    
    var jobPostState by mutableStateOf(JobPostUiState())
        private set
    
    var validationState by mutableStateOf(ValidationState())
        private set
    
    var isSubmitting by mutableStateOf(false)
        private set
    
    var submitSuccess by mutableStateOf(false)
        private set

    fun updateJobPost(update: JobPostUiState.() -> JobPostUiState) {
        val oldState = jobPostState
        jobPostState = jobPostState.update()
        Log.d("RecruitmentViewModel", "Job post updated: ${oldState.jobTitle} -> ${jobPostState.jobTitle}")
        // Clear validation error khi user edit field
        clearFieldError(jobPostState)
    }
    
    fun validateAndSubmit(onSuccess: (JobPostUiState) -> Unit, onError: (String) -> Unit) {
        Log.d("RecruitmentViewModel", "validateAndSubmit called")
        Log.d("RecruitmentViewModel", "Current state: $jobPostState")
        val validation = validateForm(jobPostState)
        validationState = validation
        
        Log.d("RecruitmentViewModel", "Validation result: $validation")
        
        if (isFormValid(validation)) {
            Log.d("RecruitmentViewModel", "Form is valid, starting submission")
            isSubmitting = true
            submitJobPost(jobPostState, onSuccess, onError)
        } else {
            Log.d("RecruitmentViewModel", "Form validation failed")
            onError("Vui lòng kiểm tra lại thông tin đã nhập")
        }
    }
    
    private fun validateForm(state: JobPostUiState): ValidationState {
        return ValidationState(
            companyNameError = if (state.companyName.isBlank()) "Tên công ty không được để trống" else null,
            jobTitleError = if (state.jobTitle.isBlank()) "Tên công việc không được để trống" else null,
            quantityError = if (state.quantity.isBlank() || state.quantity.toIntOrNull() == null || state.quantity.toInt() <= 0) 
                "Số lượng phải là số dương" else null,
            wageError = if (state.wage.isBlank()) "Mức lương không được để trống" else null,
            descriptionError = if (state.description.isBlank()) "Mô tả công việc không được để trống" else null,
            requirementError = if (state.requirement.isBlank()) "Yêu cầu ứng viên không được để trống" else null,
            addressError = if (state.address.isBlank()) "Địa chỉ không được để trống" else null,
            contactNameError = if (state.contactName.isBlank()) "Tên người liên hệ không được để trống" else null,
            contactEmailError = if (state.contactEmail.isBlank()) "Email không được để trống" 
                else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(state.contactEmail).matches()) 
                    "Email không hợp lệ" else null,            contactPhoneError = if (state.contactPhone.isBlank()) "Số điện thoại không được để trống"
                else if (state.contactPhone.length < 6) "Số điện thoại phải có ít nhất 6 số" else null
        )
    }
    
    private fun isFormValid(validation: ValidationState): Boolean {
        val isValid = validation.companyNameError == null &&
               validation.jobTitleError == null &&
               validation.quantityError == null &&
               validation.wageError == null &&
               validation.descriptionError == null &&
               validation.requirementError == null &&
               validation.addressError == null &&
               validation.contactNameError == null &&
               validation.contactEmailError == null &&
               validation.contactPhoneError == null
        
        Log.d("RecruitmentViewModel", "Form validation result: $isValid")
        return isValid
    }
      private fun clearFieldError(state: JobPostUiState) {
        // Clear specific field errors when user starts typing
        validationState = validationState.copy(
            companyNameError = if (state.companyName.isNotBlank()) null else validationState.companyNameError,
            jobTitleError = if (state.jobTitle.isNotBlank()) null else validationState.jobTitleError
            // ... other fields
        )
    }
    
    private fun submitJobPost(
        state: JobPostUiState, 
        onSuccess: (JobPostUiState) -> Unit,
        onError: (String) -> Unit
    ) {
        Log.d("RecruitmentViewModel", "submitJobPost called")
        // Simulate API call delay
        viewModelScope.launch {
            try {
                Log.d("RecruitmentViewModel", "Starting delay...")
                delay(2000) // Simulate network delay
                
                Log.d("RecruitmentViewModel", "Adding job to repository...")                // Thêm job vào repository - QUAN TRỌNG: Đây là điểm kết nối
                val newJob = JobsRepositoryFirestore.addJobFromPost(state)
                
                if (newJob != null) {
                    Log.d("RecruitmentViewModel", "Job added successfully: ${newJob.title}")
                    isSubmitting = false
                    submitSuccess = true
                    onSuccess(state)
                } else {
                    Log.d("RecruitmentViewModel", "Failed to add job")
                    isSubmitting = false
                    onError("Đăng bài thất bại: Không thể thêm công việc")
                }
                
            } catch (e: Exception) {
                Log.e("RecruitmentViewModel", "Error submitting job", e)
                isSubmitting = false
                onError("Đăng bài thất bại: ${e.message}")
            }
        }
    }
    
    fun resetForm() {
        jobPostState = JobPostUiState()
        validationState = ValidationState()
        isSubmitting = false
        submitSuccess = false
    }
}
