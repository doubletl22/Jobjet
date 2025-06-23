package com.example.jobjetv1.ui.view.mainscreen

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jobjetv1.viewmodel.SearchViewModel
import com.example.jobjetv1.data.model.JobCategory
import com.example.jobjetv1.data.model.Job
import com.example.jobjetv1.R
import com.example.jobjetv1.ui.view.BottomNavBar
import com.example.jobjetv1.ui.components.JobCard


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = viewModel(),
    selectedTab: Int = 1,
    onTabSelected: (Int) -> Unit = {},
    onJobClick: (Job) -> Unit = {}
) {
    val searchText = viewModel.searchText
    val categories = viewModel.categories
    val suggestions = viewModel.suggestionChips
    val recents = viewModel.recentSearches
    val uiState = viewModel.uiState
    val searchFilters = viewModel.searchFilters

    var showFilters by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Tìm kiếm",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                actions = {
                    // Filter button with badge
                    Box {
                        IconButton(onClick = { showFilters = true }) {
                            Icon(
                                Icons.Default.FilterList,
                                contentDescription = "Bộ lọc",
                                tint = if (uiState.appliedFiltersCount > 0) Color(0xFF1976D2) else Color.Gray
                            )
                        }

                        if (uiState.appliedFiltersCount > 0) {
                            Badge(
                                modifier = Modifier.align(Alignment.TopEnd)
                            ) {
                                Text(
                                    uiState.appliedFiltersCount.toString(),
                                    fontSize = 10.sp
                                )
                            }
                        }
                    }
                },
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

            // Search bar
            SearchBar(
                searchText = searchText,
                onSearchTextChange = viewModel::updateSearchText,
                onSearch = {
                    viewModel.addRecent(searchText)
                    viewModel.searchJobs()
                }
            )

            Spacer(Modifier.height(12.dp))

            // Quick Filter Chips
            QuickFilterChips(
                currentFilters = searchFilters,
                onQuickFilter = { filterType, value ->
                    when (filterType) {
                        "salary_high" -> {
                            // Filter jobs with salary >= 35k and sort by salary high
                            viewModel.updateFilters(searchFilters.copy(
                                minSalary = "35000",
                                sortBy = "salary_high"
                            ))
                        }
                        "near_me" -> viewModel.updateFilters(searchFilters.copy(location = "Quận 1")) // Demo
                        "part_time" -> viewModel.updateFilters(searchFilters.copy(jobType = "Bán thời gian"))
                        "weekend" -> viewModel.updateFilters(searchFilters.copy(workTime = "Cuối tuần"))
                        "no_exp" -> viewModel.selectSuggestion("Không cần kinh nghiệm")
                    }
                }
            )

            Spacer(Modifier.height(18.dp))

            // Show different content based on search state
            if (searchText.isEmpty()) {
                // Show categories, suggestions, and recents when not searching
                SearchDefaultContent(
                    categories = categories,
                    suggestions = suggestions,
                    recents = recents,
                    onCategoryClick = { category ->
                        viewModel.selectCategory(category)
                    },
                    onSuggestionClick = { suggestion ->
                        viewModel.selectSuggestion(suggestion)
                    },
                    onRecentDelete = { index ->
                        viewModel.removeRecent(index)
                    }
                )
            } else {
                // Show search results
                SearchResultsContent(
                    uiState = uiState,
                    onJobClick = onJobClick,
                    onRetry = { viewModel.searchJobs() }
                )
            }
        }

        // Filter Bottom Sheet
        if (showFilters) {
            FilterBottomSheetContent(
                filters = searchFilters,
                viewModel = viewModel,
                onDismiss = { showFilters = false }
            )
        }
    }
}

