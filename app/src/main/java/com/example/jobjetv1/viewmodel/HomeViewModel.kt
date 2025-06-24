package com.example.jobjetv1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jobjetv1.data.model.Job
import com.example.jobjetv1.repository.JobsRepositoryFirestore
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

    fun getJobById(id: String): Job? {
        return _jobs.value.find { it.id == id }
    }
}
