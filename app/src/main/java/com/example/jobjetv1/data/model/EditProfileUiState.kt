package com.example.jobjetv1.data.model

data class EditProfileUiState(
    var name: String = "Nguyễn Văn A",
    var phone: String = "xxxxxxxx89",
    var idCard: String = "1234567xxxxxxx",
    var birth: String = "**/11/20**",
    var gender: String = "Nam",
    var address: String = "đường xx, phường xx, ...",
    var isPublic: Boolean = true,
    var isLookingForJob: Boolean = true
)
