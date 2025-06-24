package com.example.jobjetv1.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import androidx.compose.ui.graphics.Color
import com.example.jobjetv1.data.model.JobCategory
import com.example.jobjetv1.data.model.Job
import com.example.jobjetv1.repository.JobsRepository
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.example.jobjetv1.debug.FilterDebugHelper

data class SearchFilters(
    val minSalary: String = "",
    val maxSalary: String = "",
    val location: String = "",
    val jobType: String = "",
    val category: String = "",
    val workTime: String = "",
    val sortBy: String = "newest" // newest, salary_high, salary_low
)

data class SearchUiState(
    val isLoading: Boolean = false,
    val searchResults: List<Job> = emptyList(),
    val hasError: Boolean = false,
    val errorMessage: String = "",
    val isShowingFilters: Boolean = false,
    val appliedFiltersCount: Int = 0
)

class SearchViewModel : ViewModel() {

    private val jobsRepository = JobsRepository
// ... Other imports and code remain the same

    private fun filterJobs(jobs: List<Job>): List<Job> {
        return jobs.filter { job ->
            val matchesSearchText = if (searchText.isBlank()) {
                true
            } else {
                job.title.contains(searchText, ignoreCase = true) ||
                        job.company.contains(searchText, ignoreCase = true) ||
                        job.location.contains(searchText, ignoreCase = true) ||
                        job.description.contains(searchText, ignoreCase = true)
            }

            val matchesCategory = if (searchFilters.category.isEmpty() || searchFilters.category == "Tất cả") {
                true
            } else {
                job.category == searchFilters.category
            }

            val matchesLocation = if (searchFilters.location.isEmpty() || searchFilters.location == "Tất cả") {
                true
            } else {
                job.location.contains(searchFilters.location, ignoreCase = true)
            }

            val matchesJobType = if (searchFilters.jobType.isEmpty() || searchFilters.jobType == "Tất cả") {
                true
            } else {
                job.workType.contains(searchFilters.jobType, ignoreCase = true)
            }

            // Only include workTime filter if Job has workTime property
            val matchesWorkTime = if (searchFilters.workTime.isEmpty() || searchFilters.workTime == "Tất cả") {
                true
            } else {
                job.workTime?.contains(searchFilters.workTime, ignoreCase = true) ?: true // Handle null
            }

            val matchesSalary = if (searchFilters.minSalary.isEmpty() && searchFilters.maxSalary.isEmpty()) {
                true
            } else {
                try {
                    val jobSalary = extractSalaryFromString(job.salary)
                    val minSal = searchFilters.minSalary.toIntOrNull() ?: 0
                    val maxSal = searchFilters.maxSalary.toIntOrNull() ?: Int.MAX_VALUE
                    jobSalary in minSal..maxSal
                } catch (e: Exception) {
                    true
                }
            }

            matchesSearchText && matchesCategory && matchesLocation &&
                    matchesJobType && matchesWorkTime && matchesSalary
        }
    }
    // UI State
    var searchText by mutableStateOf("")
        private set

    var searchFilters by mutableStateOf(SearchFilters())
        private set

    var uiState by mutableStateOf(SearchUiState())
        private set

    var recentSearches by mutableStateOf(listOf("Kho Shopee", "Nhà hàng quận 7", "Công việc cuối tuần"))
        private set

