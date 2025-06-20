package com.example.jobjetv1.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.example.jobjetv1.viewmodel.SearchViewModel
import com.example.jobjetv1.data.model.JobCategory
import com.google.accompanist.flowlayout.FlowRow
import com.example.jobjetv1.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    selectedTab: Int = 1,
    onTabSelected: (Int) -> Unit = {}
) {
    val searchText = viewModel.searchText
    val categories = viewModel.categories
    val suggestions = viewModel.suggestionChips
    val recents = viewModel.recentSearches

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tìm kiếm", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
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
                .background(Color.White)
                .padding(horizontal = 12.dp)
        ) {
            Spacer(Modifier.height(8.dp))
            // Thanh tìm kiếm
            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(Color(0xFFF7F7F7))
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray)
                    Spacer(Modifier.width(8.dp))
                    BasicTextField(
                        value = searchText,
                        onValueChange = { viewModel.searchText = it },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                        decorationBox = { innerTextField ->
                            if (searchText.isEmpty()) {
                                Text("Tìm kiếm công việc...", color = Color(0xFFBDBDBD), fontSize = 16.sp)
                            }
                            innerTextField()
                        }
                    )
                }
            }
            Spacer(Modifier.height(18.dp))
            // Lưới ngành nghề
            CategoryGrid(categories = categories, onClick = {})
            Spacer(Modifier.height(18.dp))
            Text("Đề xuất cho bạn", fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(bottom = 8.dp))
            FlowRow(
                mainAxisSpacing = 8.dp,
                crossAxisSpacing = 8.dp,
                modifier = Modifier.padding(bottom = 10.dp)
            ) {
                suggestions.forEach { chip ->
                    SuggestChip(chip, onClick = { viewModel.addRecent(chip) })
                }
            }
            Spacer(Modifier.height(6.dp))
            Text("Tìm kiếm gần đây", fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(bottom = 8.dp))
            recents.forEachIndexed { i, search ->
                RecentSearchItem(
                    query = search,
                    onDelete = { viewModel.removeRecent(i) }
                )
            }
        }
    }
}

@Composable
fun CategoryGrid(categories: List<JobCategory>, onClick: (String) -> Unit) {
    Column {
        for (row in 0..1) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                for (col in 0..2) {
                    val idx = row * 3 + col
                    if (idx < categories.size) {
                        val cat = categories[idx]
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(6.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFFF3F6FA))
                                .clickable { onClick(cat.label) }
                                .padding(vertical = 14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE3F1FF)),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(cat.icon),
                                    contentDescription = cat.label,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(Modifier.height(7.dp))
                            Text(cat.label, fontSize = 13.sp, color = Color(0xFF1976D2), fontWeight = FontWeight.Medium)
                        }
                    } else {
                        Spacer(Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun SuggestChip(label: String, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = Color(0xFFF3F6FA),
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(label, modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp), fontSize = 14.sp, color = Color(0xFF1976D2))
    }
}

@Composable
fun RecentSearchItem(query: String, onDelete: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 7.dp)
    ) {
        Icon(painterResource(id = R.drawable.outline_timer_24),
            contentDescription = null,
            tint = Color(0xFFBDBDBD),
            modifier = Modifier.size(18.dp))
        Spacer(Modifier.width(7.dp))
        Text(query, fontSize = 15.sp)
        Spacer(Modifier.weight(1f))
        IconButton(onClick = onDelete, modifier = Modifier.size(18.dp)) {
            Icon(Icons.Default.Close, contentDescription = "Xóa", tint = Color(0xFFBDBDBD))
        }
    }
}
