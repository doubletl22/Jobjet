package com.example.jobjetv1.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jobjetv1.data.model.Job
import com.example.jobjetv1.repository.JobsRepository
import com.example.jobjetv1.repository.JobsRepositoryFirestore
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _jobs = MutableStateFlow<List<Job>>(emptyList())
    val jobs: StateFlow<List<Job>> = _jobs

    fun loadJobsFromFirebase() {
        viewModelScope.launch {
            val jobs = JobsRepositoryFirestore.getAllJobs()
            _jobs.value = jobs
        }
    }
    fun loadJobs() {
        viewModelScope.launch {
            Log.d("HomeViewModel", "Loading jobs from all sources...")

            // Tải song song dữ liệu từ hai nguồn
            val localJobsDeferred = async { JobsRepository.allJobs }
            val firestoreJobsDeferred = async { JobsRepositoryFirestore.getAllJobs() }

            // Đợi kết quả từ cả hai nguồn
            val localJobs = localJobsDeferred.await()
            val firestoreJobs = firestoreJobsDeferred.await()

            // Kết hợp hai danh sách và loại bỏ các job trùng lặp bằng ID
            val combinedJobs = (localJobs + firestoreJobs).distinctBy { it.id }

            _jobs.value = combinedJobs
            Log.d("HomeViewModel", "Loaded ${localJobs.size} local jobs and ${firestoreJobs.size} Firestore jobs. Total unique jobs: ${combinedJobs.size}")
        }
    }


    fun getJobById(id: String): Job? {
        return _jobs.value.find { it.id == id }
    }
}
