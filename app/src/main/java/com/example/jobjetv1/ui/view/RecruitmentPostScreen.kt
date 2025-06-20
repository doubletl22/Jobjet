package com.example.jobjetv1.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.*
import com.example.jobjetv1.R
import com.example.jobjetv1.data.model.JobPostUiState
import com.example.jobjetv1.data.model.WorkType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecruitmentPostScreen(
    onBack: () -> Unit = {},
    onSubmit: (JobPostUiState) -> Unit = {}
) {
    var state by remember { mutableStateOf(JobPostUiState()) }
    val scrollState = rememberScrollState()
    val wageUnits = listOf("VND/giờ", "VND/tháng", "USD/giờ")
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Đăng bài tuyển dụng", fontWeight = FontWeight.Bold, fontSize = 19.sp) },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Huỷ", fontSize = 15.sp, color = Color(0xFF1976D2)) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(padding)
                .background(Color(0xFFF6F8FA))
                .padding(bottom = 16.dp)
        ) {
            Spacer(Modifier.height(12.dp))
            // Thông tin cơ bản
            CardSection("Thông tin cơ bản") {
                Column {
                    OutlinedTextField(
                        value = state.companyName,
                        onValueChange = { state = state.copy(companyName = it) },
                        label = { Text("Tên công ty") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = state.jobTitle,
                        onValueChange = { state = state.copy(jobTitle = it) },
                        label = { Text("Tên công việc") },
                        modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
                    )
                    OutlinedTextField(
                        value = state.quantity,
                        onValueChange = { state = state.copy(quantity = it) },
                        label = { Text("Số lượng") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 10.dp)
                    ) {
                        Text("Hình thức làm việc:", fontWeight = FontWeight.Medium)
                        Spacer(Modifier.width(12.dp))
                        RadioButton(
                            selected = state.workType == WorkType.FULL_TIME,
                            onClick = { state = state.copy(workType = WorkType.FULL_TIME) }
                        )
                        Text("Toàn thời gian", fontSize = 14.sp)
                        Spacer(Modifier.width(12.dp))
                        RadioButton(
                            selected = state.workType == WorkType.PART_TIME,
                            onClick = { state = state.copy(workType = WorkType.PART_TIME) }
                        )
                        Text("Bán thời gian", fontSize = 14.sp)
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 10.dp)
                    ) {
                        OutlinedTextField(
                            value = state.wage,
                            onValueChange = { state = state.copy(wage = it) },
                            label = { Text("Mức lương") },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(Modifier.width(8.dp))
                        Box {
                            OutlinedButton(
                                onClick = { expanded = true },
                                modifier = Modifier.height(56.dp)
                            ) {
                                Text(state.wageUnit)
                            }
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                wageUnits.forEach { unit ->
                                    DropdownMenuItem(
                                        text = { Text(unit) },
                                        onClick = {
                                            state = state.copy(wageUnit = unit)
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Mô tả công việc
            CardSection("Mô tả công việc") {
                OutlinedTextField(
                    value = state.description,
                    onValueChange = { state = state.copy(description = it) },
                    label = { Text("Mô tả chi tiết về công việc...") },
                    modifier = Modifier.fillMaxWidth().height(95.dp),
                    maxLines = 4,
                    singleLine = false
                )
            }
            // Yêu cầu ứng viên
            CardSection("Yêu cầu ứng viên") {
                OutlinedTextField(
                    value = state.requirement,
                    onValueChange = { state = state.copy(requirement = it) },
                    label = { Text("Yêu cầu nhân viên...") },
                    modifier = Modifier.fillMaxWidth().height(75.dp),
                    maxLines = 3,
                    singleLine = false
                )
            }
            // Nội quy công việc
            CardSection("Nội quy công việc") {
                OutlinedTextField(
                    value = state.rule,
                    onValueChange = { state = state.copy(rule = it) },
                    label = { Text("Mô tả nội quy") },
                    modifier = Modifier.fillMaxWidth().height(75.dp),
                    maxLines = 3,
                    singleLine = false
                )
            }
            // Địa điểm làm việc
            CardSection("Địa điểm làm việc") {
                OutlinedTextField(
                    value = state.address,
                    onValueChange = { state = state.copy(address = it) },
                    leadingIcon = {
                        Icon(
                            painterResource(R.drawable.outline_location_on_24),
                            contentDescription = null,
                            tint = Color(0xFF2196F3)
                        )
                    },
                    label = { Text("Nhập địa chỉ làm việc") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(12.dp))
                // Hình bản đồ mô phỏng (có thể là Image tĩnh, hoặc Google Maps thực nếu cần)
                Image(
                    painter = painterResource(R.drawable.outline_map_24),
                    contentDescription = "Bản đồ",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .clip(RoundedCornerShape(11.dp))
                )
            }
            // Thông tin liên hệ
            CardSection("Thông tin liên hệ") {
                OutlinedTextField(
                    value = state.contactName,
                    onValueChange = { state = state.copy(contactName = it) },
                    label = { Text("Tên người liên hệ") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = state.contactEmail,
                    onValueChange = { state = state.copy(contactEmail = it) },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
                )
                OutlinedTextField(
                    value = state.contactPhone,
                    onValueChange = { state = state.copy(contactPhone = it) },
                    label = { Text("Số điện thoại") },
                    modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )
            }
            Spacer(Modifier.height(15.dp))
            // Nút đăng tuyển
            Button(
                onClick = { onSubmit(state) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(11.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
            ) {
                Text("Đăng tuyển ngay", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
            }
            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
fun CardSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        shape = RoundedCornerShape(17.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 11.dp, vertical = 7.dp)
    ) {
        Column(Modifier.padding(14.dp)) {
            Text(title, color = Color(0xFF1976D2), fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(Modifier.height(9.dp))
            content()
        }
    }
}
