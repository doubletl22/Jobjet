package com.example.jobjetv1.repository

import com.example.jobjetv1.data.model.Job
import com.example.jobjetv1.data.model.JobPostUiState

interface IJobsRepository {
    suspend fun addJobFromPost(jobPostState: JobPostUiState): Job?
    suspend fun getAllJobs(): List<Job>
    suspend fun getJobById(id: String): Job?
    suspend fun removeJob(jobId: String): Boolean
    suspend fun searchJobs(query: String): List<Job>
}