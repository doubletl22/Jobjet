package com.example.jobjetv1.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.jobjetv1.data.model.Job
import com.example.jobjetv1.repository.JobsRepositoryFirestore

class HomeViewModel : ViewModel() {
    
    // Jobs từ repository - sử dụng derivedStateOf để Compose track thay đổi
    val jobs: List<Job> by derivedStateOf { JobsRepositoryFirestore.allJobs }
    
    /**
     * Tìm kiếm jobs
     */
    fun searchJobs(query: String): List<Job> {
        // Implement search if needed, currently not implemented in Firestore repo
        return jobs.filter { job ->
            job.title.contains(query, ignoreCase = true) ||
            job.address.contains(query, ignoreCase = true) ||
            job.description.contains(query, ignoreCase = true)
        }
    }
    
    /**
     * Lấy job theo ID
     */
    fun getJobById(id: String): Job? {
        return JobsRepositoryFirestore.getJobById(id)
    }
    
    /**
     * Refresh jobs (có thể dùng cho pull-to-refresh)
     */
    fun refreshJobs() {
        // Firestore updates automatically handled by listener
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
        // Not implemented in Firestore repo
        throw UnsupportedOperationException("addTestJob not supported in Firestore repository")
    }
}
