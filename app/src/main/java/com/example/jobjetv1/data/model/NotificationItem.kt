package com.example.jobjetv1.data.model


data class NotificationItem(
    val id: String,
    val type: NotificationType,
    val title: String,
    val message: String,
    val time: String,
    val isRead: Boolean = false,

)

enum class NotificationType {
    SUCCESS, MESSAGE, ALERT, VIEW
}
