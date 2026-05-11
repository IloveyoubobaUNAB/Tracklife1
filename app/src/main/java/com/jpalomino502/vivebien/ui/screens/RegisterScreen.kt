package com.jpalomino502.vivebien.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.jpalomino502.vivebien.feature.auth.ui.RegisterViewModel
import com.jpalomino502.vivebien.navigation.Screen

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.registerSuccess) {
        if (uiState.registerSuccess) {
            navController.navigate(Screen.Main.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopWave(modifier = Modifier.fillMaxWidth().height(160.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(100.dp))

            Text("ViveBien", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50))
            Spacer(modifier = Modifier.height(4.dp))
            Text("Crea tu cuenta", fontSize = 16.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = uiState.displayName,
                onValueChange = viewModel::onDisplayNameChanged,
                label = { Text("Nombre completo") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(8.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = uiState.username,
                onValueChange = viewModel::onUsernameChanged,
                label = { Text("Usuario") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(8.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = uiState.password,
                onValueChange = viewModel::onPasswordChanged,
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(8.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = uiState.confirmPassword,
                onValueChange = viewModel::onConfirmPasswordChanged,
                label = { Text("Confirmar contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (uiState.errorMessage.isNotEmpty()) {
                Text(text = uiState.errorMessage, color = Color.Red, fontSize = 14.sp, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(8.dp))
            }

            Button(
                onClick = viewModel::onRegisterClicked,
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50), contentColor = Color.White)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text("Crear cuenta", fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Text(text = "¿Ya tienes cuenta? ", fontSize = 14.sp, color = Color.Gray)
                Text(
                    text = "Iniciar sesión",
                    fontSize = 14.sp,
                    color = Color(0xFF4CAF50),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable { navController.popBackStack() }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
