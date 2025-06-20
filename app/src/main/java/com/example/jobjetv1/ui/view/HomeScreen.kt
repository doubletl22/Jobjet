package com.example.jobjetv1.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.example.jobjetv1.viewmodel.HomeViewModel
import com.example.jobjetv1.data.model.Job
import com.example.jobjetv1.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    val jobs = viewModel.jobs.filter {
        it.title.contains(searchText, true) ||
                it.address.contains(searchText, true) ||
                it.description.contains(searchText, true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Trang chủ", fontSize = 22.sp, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            BottomNavBar(selectedIndex = selectedTab, onTabSelected = onTabSelected)
        }
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 12.dp)
        ) {
            Spacer(Modifier.height(16.dp))
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                placeholder = { Text("Tìm kiếm công việc...") },
                leadingIcon = { Icon(painterResource(id = R.drawable.outline_search_24), null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            Spacer(Modifier.height(16.dp))
            Text("Việc làm gợi ý cho bạn", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                jobs.forEach { job ->
                    JobCardM3(job)
                }
            }
        }
    }
}

@Composable
fun JobCardM3(job: Job) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE8EAF6)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = job.iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp)
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(job.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(job.address, color = Color.Gray, fontSize = 13.sp)
                Text(job.description, color = Color.Gray, fontSize = 13.sp)
                Spacer(Modifier.height(4.dp))
                Text(job.wage, color = job.wageColor, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
            Spacer(Modifier.width(8.dp))
            Button(
                onClick = { /* Xử lý đăng ký việc */ },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                modifier = Modifier.height(36.dp)
            ) {
                Text("Đăng Kí", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }
    }
}
