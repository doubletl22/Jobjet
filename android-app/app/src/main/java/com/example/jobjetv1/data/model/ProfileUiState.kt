package com.example.jobjetv1.data.model

data class ProfileUiStat(
    val name: String = "Nguyễn Văn A",
    val avatarUrl: String? = null,
    val rating: Float = 4.8f,
    val progress: Int = 75,
    val mainBank: BankInfo = BankInfo("Vietcombank", "**** 5678", 2_500_000, isDefault = true),
    val working: Boolean = true,
    val workTime: String = "06:45:12",
    val savedJobs: List<SavedJob> = listOf(
        SavedJob("Nhân viên bán hàng", "Circle K", "Quận 1, TP.HCM", "23,000 VND/giờ"),
        SavedJob("Nhân viên kho", "Shopee Express", "Quận 7, TP.HCM", "31,250 VND/giờ")
    )
)
data class BankInfo(val bankName: String, val accNo: String, val balance: Int, val isDefault: Boolean)
data class SavedJob(val title: String, val company: String, val address: String, val wage: String)
data class ProfileUiState(val mainBank: BankInfo = BankInfo("Vietcombank", "**** 5678", 2_500_000, isDefault = true ))