    // Dynamic categories with enhanced visual appeal and detailed info
    val categories by derivedStateOf {
        val allJobs = jobsRepository.allJobs
        val warehouseJobs = allJobs.count { job ->
            job.category == "Kho bãi"
        }
        val restaurantJobs = allJobs.count { job ->
            job.category == "Nhà hàng"
        }
        val salesJobs = allJobs.count { job ->
            job.category == "Bán hàng"
        }
        val officeJobs = allJobs.count { job ->
            job.category == "Văn phòng"
        }
        val deliveryJobs = allJobs.count { job ->
            job.category == "Giao hàng"
        }
        val techJobs = allJobs.count { job ->
            job.category == "Kỹ thuật"
        }

        listOf(
            JobCategory(
                icon = Icons.Default.Inventory,
                label = "Kho bãi",
                description = "Phân loại, sắp xếp hàng hóa tại các trung tâm logistics",
                color = Color(0xFF2E7D32), // Green gradient
                jobCount = warehouseJobs,
                isPopular = warehouseJobs > 2,
                salary = "25-35k/giờ",
                keywords = listOf("Shopee", "Lazada", "Tiki", "Phân loại", "Đóng gói", "Sorting")
            ),
            JobCategory(
                icon = Icons.Default.Restaurant,
                label = "Nhà hàng",
                description = "Phục vụ, pha chế, bán hàng F&B và dịch vụ ẩm thực",
                color = Color(0xFFE53935), // Red gradient
                jobCount = restaurantJobs,
                isPopular = restaurantJobs > 1,
                salary = "20-30k/giờ",
                keywords = listOf("Phục vụ", "Pha chế", "Cashier", "Bếp", "Barista", "KFC", "McDonald's")
            ),
            JobCategory(
                icon = Icons.Default.ShoppingCart,
                label = "Bán hàng",
                description = "Tư vấn, chăm sóc khách hàng, bán lẻ và online",
                color = Color(0xFF1E88E5), // Blue gradient
                jobCount = salesJobs,
                isPopular = salesJobs > 1,
                salary = "22-40k/giờ",
                keywords = listOf("Tư vấn", "CSKH", "Telesale", "Retail", "Promoter", "Beauty advisor")
            ),
            JobCategory(
                icon = Icons.Default.BusinessCenter,
                label = "Văn phòng",
                description = "Admin, data entry, hỗ trợ văn phòng và xử lý tài liệu",
                color = Color(0xFF8E24AA), // Purple gradient
                jobCount = officeJobs,
                isPopular = officeJobs > 1,
                salary = "25-50k/giờ",
                keywords = listOf("Excel", "Data entry", "Admin", "Hỗ trợ", "Receptionist", "Assistant")
            ),
            JobCategory(
                icon = Icons.Default.LocalShipping,
                label = "Giao hàng",
                description = "Shipper, tài xế, courier và dịch vụ vận chuyển",
                color = Color(0xFFFF9800), // Orange gradient
                jobCount = deliveryJobs,
                isPopular = deliveryJobs > 1,
                salary = "30-60k/giờ",
                keywords = listOf("Grab", "Bee", "Shipper", "Motorbike", "Now", "Ahamove", "Delivery")
            ),
            JobCategory(
                icon = Icons.Default.Build,
                label = "Kỹ thuật",
                description = "Setup, lắp đặt thiết bị, công việc kỹ thuật",
                color = Color(0xFF795548), // Brown gradient
                jobCount = techJobs,
                isPopular = techJobs > 0,
                salary = "35-60k/giờ",
                keywords = listOf("Setup", "Lắp đặt", "Kỹ thuật", "Thiết bị", "Technical", "Installation")
            ),
            JobCategory(
                icon = Icons.Default.Star,
                label = "Tất cả",
                description = "Khám phá toàn bộ cơ hội nghề nghiệp dành cho bạn",
                color = Color(0xFF546E7A), // Blue grey gradient
                jobCount = allJobs.size,
                isPopular = false,
                salary = "20-60k/giờ",
                keywords = listOf("Part-time", "Full-time", "Flexible", "Student job", "Weekend", "Evening")
            )
        )
    }

    // Smart suggestions based on trending and user behavior
    val suggestionChips by derivedStateOf {
        val trending = listOf(
            "💼 Part-time Shopee", "🏪 Nhà hàng cuối tuần", "📦 Kho Lazada",
            "🚚 Shipper Grab", "☕ Barista", "🛒 Cashier siêu thị"
        )

        val locationBased = listOf(
            "🌟 Quận 1 - Lương cao", "🔥 Gò Vấp hot jobs", "✨ Quận 7 mới"
        )

        val timeBased = listOf(
            "⏰ Việc làm tối", "🌅 Ca sáng linh hoạt", "🎯 Cuối tuần"
        )

        (trending + locationBased + timeBased).shuffled().take(6)
    }

    // Filter options
    val locationOptions = listOf(
        "Tất cả", "Quận 1", "Quận 2", "Quận 3", "Quận 4", "Quận 5",
        "Quận 6", "Quận 7", "Quận 8", "Quận 9", "Quận 10", "Quận 11", "Quận 12",
        "Quận Bình Thạnh", "Quận Gò Vấp", "Quận Phú Nhuận", "Quận Tân Bình",
        "Quận Tân Phú", "Quận Thủ Đức", "Huyện Bình Chánh", "Huyện Hóc Môn"
    )

    val jobTypeOptions = listOf(
        "Tất cả", "Toàn thời gian", "Bán thời gian", "Thực tập", "Freelance", "Hợp đồng"
    )

    val workTimeOptions = listOf(
        "Tất cả", "Ca sáng", "Ca chiều", "Ca tối", "Cuối tuần", "Linh hoạt"
    )

