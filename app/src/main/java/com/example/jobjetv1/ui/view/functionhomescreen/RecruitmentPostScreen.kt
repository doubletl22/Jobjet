package com.example.jobjetv1.ui.view.functionhomescreen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jobjetv1.R
import com.example.jobjetv1.data.model.JobPostUiState
import com.example.jobjetv1.data.model.WorkType
import com.example.jobjetv1.viewmodel.RecruitmentViewModel
import androidx.navigation.NavController // THÊM IMPORT NÀY

// --- Constants for better maintainability and consistency ---
private val ScreenPadding = 16.dp
private val SectionSpacing = 8.dp
private val FieldSpacing = 12.dp
private val CardCornerRadius = 16.dp
private val ButtonCornerRadius = 12.dp

private val PrimaryBlue = Color(0xFF1976D2)
private val ActionBlue = Color(0xFF2196F3)
private val ScreenBackground = Color(0xFFF6F8FA)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecruitmentPostScreen(
    navController: NavController, // SỬA: thêm tham số này
    viewModel: RecruitmentViewModel = viewModel(),
    onBack: () -> Unit = {},
    onSubmit: (JobPostUiState) -> Unit = {}
) {
    val state = viewModel.jobPostState
    val isSubmitting = viewModel.isSubmitting
    val scrollState = rememberScrollState()
    val wageUnits = listOf("VND/giờ", "VND/tháng", "USD/giờ")
    var expanded by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Đăng bài tuyển dụng", fontWeight = FontWeight.Bold, fontSize = 19.sp) },
                navigationIcon = {
                    TextButton(onClick = onBack) { Text("Huỷ", fontSize = 15.sp, color = PrimaryBlue) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(padding) // Apply padding from Scaffold
                .background(ScreenBackground)
                .padding(bottom = ScreenPadding) // Add padding at the very bottom
        ) {
            Spacer(Modifier.height(FieldSpacing))

            // Thông tin cơ bản
            CardSection("Thông tin cơ bản") {
                OutlinedTextField(
                    value = state.companyName,
                    onValueChange = { viewModel.updateJobPost { copy(companyName = it) } },
                    label = { Text("Tên công ty") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(FieldSpacing))
                OutlinedTextField(
                    value = state.jobTitle,
                    onValueChange = { viewModel.updateJobPost { copy(jobTitle = it) } },
                    label = { Text("Tên công việc") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(FieldSpacing))
                OutlinedTextField(
                    value = state.quantity,
                    onValueChange = { viewModel.updateJobPost { copy(quantity = it) } },
                    label = { Text("Số lượng") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(FieldSpacing))
                Text("Hình thức làm việc:", fontWeight = FontWeight.Medium)
                Spacer(Modifier.height(SectionSpacing))
                Row(modifier = Modifier.fillMaxWidth()) {
                    WorkTypeRadioButton(
                        text = "Toàn thời gian",
                        selected = state.workType == WorkType.FULL_TIME,
                        onClick = { viewModel.updateJobPost { copy(workType = WorkType.FULL_TIME) } },
                        modifier = Modifier.weight(1f)
                    )
                    WorkTypeRadioButton(
                        text = "Bán thời gian",
                        selected = state.workType == WorkType.PART_TIME,
                        onClick = { viewModel.updateJobPost { copy(workType = WorkType.PART_TIME) } },
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(Modifier.height(FieldSpacing))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = state.wage,
                        onValueChange = { viewModel.updateJobPost { copy(wage = it) } },
                        label = { Text("Mức lương") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                    )
                    Spacer(Modifier.width(SectionSpacing))
                    Box {
                        OutlinedButton(
                            onClick = { expanded = true },
                            modifier = Modifier.height(56.dp) // Match TextField height
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
                                        viewModel.updateJobPost { copy(wageUnit = unit) }
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // Mô tả công việc
            CardSection("Mô tả công việc") {
                OutlinedTextField(
                    value = state.description,
                    onValueChange = { viewModel.updateJobPost { copy(description = it) } },
                    label = { Text("Mô tả chi tiết về công việc...") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 4 // Use minLines instead of fixed height
                )
            }
            // Yêu cầu ứng viên
            CardSection("Yêu cầu ứng viên") {
                OutlinedTextField(
                    value = state.requirement,
                    onValueChange = { viewModel.updateJobPost { copy(requirement = it) } },
                    label = { Text("Yêu cầu nhân viên...") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            }
            // Nội quy công việc
            CardSection("Nội quy công việc") {
                OutlinedTextField(
                    value = state.rule,
                    onValueChange = { viewModel.updateJobPost { copy(rule = it) } },
                    label = { Text("Mô tả nội quy") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            }
            // Địa điểm làm việc
            CardSection("Địa điểm làm việc") {
                OutlinedTextField(
                    value = state.address,
                    onValueChange = { viewModel.updateJobPost { copy(address = it) } },
                    leadingIcon = {
                        Icon(
                            painterResource(R.drawable.outline_location_on_24),
                            contentDescription = null,
                            tint = ActionBlue
                        )
                    },
                    label = { Text("Nhập địa chỉ làm việc") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(FieldSpacing))
                // Hình bản đồ mô phỏng
                Image(
                    painter = painterResource(R.drawable.outline_map_24),
                    contentDescription = "Bản đồ",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .clip(RoundedCornerShape(CardCornerRadius))
                )
            }
            // Thông tin liên hệ
            CardSection("Thông tin liên hệ") {
                OutlinedTextField(
                    value = state.contactName,
                    onValueChange = { viewModel.updateJobPost { copy(contactName = it) } },
                    label = { Text("Tên người liên hệ") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(FieldSpacing))
                OutlinedTextField(
                    value = state.contactEmail,
                    onValueChange = { viewModel.updateJobPost { copy(contactEmail = it) } },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
                Spacer(Modifier.height(FieldSpacing))
                OutlinedTextField(
                    value = state.contactPhone,
                    onValueChange = { viewModel.updateJobPost { copy(contactPhone = it) } },
                    label = { Text("Số điện thoại") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )
            }
            Spacer(Modifier.height(18.dp))
            // Nút đăng tuyển
            Button(
                onClick = {
                    Log.d("RecruitmentPostScreen", "Submit button clicked")
                    errorMessage = null // Clear previous error
                    viewModel.validateAndSubmit(
                        onSuccess = { jobPost ->
                            Log.d("RecruitmentPostScreen", "Job submitted successfully: ${jobPost.jobTitle}")
                            // --- BẮT ĐẦU ĐOẠN SỬA ĐỔI ---
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("need_reload", true)
                            navController.popBackStack()
                            // --- KẾT THÚC ĐOẠN SỬA ĐỔI ---
                        },
                        onError = { error ->
                            Log.d("RecruitmentPostScreen", "Job submission failed: $error")
                            errorMessage = error
                        }
                    )
                },
                enabled = !isSubmitting,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = ScreenPadding)
                    .height(50.dp),
                shape = RoundedCornerShape(ButtonCornerRadius),
                colors = ButtonDefaults.buttonColors(containerColor = ActionBlue)
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.5.dp
                    )
                    Spacer(Modifier.width(SectionSpacing))
                    Text("Đang đăng bài...", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                } else {
                    Text("Đăng tuyển ngay", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                }
            }

            // Hiển thị error message nếu có
            errorMessage?.let { error ->
                Spacer(Modifier.height(SectionSpacing))
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = ScreenPadding)
                )
            }

            // Add space for system navigation bar if present
            Spacer(Modifier.navigationBarsPadding())
        }
    }
}

@Composable
fun CardSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(
        shape = RoundedCornerShape(CardCornerRadius),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = ScreenPadding, vertical = SectionSpacing)
    ) {
        Column(Modifier.padding(ScreenPadding)) {
            Text(title, color = PrimaryBlue, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(Modifier.height(FieldSpacing))
            content()
        }
    }
}

@Composable
private fun WorkTypeRadioButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )
        Spacer(Modifier.width(4.dp))
        Text(text, fontSize = 14.sp)
    }
}