package com.example.jobjetv1.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jobjetv1.data.model.Job
import com.example.jobjetv1.repository.JobsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _jobs = MutableStateFlow<List<Job>>(emptyList())
    val jobs: StateFlow<List<Job>> = _jobs

    fun loadJobs() {
        viewModelScope.launch {
            // Logic phức tạp cũ về gộp dữ liệu không còn cần thiết
            _jobs.value = JobsRepository.getAllJobs()
        }
    }


    fun getJobById(id: String): Job? {
        return _jobs.value.find { it.id == id }
    }
}
