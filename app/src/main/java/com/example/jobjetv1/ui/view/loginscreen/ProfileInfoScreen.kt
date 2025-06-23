package com.example.jobjetv1.ui.view.loginscreen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.jobjetv1.R
import com.example.jobjetv1.viewmodel.ProfileInfoViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileInfoScreen(
    viewModel: ProfileInfoViewModel,
    onProfileComplete: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var showDatePicker by remember { mutableStateOf(false) }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.onImageSelected(it) }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // Header
            Text(
            text = "Thông tin cá nhân",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Profile Image
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(Color.LightGray, shape = MaterialTheme.shapes.circular)
                .clickable { imagePicker.launch("image/*") }
                .align(Alignment.CenterHorizontally),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_camera_24),
                contentDescription = "Add photo",
                tint = Color.White
            )
            Text(
                text = "Chạm để thêm ảnh",
                color = Color.White,
                modifier = Modifier.padding(top = 140.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Full Name
        OutlinedTextField(
            value = viewModel.fullName,
            onValueChange = { viewModel.onFullNameChanged(it) },
            label = { Text("Họ và tên") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Date of Birth
        OutlinedTextField(
            value = viewModel.dateOfBirth,
            onValueChange = {},
            label = { Text("Ngày sinh") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDatePicker = true },
            enabled = false
        )

        // Gender Selection
        Column(modifier = Modifier.padding(vertical = 16.dp)) {
            Text("Giới tính", style = MaterialTheme.typography.bodyLarge)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                RadioButton(
                    selected = viewModel.gender == "Nam",
                    onClick = { viewModel.onGenderChanged("Nam") }
                )
                Text("Nam", modifier = Modifier.padding(start = 8.dp, end = 16.dp))
                
                RadioButton(
                    selected = viewModel.gender == "Nữ",
                    onClick = { viewModel.onGenderChanged("Nữ") }
                )
                Text("Nữ", modifier = Modifier.padding(start = 8.dp, end = 16.dp))
                
                RadioButton(
                    selected = viewModel.gender == "Khác",
                    onClick = { viewModel.onGenderChanged("Khác") }
                )
                Text("Khác", modifier = Modifier.padding(start = 8.dp))
            }
        }

        // Phone Number (disabled, pre-filled)
        OutlinedTextField(
            value = viewModel.phoneNumber,
            onValueChange = {},
            label = { Text("Số điện thoại") },
            modifier = Modifier.fillMaxWidth(),
            enabled = false
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Email
        OutlinedTextField(
            value = viewModel.email,
            onValueChange = { viewModel.onEmailChanged(it) },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Email
        )

        Spacer(modifier = Modifier.height(16.dp))

        // CMND/CCCD
        OutlinedTextField(
            value = viewModel.idNumber,
            onValueChange = { viewModel.onIdNumberChanged(it) },
            label = { Text("Số CMND/CCCD") },
            modifier = Modifier.fillMaxWidth(),
            keyboardType = KeyboardType.Number
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Address
        OutlinedTextField(
            value = viewModel.address,
            onValueChange = { viewModel.onAddressChanged(it) },
            label = { Text("Địa chỉ") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Continue Button
        Button(
            onClick = { viewModel.saveProfile(onSuccess = onProfileComplete) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            enabled = !viewModel.uiState.isLoading
        ) {
            if (viewModel.uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Tiếp tục")
            }
        }

        // Error message
        viewModel.uiState.errorMessage?.let { error ->
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        }

        // Loading overlay
        if (viewModel.uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
                    .clickable(enabled = false) { /* Prevent clicks while loading */ },
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }

    if (showDatePicker) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Huỷ")
                }
            }
        ) {
            DatePicker(
                state = rememberDatePickerState(
                    initialSelectedDateMillis = calendar.timeInMillis
                ),
                showModeToggle = false,
                title = { Text("Chọn ngày sinh") },
                headline = { /* No headline */ },
                colors = DatePickerDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    headlineContentColor = MaterialTheme.colorScheme.onSurface,
                    weekdayContentColor = MaterialTheme.colorScheme.onSurface,
                    subheadContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    yearContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    currentYearContentColor = MaterialTheme.colorScheme.primary,
                    selectedYearContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedYearContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    dayContentColor = MaterialTheme.colorScheme.onSurface,
                    selectedDayContainerColor = MaterialTheme.colorScheme.primary,
                    selectedDayContentColor = MaterialTheme.colorScheme.onPrimary,
                    todayDateBorderColor = MaterialTheme.colorScheme.primary,
                    todayContentColor = MaterialTheme.colorScheme.primary
                ),
                dateValidator = { timestamp ->
                    timestamp <= System.currentTimeMillis()
                },
                onDateSelected = { millis ->
                    val selectedDate = Calendar.getInstance().apply {
                        timeInMillis = millis
                    }
                    val formattedDate = String.format(
                        "%02d/%02d/%d",
                        selectedDate.get(Calendar.DAY_OF_MONTH),
                        selectedDate.get(Calendar.MONTH) + 1,
                        selectedDate.get(Calendar.YEAR)
                    )
                    viewModel.onDateOfBirthChanged(formattedDate)
                }
            )
        }
    }
}
