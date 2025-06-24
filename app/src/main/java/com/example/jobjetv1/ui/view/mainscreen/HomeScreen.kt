package com.example.jobjetv1.ui.view.mainscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn // Import LazyColumn
import androidx.compose.foundation.lazy.items // Import items cho LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import android.util.Log
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.jobjetv1.viewmodel.HomeViewModel
import com.example.jobjetv1.viewmodel.SavedJobsViewModel
import com.example.jobjetv1.data.model.Job
import com.example.jobjetv1.ui.view.BottomNavBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController, // THÊM navController
    viewModel: HomeViewModel = viewModel(),
    savedJobsViewModel: SavedJobsViewModel? = null,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    onJobClick: (Job) -> Unit = {}
) {
    LaunchedEffect(Unit) {
        viewModel.loadJobsFromFirebase()
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    val needReload = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<Boolean>("need_reload")
    LaunchedEffect(needReload) {
        needReload?.observe(lifecycleOwner) { shouldReload ->
            if (shouldReload == true) {
                viewModel.loadJobsFromFirebase()
                navController.currentBackStackEntry?.savedStateHandle?.set("need_reload", false)
            }
        }
    }
    val jobs by viewModel.jobs.collectAsState()

    var searchText by remember { mutableStateOf("") }
    val filteredJobs = jobs.filter {
        it.jobTitle.contains(searchText, ignoreCase = true) ||
                it.address.contains(searchText, ignoreCase = true) ||
                it.description.contains(searchText, ignoreCase = true)
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
        // Sử dụng LazyColumn thay vì Column thông thường
        LazyColumn( // Thay thế Column bằng LazyColumn
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp) // Áp dụng spacing cho LazyColumn
        ) {
            item { // Thêm các phần tử không phải là item của danh sách vào đây
                Spacer(Modifier.height(16.dp))
                Text("Việc làm gợi ý cho bạn (${filteredJobs.size})", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }

            // Sử dụng items để hiển thị danh sách Job
            items(filteredJobs, key = { it.id }) { job ->
                JobCardM3(
                    job = job,
                    onJobClick = onJobClick,
                    savedJobsViewModel = savedJobsViewModel
                )
            }

            item { // Thêm các phần tử cuối cùng nếu có
                Spacer(Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun JobCardM3(
    job: Job,
    onJobClick: (Job) -> Unit = {},
    savedJobsViewModel: SavedJobsViewModel? = null
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onJobClick(job) }
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
                Text(
                    job.jobTitle,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(
                    text = job.address,
                    color = Color.Gray,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = job.description,
                    color = Color.Gray,
                    fontSize = 13.sp,
                    maxLines = 1, // Giới hạn chỉ 1 dòng
                    overflow = TextOverflow.Ellipsis // Hiển thị dấu ... nếu tràn
                )
                Spacer(Modifier.height(4.dp))
                Text(job.wage, color = job.wageColor, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
            Spacer(Modifier.width(8.dp))

            // Nút Lưu việc làm
            savedJobsViewModel?.let { viewModel ->
                val isJobSaved = viewModel.isJobSaved(job.id)

                // Debug log khi state thay đổi
                LaunchedEffect(isJobSaved) {
                    Log.d("HomeScreen", "Job ${job.title} saved state changed to: $isJobSaved")
                }

                IconButton(
                    onClick = {
                        Log.d("HomeScreen", "Save/Unsave clicked for job: ${job.title}")
                        if (isJobSaved) {
                            viewModel.removeSavedJob(job.id)
                        } else {
                            viewModel.saveJob(job)
                        }
                    },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        if (isJobSaved) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = if (isJobSaved) "Bỏ lưu" else "Lưu việc làm",
                        tint = if (isJobSaved) Color(0xFF2196F3) else Color.Gray,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(Modifier.width(4.dp))

            Button(
                onClick = {
                    // Ngăn việc click lan ra JobCard khi click button
                    // Xử lý đăng ký việc ở đây
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3)),
                modifier = Modifier.height(36.dp)
            ) {
                Text("Đăng Kí", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }
    }
}