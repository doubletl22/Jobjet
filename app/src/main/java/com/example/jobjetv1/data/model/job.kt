package com.example.jobjetv1.data.model

import androidx.compose.ui.graphics.Color
import com.google.firebase.Timestamp
import com.example.jobjetv1.R

/**
 * Đại diện cho một tin tuyển dụng.
 * Data class này được thiết kế để tương thích với cả dữ liệu mẫu cục bộ và dữ liệu từ Firestore.
 *
 * Nó chứa tất cả các trường có thể được sử dụng trong toàn bộ ứng dụng.
 * - `jobTitle` và `companyName` là nguồn dữ liệu chính cho thông tin công việc và công ty.
 * - `iconRes` và `wageColor` được bao gồm để hiển thị trên giao diện người dùng, với các giá trị mặc định hợp lý.
 * - Các thuộc tính được tính toán như `company`, `location`, `category`, và `salary` cung cấp
 * một giao diện nhất quán cho các phần khác nhau của ứng dụng.
 */
data class Job(
    // --- Các trường dữ liệu chính (Nguồn dữ liệu đáng tin cậy) ---
    val id: String = "",
    val jobTitle: String = "",
    val companyName: String = "",
    val description: String = "",
    val requirement: String = "",
    val address: String = "",
    val wage: String = "",
    val quantity: Int = 0,
    val workType: String = "",
    val contactName: String = "",
    val contactEmail: String = "",
    val contactPhone: String = "",
    val postedDate: Timestamp = Timestamp.now(),

    // --- Các trường để hiển thị & tương thích với dữ liệu cũ (Legacy) ---
    val iconRes: Int = R.drawable.outline_warehouse_24,
    val wageColor: Color = Color(0xFF43A047),
    val title: String = "" // Trường cũ được sử dụng bởi JobsRepository
) {

    // Constructor phụ để tương thích ngược với JobsRepository
    constructor(
        id: String,
        legacyTitle: String, // "title" cũ từ JobsRepository
        address: String,
        description: String,
        wage: String,
        wageColor: Color,
        iconRes: Int,
        postedDate: Timestamp
    ) : this(
        id = id,
        jobTitle = legacyTitle, // Sử dụng legacyTitle làm jobTitle
        companyName = extractCompanyFromTitle(legacyTitle), // Cố gắng trích xuất tên công ty từ legacyTitle
        description = description,
        requirement = "", // Dữ liệu cũ không có trường này
        address = address,
        wage = wage,
        quantity = 1, // Giá trị mặc định
        workType = "", // Dữ liệu cũ không có trường này
        contactName = "",
        contactEmail = "",
        contactPhone = "",
        postedDate = postedDate,
        iconRes = iconRes,
        wageColor = wageColor,
        title = legacyTitle // Lưu lại legacyTitle gốc
    )

    // --- Các thuộc tính được tính toán để truy cập nhất quán trong UI ---

    // Cung cấp tên công ty nhất quán. Ưu tiên trường `companyName` tường minh.
    val company: String
        get() = if (companyName.isNotBlank()) companyName else extractCompanyFromTitle(this.title)

    // Cung cấp vị trí nhất quán (Quận/Thành phố).
    val location: String
        get() = extractLocationFromAddress(address)

    // Cung cấp danh mục nhất quán. Ưu tiên suy ra từ `jobTitle`.
    val category: String
        get() = extractCategoryFromTitle(if (jobTitle.isNotBlank()) jobTitle else this.title)

    // Cung cấp `salary` như một tên gọi khác của `wage` để tương thích.
    val salary: String
        get() = wage

    companion object {
        private fun extractCompanyFromTitle(title: String): String {
            // Logic này không hoàn hảo nhưng được duy trì để tương thích với dữ liệu cũ.
            return when {
                title.contains("Shoppe", ignoreCase = true) -> "Shopee"
                title.contains("Kho", ignoreCase = true) -> "Shopee Warehouse"
                title.contains("Nhà hàng", ignoreCase = true) -> "Nhà hàng ${title.split(" ").lastOrNull() ?: ""}"
                title == "Out Layer" -> "Out Layer"
                else -> title.split(" ").firstOrNull() ?: "Công ty"
            }
        }

        private fun extractLocationFromAddress(address: String): String {
            // Trích xuất quận/khu vực từ địa chỉ
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