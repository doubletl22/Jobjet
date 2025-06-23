package com.example.jobjetv1.utils

object Constants {
    // Animation durations
    const val ANIMATION_DURATION_SHORT = 300
    const val ANIMATION_DURATION_MEDIUM = 500
    const val ANIMATION_DURATION_LONG = 1000
    
    // Search configurations
    const val SEARCH_DEBOUNCE_DELAY = 300L
    const val MAX_RECENT_SEARCHES = 5
    const val MIN_SEARCH_QUERY_LENGTH = 2
    
    // Job configurations
    const val JOBS_PER_PAGE = 20
    const val MAX_SAVED_JOBS = 100
    
    // UI configurations
    const val CARD_CORNER_RADIUS = 12
    const val BUTTON_CORNER_RADIUS = 8
    const val ICON_SIZE_SMALL = 16
    const val ICON_SIZE_MEDIUM = 24
    const val ICON_SIZE_LARGE = 32
    
    // Colors
    const val PRIMARY_COLOR = 0xFF1976D2
    const val SECONDARY_COLOR = 0xFF42A5F5
    const val SUCCESS_COLOR = 0xFF4CAF50
    const val WARNING_COLOR = 0xFFFF9800
    const val ERROR_COLOR = 0xFFF44336
    
    // Job categories
    object JobCategories {
        const val TECHNOLOGY = "Công nghệ"
        const val MARKETING = "Marketing"
        const val SALES = "Bán hàng"
        const val DESIGN = "Thiết kế"
        const val FINANCE = "Tài chính"
        const val EDUCATION = "Giáo dục"
        const val HEALTHCARE = "Y tế"
        const val CUSTOMER_SERVICE = "Chăm sóc khách hàng"
    }
    
    // Salary ranges
    object SalaryRanges {
        const val ENTRY_LEVEL = "5-10 triệu"
        const val MID_LEVEL = "10-20 triệu"
        const val SENIOR_LEVEL = "20-40 triệu"
        const val EXECUTIVE_LEVEL = "40+ triệu"
    }
    
    // Work types
    object WorkTypes {
        const val FULL_TIME = "Toàn thời gian"
        const val PART_TIME = "Bán thời gian"
        const val CONTRACT = "Hợp đồng"
        const val INTERNSHIP = "Thực tập"
        const val REMOTE = "Remote"
    }
    
    // Experience levels
    object ExperienceLevels {
        const val FRESH_GRADUATE = "Mới tốt nghiệp"
        const val ONE_TO_THREE_YEARS = "1-3 năm"
        const val THREE_TO_FIVE_YEARS = "3-5 năm"
        const val FIVE_PLUS_YEARS = "5+ năm"
    }
    
    // Popular locations
    object Locations {
        const val HO_CHI_MINH = "Hồ Chí Minh"
        const val HA_NOI = "Hà Nội"
        const val DA_NANG = "Đà Nẵng"
        const val HAI_PHONG = "Hải Phòng"
        const val CAN_THO = "Cần Thơ"
        const val BINH_DUONG = "Bình Dương"
        const val DONG_NAI = "Đồng Nai"
    }
}
