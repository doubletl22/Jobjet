package com.example.jobjetv1.ui.view.functionhomescreen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.res.painterResource
import com.example.jobjetv1.data.model.Job
import com.example.jobjetv1.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationScreen(
    job: Job?,
    onBack: () -> Unit,
    onSubmit: (ApplicationData) -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var experience by remember { mutableStateOf("") }
    var motivation by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ứng tuyển công việc") },
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Job info header
            item {
                job?.let { 
                    JobApplicationHeader(it)
                }
            }
            
            // Application form
            item {
                ApplicationForm(
                    fullName = fullName,
                    onFullNameChange = { fullName = it },
                    phone = phone,
                    onPhoneChange = { phone = it },
                    email = email,
                    onEmailChange = { email = it },
                    experience = experience,
                    onExperienceChange = { experience = it },
                    motivation = motivation,
                    onMotivationChange = { motivation = it }
                )
            }
            
            // Submit button
            item {
                Button(
                    onClick = {
                        isSubmitting = true
                        val applicationData = ApplicationData(
                            jobId = job?.id ?: "",
                            fullName = fullName,
                            phone = phone,
                            email = email,
                            experience = experience,
                            motivation = motivation
                        )
                        onSubmit(applicationData)
                    },
                    enabled = !isSubmitting && fullName.isNotBlank() && phone.isNotBlank() && email.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2196F3)
                    )
                ) {
                    if (isSubmitting) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Icon(Icons.Default.Send, contentDescription = null, tint = Color.White)
                        Spacer(Modifier.width(8.dp))
                        Text("Gửi đơn ứng tuyển", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
            
            item {
                Spacer(Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun JobApplicationHeader(job: Job) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Bạn đang ứng tuyển cho:",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = job.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = job.address,
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = job.wage,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = job.wageColor
            )
        }
    }
}

@Composable
fun ApplicationForm(
    fullName: String,
    onFullNameChange: (String) -> Unit,
    phone: String,
    onPhoneChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    experience: String,
    onExperienceChange: (String) -> Unit,
    motivation: String,
    onMotivationChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Thông tin ứng viên",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            
            // Họ tên
            OutlinedTextField(
                value = fullName,
                onValueChange = onFullNameChange,
                label = { Text("Họ và tên *") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null)
                }
            )
            
            // Số điện thoại
            OutlinedTextField(
                value = phone,
                onValueChange = onPhoneChange,
                label = { Text("Số điện thoại *") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                leadingIcon = {
                    Icon(Icons.Default.Phone, contentDescription = null)
                }
            )
            
            // Email
            OutlinedTextField(
                value = email,
                onValueChange = onEmailChange,
                label = { Text("Email *") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                leadingIcon = {
                    Icon(Icons.Default.Email, contentDescription = null)
                }
            )
            
            // Kinh nghiệm
            OutlinedTextField(
                value = experience,
                onValueChange = onExperienceChange,
                label = { Text("Kinh nghiệm làm việc") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                minLines = 3,
                maxLines = 5,
                leadingIcon = {
                    Icon(Icons.Default.Search,
                        contentDescription = null)
                }
            )
            
            // Động lực ứng tuyển
            OutlinedTextField(
                value = motivation,
                onValueChange = onMotivationChange,
                label = { Text("Lý do muốn ứng tuyển") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                minLines = 3,
                maxLines = 5,
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_lightbulb_24), // Dùng painterResource
                        contentDescription = null
                    )
                }
            )
            
            Text(
                text = "* Trường bắt buộc",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}

data class ApplicationData(
    val jobId: String,
    val fullName: String,
    val phone: String,
    val email: String,
    val experience: String,
    val motivation: String
)
