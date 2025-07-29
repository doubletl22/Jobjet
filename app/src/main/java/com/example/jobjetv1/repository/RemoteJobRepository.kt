package com.example.jobjetv1.repository

import android.util.Log
import com.example.jobjetv1.data.model.Job
import com.example.jobjetv1.data.model.JobPostUiState
import com.example.jobjetv1.network.RetrofitClient

object JobsRepository { // Có thể giữ tên cũ hoặc đổi
    private val apiService = RetrofitClient.instance

    suspend fun getAllJobs(): List<Job> {
        return try {
            apiService.getAllJobs()
        } catch (e: Exception) {
            Log.e("JobsRepository", "Lỗi tải công việc: ${e.message}")
            emptyList() // Trả về danh sách rỗng nếu lỗi
        }
    }

    suspend fun addJobFromPost(jobPostState: JobPostUiState): Job? {
        return try {
            apiService.addJob(jobPostState)
        } catch (e: Exception) {
            Log.e("JobsRepository", "Lỗi đăng công việc: ${e.message}")
            null
        }
    }
}