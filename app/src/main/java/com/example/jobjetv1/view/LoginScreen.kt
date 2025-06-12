package com.example.jobjetv1.view

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.jobjetv1.R
import com.example.jobjetv1.viewmodel.LoginEvent
import com.example.jobjetv1.viewmodel.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun LoginScreen(navController: NavController, loginViewModel: LoginViewModel = viewModel()) {
    val context = LocalContext.current
    val activity = context as? Activity
    val webClientId = stringResource(id = R.string.default_web_client_id)
    val googleSignInOptions = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .build()
    }
    val googleSignInClient = remember {
        GoogleSignIn.getClient(context, googleSignInOptions)
    }

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                val idToken = account.idToken!!
                loginViewModel.signInWithGoogle(idToken)
            } catch (e: ApiException) {
                Toast.makeText(context, "Lỗi Google Sign-In: ${e.statusCode}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Lắng nghe các sự kiện từ ViewModel
    LaunchedEffect(key1 = context) {
        loginViewModel.event.collect { event ->
            when (event) {
                is LoginEvent.NavigateToOtp -> {
                    // Điều hướng tới màn hình OTP và truyền verificationId
                    navController.navigate("otp/${event.verificationId}/${loginViewModel.phoneNumber.value}")
                    loginViewModel.onEventHandled() // Reset event
                }
                is LoginEvent.ShowError -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                    loginViewModel.onEventHandled() // Reset event
                }
                is LoginEvent.VerificationCompleted -> {
                    // Đăng nhập thành công, đi thẳng đến màn hình success
                    navController.navigate("success") {
                        popUpTo("login") { inclusive = true }
                    }
                    loginViewModel.onEventHandled()
                }
                LoginEvent.Idle -> {
                    // Không làm gì
                }
            }
        }
    }

    val phoneNumber by loginViewModel.phoneNumber.collectAsState()
    val isLoading by loginViewModel.isLoading.collectAsState()

    Scaffold(
        content = { padding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.image1), // Thêm logo của bạn vào drawable
                    contentDescription = "App Logo",
                    modifier = Modifier.size(80.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text("Chào mừng!", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                Text(
                    "Đăng nhập để tiếp tục",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Spacer(modifier = Modifier.height(48.dp))

                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { loginViewModel.onPhoneNumberChanged(it) },
                    label = { Text("Nhập số điện thoại của bạn") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (activity != null) {
                            loginViewModel.sendOtp(activity)
                        } else {
                            Toast.makeText(context, "Lỗi ứng dụng.", Toast.LENGTH_SHORT).show()
                        }
                    },
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp)
                )  {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                    } else {
                        Text("TIẾP TỤC")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text("HOẶC", color = Color.Gray)
                Spacer(modifier = Modifier.height(24.dp))

                // Nút đăng nhập với Google (ví dụ)
                Button(
                    onClick = {
                        val signInIntent = googleSignInClient.signInIntent
                        googleSignInLauncher.launch(signInIntent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)
                ) {
                    // Thêm icon Google vào đây
                    Text("Đăng nhập với Google")
                }

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Bằng cách đăng nhập, bạn đồng ý với Điều khoản sử dụng và Chính sách bảo mật của chúng tôi",
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    )
}
