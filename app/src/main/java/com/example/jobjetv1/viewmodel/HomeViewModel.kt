package com.example.jobjetv1.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.jobjetv1.data.model.Job
import com.example.jobjetv1.repository.JobsRepository

class HomeViewModel : ViewModel() {
    
    // Jobs từ repository - sử dụng derivedStateOf để Compose track thay đổi
    val jobs: List<Job> by derivedStateOf { JobsRepository.allJobs }
    
    /**
     * Tìm kiếm jobs
     */
    fun searchJobs(query: String): List<Job> {
        return JobsRepository.searchJobs(query)
    }
    
    /**
     * Lấy job theo ID
     */
    fun getJobById(id: String): Job? {
        return JobsRepository.getJobById(id)
    }
    
    /**
     * Refresh jobs (có thể dùng cho pull-to-refresh)
     */
    fun refreshJobs() {
        // In real app, this would call API to refresh data
        // For now, jobs are automatically updated via JobsRepository
    }
    
    /**
     * Force recomposition for debugging
     */
    fun getJobsCount(): Int = jobs.size
    
    /**
     * Debug function to get latest job
     */
    fun getLatestJob(): Job? = jobs.firstOrNull()
    
    /**
     * Observer để theo dõi khi jobs thay đổi
     */
    @Composable
    fun ObserveJobsChanges() {
        LaunchedEffect(jobs.size) {
            // Jobs count changed, UI will automatically update
        }
    }
    
    /**
     * Test function để thêm job để test
     */
    fun addTestJob(): Job {
        return JobsRepository.addTestJob()
    }
}
