package com.example.jobjetv1.repository

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.example.jobjetv1.data.model.Job
import com.example.jobjetv1.data.model.JobPostUiState
import com.example.jobjetv1.R
import com.google.firebase.Timestamp

object JobsRepository {

    object JobsRepository {
        val allJobs: List<Job> = listOf() // Replace with Firestore or mock data

        fun getJobById(id: String): Job? {
            return allJobs.find { it.id == id }
        }
    }
    // Sử dụng mutableStateOf để Compose có thể reactive
    private var _allJobs by mutableStateOf(initializeSampleJobs())
    val allJobs: List<Job> get() = _allJobs
    
    private var nextJobId = 10 // Start from 10 since sample jobs use 1-9
    
    private fun initializeSampleJobs() = listOf(
        Job("1", "Out Layer", "Global City Quận 9", "Setup, điều chỉnh sân khấu.", "50,000 VND/Giờ", Color(0xFF43A047), R.drawable.outline_tools_pliers_wire_stripper_24, postedDate = Timestamp.now()),
        Job("2", "Kho Shoppe", "221/4 Phan Huy Ích, P14, Gò Vấp", "Phân loại bưu kiện, sắp xếp hàng hóa.", "31,250 VND/Giờ", Color(0xFF43A047), R.drawable.outline_warehouse_24, postedDate = Timestamp.now()),
        Job("3", "Kho Shoppe", "618/1B Âu cơ, P10, Tân Bình", "Phân loại bưu kiện, sắp xếp hàng hóa.", "31,250 VND/Giờ", Color(0xFF43A047), R.drawable.outline_warehouse_24, postedDate = Timestamp.now()),
        Job("4", "Nhà hàng", "202 Hoàng Văn Thụ, Phú Nhuận", "Phân loại bưu kiện, sắp xếp hàng hóa.", "25,000 VND/Giờ", Color(0xFF43A047), R.drawable.outline_restaurant_24, postedDate = Timestamp.now()),
        Job("5", "Kho Shoppe", "123 Nguyễn Hữu Tiến, Tân Phú", "Phân loại bưu kiện, sắp xếp hàng hóa.", "31,250 VND/Giờ", Color(0xFF43A047), R.drawable.outline_warehouse_24, postedDate = Timestamp.now()),
    )
    
    /**
     * Thêm job mới từ JobPostUiState
     */    fun addJobFromPost(jobPost: JobPostUiState): Job {
        val newJob = Job(
            id = nextJobId.toString(),
            title = jobPost.jobTitle,
            address = jobPost.address,
            description = jobPost.description,
            wage = "${jobPost.wage} ${jobPost.wageUnit}",
            wageColor = Color(0xFF43A047), // Green color for new jobs
            iconRes = getIconForJobType(jobPost.jobTitle),
            postedDate = Timestamp.now()
        )

        // Add to beginning of list (newest jobs first)
        val oldCount = _allJobs.size
        _allJobs = listOf(newJob) + _allJobs
        nextJobId++

        return newJob
    }
    
    /**
     * Thêm job test để debug
     */
    fun addTestJob(): Job {
        val testJob = Job(
            id = nextJobId.toString(),
            title = "Test Job ${nextJobId}",
            address = "Test Address",
            description = "This is a test job for debugging",
            wage = "30,000 VND/Giờ",
            wageColor = Color(0xFF43A047),
            iconRes = R.drawable.outline_warehouse_24,
            postedDate = Timestamp.now()
        )
        
        val oldCount = _allJobs.size
        _allJobs = listOf(testJob) + _allJobs
        nextJobId++        
        return testJob
    }
    
    /**
     * Lấy job theo ID
     */
    fun getJobById(id: String): Job? {
        return _allJobs.find { it.id == id }
    }
    
    /**
     * Lấy danh sách jobs mới nhất
     */
    fun getRecentJobs(limit: Int = 10): List<Job> {
        return _allJobs.take(limit)
    }
    
    /**
     * Xóa job (chỉ admin hoặc người đăng)
     */
    fun removeJob(jobId: String): Boolean {
        val originalSize = _allJobs.size
        _allJobs = _allJobs.filter { it.id != jobId }
        return _allJobs.size < originalSize
    }
    
    /**
     * Tìm kiếm jobs
     */
    fun searchJobs(query: String): List<Job> {
        if (query.isBlank()) return _allJobs
        
        return _allJobs.filter { job ->
            job.title.contains(query, ignoreCase = true) ||
            job.address.contains(query, ignoreCase = true) ||
            job.description.contains(query, ignoreCase = true)
        }
    }
    
    /**
     * Xác định icon cho job dựa trên title
     */
    private fun getIconForJobType(jobTitle: String): Int {
        return when {
            jobTitle.contains("kho", ignoreCase = true) -> R.drawable.outline_warehouse_24
            jobTitle.contains("nhà hàng", ignoreCase = true) || 
            jobTitle.contains("phục vụ", ignoreCase = true) -> R.drawable.outline_restaurant_24
            jobTitle.contains("bán hàng", ignoreCase = true) || 
            jobTitle.contains("shop", ignoreCase = true) -> R.drawable.ic_launcher_foreground
            jobTitle.contains("lao động", ignoreCase = true) || 
            jobTitle.contains("công nhân", ignoreCase = true) -> R.drawable.outline_tools_pliers_wire_stripper_24
            else -> R.drawable.outline_warehouse_24 // Default icon
        }
    }
}