    val sortOptions = listOf(
        "newest" to "Mới nhất",
        "salary_high" to "Lương cao nhất",
        "salary_low" to "Lương thấp nhất",
        "relevant" to "Phù hợp nhất"
    )

    init {
        // Load tất cả jobs khi khởi tạo
        searchJobs()
    }

    fun updateSearchText(text: String) {
        searchText = text
        if (text.isNotBlank()) {
            searchJobs()
        } else {
            // Reset về tất cả jobs khi search text rỗng
            searchJobs()
        }
    }

    fun searchJobs() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, hasError = false)

            try {
                // Simulate network delay
                delay(500)

                val allJobs = jobsRepository.allJobs
                val filteredJobs = filterJobs(allJobs)
                val sortedJobs = sortJobs(filteredJobs)

                uiState = uiState.copy(
                    isLoading = false,
                    searchResults = sortedJobs,
                    hasError = false
                )

            } catch (e: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    hasError = true,
                    errorMessage = "Có lỗi xảy ra khi tìm kiếm: ${e.message}"
                )
            }
        }
    }

    private fun sortJobs(jobs: List<Job>): List<Job> {
        return when (searchFilters.sortBy) {
            "salary_high" -> jobs.sortedByDescending { extractSalaryFromString(it.salary) }
            "salary_low" -> jobs.sortedBy { extractSalaryFromString(it.salary) }
            "relevant" -> {
                if (searchText.isBlank()) {
                    jobs.sortedByDescending { it.postedDate }
                } else {
                    jobs.sortedByDescending { job ->
                        val titleMatch = if (job.title.contains(searchText, ignoreCase = true)) 3 else 0
                        val companyMatch = if (job.company.contains(searchText, ignoreCase = true)) 2 else 0
                        val locationMatch = if (job.location.contains(searchText, ignoreCase = true)) 1 else 0
                        titleMatch + companyMatch + locationMatch
                    }
                }
            }
            else -> jobs.sortedByDescending { it.postedDate } // newest
        }
    }

    private fun extractSalaryFromString(salaryStr: String): Int {
        return try {
            // Extract numbers from salary string (e.g., "30.000 - 50.000 VND/giờ" -> 50000)
            val numbers = salaryStr.replace(".", "").replace(",", "")
                .filter { it.isDigit() || it == ' ' || it == '-' }
                .split("-", " ")
                .mapNotNull { it.trim().toIntOrNull() }
                .filter { it > 0 }

            numbers.maxOrNull() ?: 0
        } catch (e: Exception) {
            0
        }
    }

    fun updateFilters(filters: SearchFilters) {
        searchFilters = filters
        uiState = uiState.copy(
            appliedFiltersCount = countAppliedFilters(filters)
        )
        searchJobs()
    }

    private fun countAppliedFilters(filters: SearchFilters): Int {
        var count = 0
        if (filters.minSalary.isNotEmpty()) count++
        if (filters.maxSalary.isNotEmpty()) count++
        if (filters.location.isNotEmpty() && filters.location != "Tất cả") count++
        if (filters.jobType.isNotEmpty() && filters.jobType != "Tất cả") count++
        if (filters.category.isNotEmpty() && filters.category != "Tất cả") count++
        if (filters.workTime.isNotEmpty() && filters.workTime != "Tất cả") count++
        return count
    }

    fun clearFilters() {
        searchFilters = SearchFilters()
        uiState = uiState.copy(appliedFiltersCount = 0)
        searchJobs()
    }

    fun clearQuickFilters() {
        searchFilters = SearchFilters()
        uiState = uiState.copy(appliedFiltersCount = 0)
        searchJobs()
    }

    fun toggleFiltersVisibility() {
        uiState = uiState.copy(isShowingFilters = !uiState.isShowingFilters)
    }

    fun selectCategory(category: String) {
        searchFilters = searchFilters.copy(category = category)
        uiState = uiState.copy(
            appliedFiltersCount = countAppliedFilters(searchFilters)
        )
        searchJobs()
    }

    fun selectSuggestion(suggestion: String) {
        searchText = suggestion
        addRecent(suggestion)
        searchJobs()
    }

    fun removeRecent(index: Int) {
        recentSearches = recentSearches.toMutableList().also { it.removeAt(index) }
    }

    fun addRecent(search: String) {
        if (search.isNotBlank() && !recentSearches.contains(search)) {
            recentSearches = listOf(search) + recentSearches.take(9) // Keep max 10 recent searches
        }
    }

    fun clearError() {
        uiState = uiState.copy(hasError = false, errorMessage = "")
    }

    fun getDebugInfo(): String {
        return FilterDebugHelper.getDebugCategoryInfo()
    }
}