@Composable
private fun SearchBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onSearch: () -> Unit
) {
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
                onValueChange = onSearchTextChange,
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 10.dp),
                textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                decorationBox = { innerTextField ->
                    if (searchText.isEmpty()) {
                        Text("Tìm kiếm công việc...", color = Color(0xFFBDBDBD), fontSize = 16.sp)
                    }
                    innerTextField()
                }
            )

            if (searchText.isNotEmpty()) {
                IconButton(
                    onClick = { onSearchTextChange("") },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Xóa",
                        tint = Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchDefaultContent(
    categories: List<JobCategory>,
    suggestions: List<String>,
    recents: List<String>,
    onCategoryClick: (String) -> Unit,
    onSuggestionClick: (String) -> Unit,
    onRecentDelete: (Int) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            // Categories Grid
            CategoryGrid(categories = categories, onClick = onCategoryClick)
        }

        item {
            // Quick Actions
            QuickActionsSection(
                onLocationQuickSearch = { location ->
                    onSuggestionClick("Việc làm tại $location")
                },
                onSalaryQuickSearch = { salaryRange ->
                    onSuggestionClick("Lương $salaryRange")
                },
                onWorkTimeQuickSearch = { workTime ->
                    onSuggestionClick("Việc làm $workTime")
                }
            )
        }

        item {
            // Suggestions
            Column {
                Text(
                    "Đề xuất cho bạn",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 10.dp)
                ) {
                    items(suggestions) { chip ->
                        SuggestChip(chip, onClick = { onSuggestionClick(chip) })
                    }
                }
            }
        }

        item {
            // Recent searches
            if (recents.isNotEmpty()) {
                Column {
                    Text(
                        "Tìm kiếm gần đây",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    recents.forEachIndexed { index, search ->
                        RecentSearchItem(
                            query = search,
                            onDelete = { onRecentDelete(index) },
                            onClick = { onSuggestionClick(search) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchResultsContent(
    uiState: com.example.jobjetv1.viewmodel.SearchUiState,
    onJobClick: (Job) -> Unit,
    onRetry: () -> Unit
) {
    when {
        uiState.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(Modifier.height(16.dp))
                    Text("Đang tìm kiếm...", color = Color.Gray)
                }
            }
        }

        uiState.hasError -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(32.dp)
                ) {
                    Icon(
                        Icons.Default.Error,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = Color.Red
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        uiState.errorMessage,
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = onRetry) {
                        Text("Thử lại")
                    }
                }
            }
        }

        uiState.searchResults.isEmpty() -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(32.dp)
                ) {
                    Icon(
                        Icons.Default.SearchOff,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = Color.Gray
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        "Không tìm thấy công việc nào",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Hãy thử thay đổi từ khóa tìm kiếm hoặc bộ lọc",
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                    )
                }
            }
        }

        else -> {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    // Results header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Tìm thấy ${uiState.searchResults.size} công việc",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF1976D2)
                        )
                    }
                }

                items(uiState.searchResults) { job ->
                    JobCard(
                        job = job,
                        onClick = { onJobClick(job) },
                        onBookmarkClick = { /* TODO: Implement bookmark functionality */ },
                        isBookmarked = false
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterBottomSheetContent(
    filters: com.example.jobjetv1.viewmodel.SearchFilters,
    viewModel: SearchViewModel,
    onDismiss: () -> Unit
) {
    // Simple filter dialog implementation
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Bộ lọc tìm kiếm",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text("Khu vực", fontWeight = FontWeight.Medium)
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(viewModel.locationOptions.take(6)) { location ->
                            FilterChip(
                                selected = filters.location == location,
                                onClick = {
                                    viewModel.updateFilters(filters.copy(location = location))
                                },
                                label = { Text(location, fontSize = 12.sp) }
                            )
                        }
                    }
                }

                item {
                    Text("Loại công việc", fontWeight = FontWeight.Medium)
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(viewModel.jobTypeOptions) { jobType ->
                            FilterChip(
                                selected = filters.jobType == jobType,
                                onClick = {
                                    viewModel.updateFilters(filters.copy(jobType = jobType))
                                },
                                label = { Text(jobType, fontSize = 12.sp) }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Áp dụng")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                viewModel.clearFilters()
                onDismiss()
            }) {
                Text("Xóa tất cả")
            }
        }
    )
}

