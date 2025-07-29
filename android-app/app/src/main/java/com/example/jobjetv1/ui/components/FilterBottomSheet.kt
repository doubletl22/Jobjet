package com.example.jobjetv1.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jobjetv1.viewmodel.SearchFilters

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    filters: SearchFilters,
    locationOptions: List<String>,
    jobTypeOptions: List<String>,
    workTimeOptions: List<String>,
    sortOptions: List<Pair<String, String>>,
    appliedFiltersCount: Int,
    onFiltersChange: (SearchFilters) -> Unit,
    onClearFilters: () -> Unit,
    onDismiss: () -> Unit
) {
    var tempFilters by remember { mutableStateOf(filters) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { it != SheetValue.Hidden }
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Bộ lọc tìm kiếm",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Row {
                        if (appliedFiltersCount > 0) {
                            TextButton(onClick = onClearFilters) {
                                Icon(Icons.Default.Clear, contentDescription = null)
                                Spacer(Modifier.width(4.dp))
                                Text("Xóa ($appliedFiltersCount)")
                            }
                        }

                        TextButton(onClick = onDismiss) {
                            Text("Đóng")
                        }
                    }
                }
            }

            item {
                // Salary Range
                FilterSection(title = "Mức lương (VND/giờ)") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = tempFilters.minSalary,
                            onValueChange = {
                                tempFilters = tempFilters.copy(minSalary = it)
                                onFiltersChange(tempFilters)
                            },
                            label = { Text("Từ") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )

                        OutlinedTextField(
                            value = tempFilters.maxSalary,
                            onValueChange = {
                                tempFilters = tempFilters.copy(maxSalary = it)
                                onFiltersChange(tempFilters)
                            },
                            label = { Text("Đến") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            item {
                // Location
                FilterSection(title = "Khu vực") {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(locationOptions) { location ->
                            FilterChip(
                                selected = tempFilters.location == location,
                                onClick = {
                                    tempFilters = tempFilters.copy(location = location)
                                    onFiltersChange(tempFilters)
                                },
                                label = { Text(location) }
                            )
                        }
                    }
                }
            }

            item {
                // Job Type
                FilterSection(title = "Loại công việc") {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(jobTypeOptions) { jobType ->
                            FilterChip(
                                selected = tempFilters.jobType == jobType,
                                onClick = {
                                    tempFilters = tempFilters.copy(jobType = jobType)
                                    onFiltersChange(tempFilters)
                                },
                                label = { Text(jobType) }
                            )
                        }
                    }
                }
            }

            item {
                // Work Time
                FilterSection(title = "Thời gian làm việc") {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(workTimeOptions) { workTime ->
                            FilterChip(
                                selected = tempFilters.workTime == workTime,
                                onClick = {
                                    tempFilters = tempFilters.copy(workTime = workTime)
                                    onFiltersChange(tempFilters)
                                },
                                label = { Text(workTime) }
                            )
                        }
                    }
                }
            }

            item {
                // Sort By
                FilterSection(title = "Sắp xếp theo") {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(sortOptions) { (sortKey, sortLabel) ->
                            FilterChip(
                                selected = tempFilters.sortBy == sortKey,
                                onClick = {
                                    tempFilters = tempFilters.copy(sortBy = sortKey)
                                    onFiltersChange(tempFilters)
                                },
                                label = { Text(sortLabel) }
                            )
                        }
                    }
                }
            }

            item {
                // Apply Button
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Áp dụng bộ lọc", modifier = Modifier.padding(vertical = 8.dp))
                }
            }
        }
    }
}

@Composable
private fun FilterSection(
    title: String,
    content: @Composable () -> Unit
) {
    Column {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1976D2),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        content()
    }
}