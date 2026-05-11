package com.jpalomino502.vivebien.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.jpalomino502.vivebien.core.domain.model.User
import com.jpalomino502.vivebien.core.ui.components.CustomTabRow
import com.jpalomino502.vivebien.feature.profile.ui.ProfileViewModel
import com.jpalomino502.vivebien.navigation.Screen

@Composable
fun PerfilScreen(
    navController: NavController = rememberNavController(),
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.loggedOut) {
        if (uiState.loggedOut) {
            navController.navigate(Screen.Login.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F8F8))) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(24.dp).clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFF4CAF50)).padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) { Text(text = "👤", color = Color.White, fontSize = 12.sp) }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Perfil", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                }
                if (!uiState.isEditMode) {
                    TextButton(onClick = viewModel::onStartEdit) {
                        Text("Editar", color = Color(0xFF4CAF50))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier.size(80.dp).clip(CircleShape)
                    .background(Color(0xFF4CAF50)).align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (uiState.isLoading) "…" else uiState.user.initials.ifBlank { "?" },
                    fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (uiState.isLoading) "Cargando..." else uiState.user.name,
                fontSize = 20.sp, fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = if (uiState.user.id.isNotBlank()) "Usuario: ${uiState.user.id}" else "",
                fontSize = 14.sp, color = Color.Gray,
                textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            var selectedTab by remember { mutableStateOf(0) }
            CustomTabRow(
                tabs = listOf("Información", "Ajustes"),
                selectedIndex = selectedTab,
                onTabSelected = { selectedTab = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (selectedTab) {
                0 -> InformacionTabContent(
                    user = uiState.user,
                    isEditMode = uiState.isEditMode,
                    editName = uiState.editName,
                    editEmail = uiState.editEmail,
                    editPhone = uiState.editPhone,
                    editBirthDate = uiState.editBirthDate,
                    errorMessage = uiState.errorMessage,
                    onNameChanged = viewModel::onEditNameChanged,
                    onEmailChanged = viewModel::onEditEmailChanged,
                    onPhoneChanged = viewModel::onEditPhoneChanged,
                    onBirthDateChanged = viewModel::onEditBirthDateChanged,
                    onSave = viewModel::onSaveProfile,
                    onCancel = viewModel::onCancelEdit
                )
                1 -> AjustesTabContent(
                    notificationsEnabled = uiState.notificationsEnabled,
                    darkModeEnabled = uiState.darkModeEnabled,
                    onNotificationsToggled = viewModel::onNotificationsToggled,
                    onDarkModeToggled = viewModel::onDarkModeToggled,
                    onLogout = viewModel::onLogout
                )
            }
        }
    }
}

@Composable
fun InformacionTabContent(
    user: User,
    isEditMode: Boolean,
    editName: String,
    editEmail: String,
    editPhone: String,
    editBirthDate: String,
    errorMessage: String,
    onNameChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    onPhoneChanged: (String) -> Unit,
    onBirthDateChanged: (String) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Información Personal", fontWeight = FontWeight.Medium, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Datos básicos de tu perfil", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))

            if (isEditMode) {
                CampoEditable("Nombre completo", editName, onNameChanged)
                Spacer(modifier = Modifier.height(12.dp))
                CampoEditable("Correo electrónico", editEmail, onEmailChanged)
                Spacer(modifier = Modifier.height(12.dp))
                CampoEditable("Teléfono", editPhone, onPhoneChanged)
                Spacer(modifier = Modifier.height(12.dp))
                CampoEditable("Fecha de nacimiento", editBirthDate, onBirthDateChanged)

                if (errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = errorMessage, color = Color.Red, fontSize = 13.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = onCancel, modifier = Modifier.weight(1f), shape = RoundedCornerShape(8.dp)) {
                        Text("Cancelar")
                    }
                    Button(
                        onClick = onSave, modifier = Modifier.weight(1f), shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                    ) { Text("Guardar") }
                }
            } else {
                CampoInformacion(etiqueta = "Nombre completo", valor = user.name)
                Spacer(modifier = Modifier.height(16.dp))
                CampoInformacion(etiqueta = "Correo electrónico", valor = user.email.ifBlank { "Sin datos" })
                Spacer(modifier = Modifier.height(16.dp))
                CampoInformacion(etiqueta = "Teléfono", valor = user.phone.ifBlank { "Sin datos" })
                Spacer(modifier = Modifier.height(16.dp))
                CampoInformacion(etiqueta = "Fecha de nacimiento", valor = user.birthDate.ifBlank { "Sin datos" })
            }
        }
    }
}

@Composable
fun CampoEditable(etiqueta: String, valor: String, onValueChange: (String) -> Unit) {
    Column {
        Text(text = etiqueta, fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = valor, onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(), singleLine = true,
            shape = RoundedCornerShape(8.dp)
        )
    }
}

@Composable
fun CampoInformacion(etiqueta: String, valor: String) {
    Column {
        Text(text = etiqueta, fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = valor, onValueChange = { },
            readOnly = true, modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                unfocusedContainerColor = Color(0xFFF5F5F5)
            )
        )
    }
}

@Composable
fun AjustesTabContent(
    notificationsEnabled: Boolean,
    darkModeEnabled: Boolean,
    onNotificationsToggled: (Boolean) -> Unit,
    onDarkModeToggled: (Boolean) -> Unit,
    onLogout: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Ajustes", fontWeight = FontWeight.Medium, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(16.dp))

            AjusteItem(
                titulo = "Notificaciones",
                descripcion = "Recibe alertas sobre tus citas y actividades",
                activado = notificationsEnabled,
                onToggle = onNotificationsToggled
            )
            Spacer(modifier = Modifier.height(12.dp))
            AjusteItem(titulo = "Idioma", descripcion = "Español", esSwitch = false)
            Spacer(modifier = Modifier.height(12.dp))
            AjusteItem(titulo = "Privacidad", descripcion = "Gestiona tus datos personales", esSwitch = false)
            Spacer(modifier = Modifier.height(12.dp))
            AjusteItem(
                titulo = "Tema Oscuro",
                descripcion = "Cambia la apariencia de la aplicación",
                activado = darkModeEnabled,
                onToggle = onDarkModeToggled
            )

            Spacer(modifier = Modifier.height(24.dp))
            Divider()
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935), contentColor = Color.White)
            ) { Text("Cerrar sesión") }
        }
    }
}

@Composable
fun AjusteItem(
    titulo: String,
    descripcion: String,
    activado: Boolean = false,
    esSwitch: Boolean = true,
    onToggle: (Boolean) -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = titulo, fontWeight = FontWeight.Medium, fontSize = 16.sp)
            Text(text = descripcion, fontSize = 14.sp, color = Color.Gray)
        }
        if (esSwitch) {
            Switch(
                checked = activado, onCheckedChange = onToggle,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White, checkedTrackColor = Color(0xFF4CAF50),
                    uncheckedThumbColor = Color.White, uncheckedTrackColor = Color.LightGray
                )
            )
        } else {
            Text(text = ">", fontSize = 18.sp, color = Color.Gray)
        }
    }
}