@Composable
fun CategoryGrid(categories: List<JobCategory>, onClick: (String) -> Unit) {
    Column {
        // Header with trending indicator
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Danh mục công việc",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2)
            )

            Text(
                "🔥 Phổ biến",
                fontSize = 12.sp,
                color = Color(0xFFEF6C00),
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(Modifier.height(12.dp))

        // Categories grid
        for (row in 0..1) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                for (col in 0..2) {
                    val idx = row * 3 + col
                    if (idx < categories.size) {
                        val category = categories[idx]
                        EnhancedCategoryCard(
                            category = category,
                            onClick = { onClick(category.label) },
                            modifier = Modifier.weight(1f)
                        )
                    } else {
                        Spacer(Modifier.weight(1f))
                    }
                }
            }
            if (row < 1) Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun EnhancedCategoryCard(
    category: JobCategory,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = tween(150),
        label = "card_scale"
    )

    val shadowElevation by animateDpAsState(
        targetValue = if (isPressed) 1.dp else 4.dp,
        animationSpec = tween(150),
        label = "card_elevation"
    )

    // Gradient background
    val gradientColors = listOf(
        category.color.copy(alpha = 0.15f),
        category.color.copy(alpha = 0.05f)
    )

    Card(
        modifier = modifier
            .scale(scale)
            .clickable { onClick() }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                    }
                )
            }
            .height(120.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = shadowElevation)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(colors = gradientColors) // Explicitly use `colors` parameter
                )
                .padding(12.dp)
        ) {
            // Popular badge (top-left)
            if (category.isPopular) {
                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            brush = Brush.horizontalGradient(
                                listOf(Color(0xFFFF6B35), Color(0xFFFF8A50))
                            )
                        )
                        .padding(horizontal = 6.dp, vertical = 3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "🔥",
                        fontSize = 10.sp
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        "HOT",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            // Job count badge (top-right)
            if (category.jobCount > 0) {
                Surface(
                    modifier = Modifier.align(Alignment.TopEnd),
                    shape = RoundedCornerShape(10.dp),
                    color = category.color.copy(alpha = 0.2f),
                    shadowElevation = 2.dp
                ) {
                    Text(
                        "${category.jobCount}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = category.color,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }

            // Main content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = if (category.isPopular || category.jobCount > 0) 24.dp else 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Icon with animated background
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf( // Explicitly use `colors` parameter
                                    category.color.copy(alpha = 0.3f),
                                    category.color.copy(alpha = 0.1f)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = category.icon,
                        contentDescription = category.label,
                        modifier = Modifier.size(22.dp),
                        tint = category.color
                    )
                }

                // Title and description
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        category.label,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = category.color,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    if (category.salary.isNotEmpty()) {
                        Text(
                            category.salary,
                            fontSize = 10.sp,
                            color = Color(0xFF666666),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium
                        )
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
fun RecentSearchItem(
    query: String,
    onDelete: () -> Unit,
    onClick: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 7.dp)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.outline_timer_24), // Explicitly use `painter` parameter
            contentDescription = null,
            tint = Color(0xFFBDBDBD),
            modifier = Modifier.size(18.dp)
        )
        Spacer(Modifier.width(7.dp))
        Text(
            query,
            fontSize = 15.sp,
            modifier = Modifier.weight(1f)
        )
        IconButton(
            onClick = onDelete,
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = "Xóa",
                tint = Color(0xFFBDBDBD),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun QuickFilterChips(
    currentFilters: com.example.jobjetv1.viewmodel.SearchFilters,
    onQuickFilter: (String, String) -> Unit
) {
    val filters = listOf(
        "salary_high" to "Lương cao",
        "near_me" to "Gần tôi",
        "part_time" to "Bán thời gian",
        "weekend" to "Cuối tuần",
        "no_exp" to "Không cần kinh nghiệm"
    )

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(vertical = 10.dp)
    ) {
        items(filters) { (filterType, label) ->
            val isSelected = when (filterType) {
                "salary_high" -> currentFilters.minSalary == "35000" && currentFilters.sortBy == "salary_high"
                "near_me" -> currentFilters.location == "Quận 1"
                "part_time" -> currentFilters.jobType == "Bán thời gian"
                "weekend" -> currentFilters.workTime == "Cuối tuần"
                "no_exp" -> false // No direct mapping
                else -> false
            }

            FilterChip(
                selected = isSelected,
                onClick = { onQuickFilter(filterType, label) },
                label = { Text(label, fontSize = 12.sp) }
            )
        }
    }
}

@Composable
private fun QuickActionsSection(
    onLocationQuickSearch: (String) -> Unit,
    onSalaryQuickSearch: (String) -> Unit,
    onWorkTimeQuickSearch: (String) -> Unit
) {
    Column {
        Text(
            "Tìm kiếm nhanh",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1976D2),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Popular locations
        QuickActionGroup(
            title = "📍 Khu vực hot",
            items = listOf("Quận 1", "Quận 7", "Gò Vấp", "Bình Thạnh"),
            onItemClick = onLocationQuickSearch
        )

        Spacer(Modifier.height(12.dp))

        // Salary ranges
        QuickActionGroup(
            title = "💰 Mức lương",
            items = listOf("25k+/giờ", "30k+/giờ", "40k+/giờ", "50k+/giờ"),
            onItemClick = onSalaryQuickSearch
        )

        Spacer(Modifier.height(12.dp))

        // Work times
        QuickActionGroup(
            title = "⏰ Thời gian",
            items = listOf("Ca sáng", "Ca chiều", "Cuối tuần", "Linh hoạt"),
            onItemClick = onWorkTimeQuickSearch
        )
    }
}

@Composable
private fun QuickActionGroup(
    title: String,
    items: List<String>,
    onItemClick: (String) -> Unit
) {
    Column {
        Text(
            title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF666666),
            modifier = Modifier.padding(bottom = 6.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items) { item ->
                QuickActionChip(
                    text = item,
                    onClick = { onItemClick(item) }
                )
            }
        }
    }
}

@Composable
private fun QuickActionChip(
    text: String,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFFE3F1FF),
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text,
            fontSize = 13.sp,
            color = Color(0xFF1976D2),
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}