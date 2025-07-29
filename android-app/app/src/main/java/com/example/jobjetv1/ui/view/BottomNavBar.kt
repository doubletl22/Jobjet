package com.example.jobjetv1.ui.view

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jobjetv1.R

@Composable
fun BottomNavBar(
    selectedIndex: Int = 0,
    onTabSelected: (Int) -> Unit = {}
) {
    val items = listOf(
        NavItem("Trang chủ", R.drawable.baseline_home_24),
        NavItem("Tìm kiếm", R.drawable.outline_search_24),
        NavItem("Hồ sơ", R.drawable.outline_person_24),
        NavItem("Thông báo", R.drawable.outline_notifications_24)
    )
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 2.dp
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = { onTabSelected(index) },
                icon = { Icon(painterResource(item.icon), contentDescription = item.label) },
                label = { Text(item.label, fontSize = 11.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFF2196F3),
                    selectedTextColor = Color(0xFF2196F3),
                    indicatorColor = Color(0x112196F3),
                    unselectedIconColor = Color(0xFFBDBDBD),
                    unselectedTextColor = Color(0xFFBDBDBD),
                )
            )
        }
    }
}

data class NavItem(val label: String, val icon: Int)
