package com.example.jobjetv1.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.example.jobjetv1.data.model.NotificationItem
import com.example.jobjetv1.data.model.NotificationType
import com.example.jobjetv1.viewmodel.NotificationViewModel
import com.example.jobjetv1.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    viewModel: NotificationViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onTabSelected: (Int) -> Unit = {},
    onMenuClick: (NotificationItem) -> Unit = {},
    selectedTab: Int = 3,
    onTabNavSelected: (Int) -> Unit = {}
) {
    val tabTitles = listOf("Tất cả", "Chưa đọc", "Đã đọc")
    val notifications = viewModel.filterNotifications()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thông báo", fontWeight = FontWeight.Bold, fontSize = 19.sp) },
                actions = {
                    TextButton(onClick = { viewModel.markAllAsRead() }) {
                        Text("Đánh dấu đã đọc tất cả", fontSize = 14.sp, color = Color(0xFF2196F3))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            BottomNavBar(selectedIndex = selectedTab, onTabSelected = onTabNavSelected)
        }
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 0.dp)
                .background(Color.White)
        ) {
            // Tabs
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp, bottom = 10.dp, start = 6.dp, end = 6.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                tabTitles.forEachIndexed { i, title ->
                    val selected = viewModel.selectedTab == i
                    TextButton(
                        onClick = { viewModel.selectedTab = i; onTabSelected(i) },
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selected) Color(0xFF2196F3) else Color(0xFFF7F8FA),
                            contentColor = if (selected) Color.White else Color(0xFF747474)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                    ) {
                        Text(title, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }
            Divider(color = Color(0xFFF2F2F2), thickness = 1.dp)
            Spacer(Modifier.height(5.dp))
            // Notification List
            notifications.forEach { item ->
                NotificationCard(item, onMenuClick)
            }
        }
    }
}

@Composable
fun NotificationCard(
    item: NotificationItem,
    onMenuClick: (NotificationItem) -> Unit = {}
) {
    val pair: Pair<Color, Int> = when (item.type) {
        NotificationType.SUCCESS -> Pair(Color(0xFFE8FBEE), R.drawable.baseline_check_circle_outline_24)
        NotificationType.MESSAGE -> Pair(Color(0xFFE8F2FF), R.drawable.baseline_email_24)
        NotificationType.ALERT  -> Pair(Color(0xFFF4F6FA), R.drawable.outline_notifications_24)
        NotificationType.VIEW   -> Pair(Color(0xFFF4E6FA), R.drawable.baseline_remove_red_eye_24)
        // else -> Pair(Color.LightGray, R.drawable.ic_notifications)  // nếu cần
    }
    val (bgColor, icon) = pair


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
            .background(if (!item.isRead) Color(0xFFF6F7FA) else Color.White),
        colors = CardDefaults.cardColors(containerColor = if (!item.isRead) Color(0xFFF6F7FA) else Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            Modifier
                .padding(vertical = 12.dp, horizontal = 13.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .size(35.dp)
                    .background(bgColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painterResource(icon),
                    contentDescription = null,
                    tint = Color(0xFF2196F3),
                    modifier = Modifier.size(23.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (!item.isRead) {
                        Icon(
                            painterResource(R.drawable.outline_notifications_unread_24),
                            contentDescription = null,
                            tint = Color(0xFF4DC37A),
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                    }
                    Text(item.title, fontWeight = if (!item.isRead) FontWeight.Bold else FontWeight.Normal, fontSize = 15.sp)
                }
                Text(item.message, fontSize = 14.sp, color = Color.Gray, maxLines = 2, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                Text(item.time, fontSize = 13.sp, color = Color(0xFFB0B0B0), modifier = Modifier.padding(top = 1.dp))
            }
            IconButton(onClick = { onMenuClick(item) }) {
                Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = Color(0xFFBDBDBD))
            }
        }
    }
}
