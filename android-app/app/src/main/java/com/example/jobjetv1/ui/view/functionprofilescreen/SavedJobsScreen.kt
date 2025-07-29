package com.example.jobjetv1.ui.view.functionprofilescreen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import com.example.jobjetv1.data.model.Job
import com.example.jobjetv1.viewmodel.SavedJobsViewModel
import com.example.jobjetv1.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedJobsScreen(
    viewModel: SavedJobsViewModel,
    onBack: () -> Unit,
    onJobClick: (Job) -> Unit = {}
) {
    val savedJobs = viewModel.savedJobs

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Việc làm đã lưu", 
                        fontSize = 20.sp, 
                        fontWeight = FontWeight.Bold
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        if (savedJobs.isEmpty()) {
            // Empty state
            EmptySavedJobsState(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        } else {
            // List of saved jobs
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "Bạn đã lưu ${savedJobs.size} công việc",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                
                items(savedJobs, key = { it.id }) { job ->
                    SavedJobCard(
                        job = job,
                        onJobClick = { onJobClick(job) },
                        onRemove = { viewModel.removeSavedJob(job.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun EmptySavedJobsState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Empty icon
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(
                    Color(0xFF2196F3).copy(alpha = 0.1f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_bookmark_border_24),
                contentDescription = null,
                modifier = Modifier.size(60.dp),
                tint = Color(0xFF2196F3)
            )
        }
        
        Spacer(Modifier.height(24.dp))
        
        Text(
            text = "Chưa có việc làm nào được lưu",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF424242)
        )
        
        Spacer(Modifier.height(8.dp))
        
        Text(
            text = "Hãy lưu những công việc bạn quan tâm để xem lại sau",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 32.dp)
        )
    }
}

@Composable
fun SavedJobCard(
    job: Job,
    onJobClick: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onJobClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Job icon
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(job.wageColor.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = job.iconRes),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = job.wageColor
                )
            }
            
            Spacer(Modifier.width(12.dp))
            
            // Job info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = job.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Spacer(Modifier.height(4.dp))
                
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = Color.Gray
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = job.address,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                
                Spacer(Modifier.height(4.dp))
                
                Text(
                    text = job.wage,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = job.wageColor
                )
            }
            
            // Remove button
            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.outline_delete_24),
                    contentDescription = "Xóa khỏi danh sách lưu",
                    tint = Color(0xFFE57373),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
