package com.example.jobjetv1.data.model

import androidx.compose.ui.graphics.Color

data class Job(
    val id: String,
    val title: String,
    val address: String,
    val description: String,
    val wage: String,      // ex: "31,250 VND/Giờ"
    val wageColor: Color,
    val iconRes: Int,       // drawable resource id

    val company: String = extractCompanyFromTitle(title),
    val location: String = extractLocationFromAddress(address),
    val category: String = extractCategoryFromTitle(title),
    val workType: String = "Bán thời gian", // Default work type, can be overridden
    val workTime: String = "Linh hoạt",     // Default work time, can be overridden
    val postedDate: String = "Hôm nay",     // Default posted date
    val salary: String = wage               // Alias for wage for consistency
) {
    companion object {
        private fun extractCompanyFromTitle(title: String): String {
            return when {
                title.contains("Shoppe", ignoreCase = true) -> "Shopee"
                title.contains("Kho", ignoreCase = true) -> "Shopee Warehouse"
                title.contains("Nhà hàng", ignoreCase = true) -> "Nhà hàng ${title.split(" ").lastOrNull() ?: ""}"
                else -> title.split(" ").firstOrNull() ?: "Công ty"
            }
        }

        private fun extractLocationFromAddress(address: String): String {
            // Extract district/area from address
            return when {
                address.contains("Quận", ignoreCase = true) -> {
                    val parts = address.split(",")
                    parts.find { it.trim().contains("Quận", ignoreCase = true) }?.trim() ?: "TP.HCM"
                }
                address.contains("Gò Vấp", ignoreCase = true) -> "Quận Gò Vấp"
                address.contains("Tân Bình", ignoreCase = true) -> "Quận Tân Bình"
                address.contains("Phú Nhuận", ignoreCase = true) -> "Quận Phú Nhuận"
                address.contains("Tân Phú", ignoreCase = true) -> "Quận Tân Phú"
                else -> "TP.HCM"
            }
        }

        private fun extractCategoryFromTitle(title: String): String {
            return when {
                title.contains("Kho", ignoreCase = true) -> "Kho bãi"
                title.contains("Nhà hàng", ignoreCase = true) -> "Nhà hàng"
                title.contains("Bán hàng", ignoreCase = true) -> "Bán hàng"
                title.contains("Văn phòng", ignoreCase = true) -> "Văn phòng"
                title.contains("Admin", ignoreCase = true) -> "Văn phòng"
                title.contains("Data", ignoreCase = true) -> "Văn phòng"
                title.contains("Giao hàng", ignoreCase = true) -> "Giao hàng"
                title.contains("Shipper", ignoreCase = true) -> "Giao hàng"
                title.contains("Setup", ignoreCase = true) ||
                        title.contains("Layer", ignoreCase = true) ||
                        title.contains("Kỹ thuật", ignoreCase = true) -> "Kỹ thuật"
                else -> "Khác"
            }
        }
    }
}
