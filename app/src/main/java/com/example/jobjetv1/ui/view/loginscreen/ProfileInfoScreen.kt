package com.example.jobjetv1.ui.view.loginscreen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    onProfileComplete: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    var showDatePicker by remember { mutableStateOf(false) }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let { viewModel.onImageSelected(it) }
        }
    )

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
                    .background(Color.LightGray, shape = CircleShape)
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
                onValueChange = { newValue -> viewModel.onFullNameChanged(newValue) },
                label = { Text("Họ và tên") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Date of Birth
            OutlinedTextField(
                value = viewModel.dateOfBirth,
                onValueChange = { },
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
                onValueChange = { },
                label = { Text("Số điện thoại") },
                modifier = Modifier.fillMaxWidth(),
                enabled = false
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Email
            OutlinedTextField(
                value = viewModel.email,
                onValueChange = { newValue -> viewModel.onEmailChanged(newValue) },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(16.dp))

            // CMND/CCCD
            OutlinedTextField(
                value = viewModel.idNumber,
                onValueChange = { newValue -> viewModel.onIdNumberChanged(newValue) },
                label = { Text("Số CMND/CCCD") },
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Number
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Address
            OutlinedTextField(
                value = viewModel.address,
                onValueChange = { newValue -> viewModel.onAddressChanged(newValue) },
                label = { Text("Địa chỉ") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Continue Button
            Button(
                onClick = { viewModel.saveProfile(onProfileComplete) },
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

            Spacer(modifier = Modifier.height(16.dp))

            // Logout Button
            Button(
                onClick = { viewModel.logout(onLogout) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Đăng xuất", color = MaterialTheme.colorScheme.onError)
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
                    .clickable(enabled = false) { },
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = Calendar.getInstance().timeInMillis
        )
        
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
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
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDatePicker = false }
                ) {
                    Text("Huỷ")
                }
            }
        ) {
            Column {
                DatePicker(
                    state = datePickerState,
                    showModeToggle = false
                )
            }
        }
    }
}
