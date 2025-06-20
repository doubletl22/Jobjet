package com.example.jobjetv1.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.jobjetv1.data.model.NotificationItem
import com.example.jobjetv1.data.model.NotificationType

class NotificationViewModel : ViewModel() {
    var selectedTab by mutableStateOf(0)
    var notifications by mutableStateOf(sampleNotifications())

    fun markAllAsRead() {
        notifications = notifications.map { it.copy(isRead = true) }
    }

    fun filterNotifications(): List<NotificationItem> = when(selectedTab) {
        0 -> notifications
        1 -> notifications.filter { !it.isRead }
        2 -> notifications.filter { it.isRead }
        else -> notifications
    }

    companion object {
        fun sampleNotifications() = listOf(
            NotificationItem(
                "1", NotificationType.SUCCESS,
                title = "Đăng kí thành công việc làm:",
                message = "Shopee Express",
                time = "2 giờ trước"
            ),
            NotificationItem(
                "2", NotificationType.MESSAGE,
                title = "Circle K",
                message = "gửi lời mời phỏng vấn cho vị trí Quản lý cửa hàng",
                time = "hôm qua",
                isRead = true
            ),
            NotificationItem(
                "3", NotificationType.ALERT,
                title = "Thông báo",
                message = "Cập nhật hồ sơ của bạn để tăng cơ hội được tuyển dụng",
                time = "3 ngày trước",
                isRead = true
            ),
            NotificationItem(
                "4", NotificationType.VIEW,
                title = "Shopee Express",
                message = "đã xem hồ sơ của bạn",
                time = "2 ngày trước",
                isRead = true
            )
        )

    }
}
