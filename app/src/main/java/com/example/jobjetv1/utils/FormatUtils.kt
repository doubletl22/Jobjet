package com.example.jobjetv1.utils

import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

object FormatUtils {
    
    private val vietnameseLocale = Locale("vi", "VN")
    private val numberFormat = NumberFormat.getInstance(vietnameseLocale)
    private val currencyFormat = NumberFormat.getCurrencyInstance(vietnameseLocale)
    
    /**
     * Format salary to Vietnamese currency format
     */
    fun formatSalary(amount: Long): String {
        return when {
            amount >= 1_000_000 -> {
                val millions = amount / 1_000_000.0
                if (millions >= 10) {
                    "${millions.toInt()} triệu"
                } else {
                    String.format("%.1f triệu", millions)
                }
            }
            amount >= 1_000 -> {
                val thousands = amount / 1_000.0
                String.format("%.0fK", thousands)
            }
            else -> numberFormat.format(amount)
        }
    }
    
    /**
     * Format salary range
     */
    fun formatSalaryRange(minSalary: Long?, maxSalary: Long?): String {
        return when {
            minSalary != null && maxSalary != null -> 
                "${formatSalary(minSalary)} - ${formatSalary(maxSalary)}"
            minSalary != null -> "Từ ${formatSalary(minSalary)}"
            maxSalary != null -> "Lên đến ${formatSalary(maxSalary)}"
            else -> "Thỏa thuận"
        }
    }
    
    /**
     * Format job posting time
     */
    fun formatJobPostingTime(dateTime: LocalDateTime): String {
        val now = LocalDateTime.now()
        val duration = java.time.Duration.between(dateTime, now)
        
        return when {
            duration.toDays() > 30 -> {
                dateTime.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT))
            }
            duration.toDays() > 0 -> "${duration.toDays()} ngày trước"
            duration.toHours() > 0 -> "${duration.toHours()} giờ trước"
            duration.toMinutes() > 0 -> "${duration.toMinutes()} phút trước"
            else -> "Vừa xong"
        }
    }
    
    /**
     * Format deadline
     */
    fun formatDeadline(dateTime: LocalDateTime): String {
        val now = LocalDateTime.now()
        val duration = java.time.Duration.between(now, dateTime)
        
        return when {
            duration.isNegative -> "Đã hết hạn"
            duration.toDays() > 30 -> {
                dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            }
            duration.toDays() > 0 -> "Còn ${duration.toDays()} ngày"
            duration.toHours() > 0 -> "Còn ${duration.toHours()} giờ"
            else -> "Sắp hết hạn"
        }
    }
    
    /**
     * Format experience level
     */
    fun formatExperience(years: Int): String {
        return when (years) {
            0 -> "Không yêu cầu kinh nghiệm"
            1 -> "1 năm kinh nghiệm"
            in 2..4 -> "$years năm kinh nghiệm"
            else -> "$years+ năm kinh nghiệm"
        }
    }
    
    /**
     * Format job type
     */
    fun formatJobType(type: String): String {
        return when (type.lowercase()) {
            "full-time", "fulltime" -> "Toàn thời gian"
            "part-time", "parttime" -> "Bán thời gian"
            "contract" -> "Hợp đồng"
            "internship" -> "Thực tập"
            "remote" -> "Remote"
            "freelance" -> "Freelance"
            else -> type
        }
    }
    
    /**
     * Truncate text with ellipsis
     */
    fun truncateText(text: String, maxLength: Int): String {
        return if (text.length <= maxLength) {
            text
        } else {
            text.take(maxLength - 3) + "..."
        }
    }
    
    /**
     * Format company name with size indicator
     */
    fun formatCompanyWithSize(companyName: String, companySize: String?): String {
        return if (companySize != null) {
            "$companyName ($companySize)"
        } else {
            companyName
        }
    }
    
    /**
     * Format location
     */
    fun formatLocation(city: String, district: String? = null): String {
        return if (district != null) {
            "$district, $city"
        } else {
            city
        }
    }
}
