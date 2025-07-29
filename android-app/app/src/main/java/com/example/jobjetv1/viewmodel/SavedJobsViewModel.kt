package com.example.jobjetv1.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import android.util.Log
import com.example.jobjetv1.data.model.Job

class SavedJobsViewModel : ViewModel() {
    
    private var _savedJobs by mutableStateOf<List<Job>>(emptyList())
    val savedJobs: List<Job> get() = _savedJobs
    
    /**
     * Lưu job vào danh sách saved jobs
     */
    fun saveJob(job: Job) {
        if (!isJobSaved(job.id)) {
            _savedJobs = _savedJobs + job
            Log.d("SavedJobsViewModel", "Saved job: ${job.title}. Total saved jobs: ${_savedJobs.size}")
        } else {
            Log.d("SavedJobsViewModel", "Job ${job.title} already saved")
        }
    }
    
    /**
     * Xóa job khỏi danh sách saved jobs
     */
    fun removeSavedJob(jobId: String) {
        val jobToRemove = _savedJobs.find { it.id == jobId }
        _savedJobs = _savedJobs.filter { it.id != jobId }
        Log.d("SavedJobsViewModel", "Removed job: ${jobToRemove?.title}. Total saved jobs: ${_savedJobs.size}")
    }
    
    /**
     * Kiểm tra job đã được lưu chưa
     */
    fun isJobSaved(jobId: String): Boolean {
        return _savedJobs.any { it.id == jobId }
    }
    
    /**
     * Lấy số lượng jobs đã lưu
     */
    fun getSavedJobsCount(): Int = _savedJobs.size
}
