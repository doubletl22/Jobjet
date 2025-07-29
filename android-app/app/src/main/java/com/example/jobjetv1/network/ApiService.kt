package com.example.jobjetv1.network



import com.example.jobjetv1.data.model.Job
import com.example.jobjetv1.data.model.JobPostUiState
import com.example.jobjetv1.repository.UserProfile
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("api/jobs")
    suspend fun getAllJobs(): List<Job>

    @POST("api/jobs")
    suspend fun addJob(@Body jobPost: JobPostUiState): Job

    @GET("api/users/{uid}")
    suspend fun getUserProfile(@Path("uid") userId: String): UserProfile

}