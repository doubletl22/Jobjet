package com.example.jobjetv1.data.model

data class JobPostUiState(
    var companyName: String = "",
    var jobTitle: String = "",
    var quantity: String = "1",
    var workType: WorkType = WorkType.FULL_TIME,
    var wage: String = "",
    var wageUnit: String = "VND/giờ",
    var description: String = "",
    var requirement: String = "",
    var rule: String = "",
    var address: String = "",
    var contactName: String = "",
    var contactEmail: String = "",
    var contactPhone: String = ""
)
enum class WorkType { FULL_TIME, PART_TIME }
