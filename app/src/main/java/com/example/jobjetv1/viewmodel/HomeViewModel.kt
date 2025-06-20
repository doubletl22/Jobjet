package com.example.jobjetv1.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.jobjetv1.data.model.Job
import com.example.jobjetv1.R
import androidx.compose.ui.graphics.Color

class HomeViewModel : ViewModel() {
    var jobs by mutableStateOf(sampleJobs())
        private set

    companion object {
        fun sampleJobs() = listOf(
            Job("1", "Out Layer", "Global City Quận 9", "Setup, điều chỉnh sân khấu.", "50,000 VND/Giờ", Color(0xFF43A047), R.drawable.outline_tools_pliers_wire_stripper_24),
            Job("2", "Kho Shoppe", "221/4 Phan Huy Ích, P14, Gò Vấp", "Phân loại bưu kiện, sắp xếp hàng hóa.", "31,250 VND/Giờ", Color(0xFF43A047), R.drawable.outline_warehouse_24),
            Job("3", "Kho Shoppe", "618/1B Âu cơ, P10, Tân Bình", "Phân loại bưu kiện, sắp xếp hàng hóa.", "31,250 VND/Giờ", Color(0xFF43A047), R.drawable.outline_warehouse_24),
            Job("4", "Nhà hàng", "202 Hoàng Văn Thụ, Phú Nhuận", "Phân loại bưu kiện, sắp xếp hàng hóa.", "25,000 VND/Giờ", Color(0xFF43A047), R.drawable.outline_restaurant_24),
            Job("5", "Kho Shoppe", "123 Nguyễn Hữu Tiến, Tân Phú", "Phân loại bưu kiện, sắp xếp hàng hóa.", "31,250 VND/Giờ", Color(0xFF43A047), R.drawable.outline_warehouse_24),
        )
    }
}
