package com.example.jobjetv1.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.jobjetv1.data.model.JobCategory
import com.example.jobjetv1.R

class SearchViewModel : ViewModel() {
    var searchText by mutableStateOf("")
    var recentSearches by mutableStateOf(listOf("Kho Shopee", "Nhà hàng quận 7", "Công việc cuối tuần"))
    val categories = listOf(
        JobCategory(R.drawable.outline_warehouse_24, "Kho bãi"),
        JobCategory(R.drawable.outline_restaurant_24, "Nhà hàng"),
        JobCategory(R.drawable.outline_warehouse_24, "Bán hàng"),
        JobCategory(R.drawable.outline_warehouse_24, "Văn phòng"),
        JobCategory(R.drawable.outline_warehouse_24, "Giao hàng"),
        JobCategory(R.drawable.outline_person_24, "Tất cả")
    )
    val suggestionChips = listOf(
        "Việc làm bán thời gian", "Kho vận", "Nhà hàng",
        "Lương trên 30k/giờ", "Quận 1", "Quận Gò Vấp"
    )

    fun removeRecent(index: Int) {
        recentSearches = recentSearches.toMutableList().also { it.removeAt(index) }
    }

    fun addRecent(search: String) {
        if (search.isNotBlank() && !recentSearches.contains(search)) {
            recentSearches = listOf(search) + recentSearches
        }
    }
}
