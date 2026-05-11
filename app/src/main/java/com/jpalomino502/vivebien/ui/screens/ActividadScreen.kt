package com.jpalomino502.vivebien.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jpalomino502.vivebien.core.domain.model.ActivityRecord
import com.jpalomino502.vivebien.core.domain.model.DailyStats
import com.jpalomino502.vivebien.core.ui.components.CustomTabRow
import com.jpalomino502.vivebien.feature.activity.ui.ActivityViewModel

@Composable
fun ActividadScreen(viewModel: ActivityViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val stats = uiState.dailyStats

    if (uiState.showAddDialog) {
        AddActividadDialog(
            onDismiss = viewModel::onHideAddDialog,
            onConfirm = { title, duration, distance, scheduledTime ->
                viewModel.onAddActivity(title, duration, distance, scheduledTime)
            },
            errorMessage = uiState.errorMessage
        )
    }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F8F8))) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
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
                    ) { Text(text = "📊", color = Color.White, fontSize = 12.sp) }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Actividad", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = viewModel::onShowAddDialog,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    shape = RoundedCornerShape(8.dp)
                ) { Text("+ Agregar") }
            }

            Spacer(modifier = Modifier.height(16.dp))

            ResumenDiarioCard(stats = stats)

            Spacer(modifier = Modifier.height(16.dp))

            var selectedTab by remember { mutableStateOf(0) }
            CustomTabRow(
                tabs = listOf("Hoy", "Semana", "Plan"),
                selectedIndex = selectedTab,
                onTabSelected = { selectedTab = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (selectedTab) {
                0 -> ActividadesHoyContent(
                    activities = uiState.todayActivities,
                    isLoading = uiState.isLoading,
                    onToggle = viewModel::onToggleActivity
                )
                else -> ProximamenteContent()
            }
        }
    }
}

@Composable
private fun ResumenDiarioCard(stats: DailyStats) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50))
    ) {
        Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier.size(120.dp).clip(CircleShape).background(Color(0x33FFFFFF)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier.size(100.dp).clip(CircleShape).background(Color(0x66FFFFFF)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("${stats.goalPercent}%", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Objetivo", fontSize = 14.sp, color = Color.White.copy(alpha = 0.8f))
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Pasos", fontSize = 14.sp, color = Color.White.copy(alpha = 0.8f))
                    Text("%,d".format(stats.steps), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Calorías", fontSize = 14.sp, color = Color.White.copy(alpha = 0.8f))
                    Text("${stats.calories} kcal", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Distancia", fontSize = 14.sp, color = Color.White.copy(alpha = 0.8f))
                    Text("%.1f km".format(stats.distanceKm), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

@Composable
private fun ActividadesHoyContent(
    activities: List<ActivityRecord>,
    isLoading: Boolean,
    onToggle: (ActivityRecord) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Actividades de Hoy", fontWeight = FontWeight.Medium, fontSize = 18.sp)
            Text(
                java.time.LocalDate.now().format(
                    java.time.format.DateTimeFormatter.ofPattern("d 'de' MMMM, yyyy", java.util.Locale("es"))
                ),
                fontSize = 14.sp, color = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))

            when {
                isLoading -> Box(modifier = Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF4CAF50))
                }
                activities.isEmpty() -> Box(modifier = Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                    Text("No hay actividades hoy.\nToca '+ Agregar' para añadir una.", color = Color.Gray, fontSize = 14.sp)
                }
                else -> activities.forEachIndexed { index, activity ->
                    if (index > 0) Spacer(modifier = Modifier.height(12.dp))
                    ActividadItem(activity = activity, onToggle = onToggle)
                }
            }
        }
    }
}

@Composable
private fun ProximamenteContent() {
    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
        Text("Próximamente", color = Color.Gray, fontSize = 16.sp)
    }
}

@Composable
fun ActividadItem(activity: ActivityRecord, onToggle: (ActivityRecord) -> Unit = {}) {
    val color = when {
        activity.completed -> Color(0xFF4CAF50)
        activity.scheduledTime != null -> Color(0xFF9C27B0)
        else -> Color(0xFF2196F3)
    }

    Card(
        modifier = Modifier.fillMaxWidth().clickable { onToggle(activity) },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (activity.completed) Color(0xFFE8F5E9) else Color(0xFFFCE4EC)
        )
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(36.dp).clip(CircleShape).background(color),
                contentAlignment = Alignment.Center
            ) { Text(text = "📊", color = Color.White, fontSize = 16.sp) }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = activity.title, fontWeight = FontWeight.Medium, fontSize = 16.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "⏱ ${activity.durationMinutes} min", fontSize = 14.sp, color = Color.Gray)
                    activity.distanceKm?.let {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "• %.1f km".format(it), fontSize = 14.sp, color = Color.Gray)
                    }
                    activity.scheduledTime?.let {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "• $it", fontSize = 14.sp, color = Color.Gray)
                    }
                }
            }

            Box(modifier = Modifier.size(12.dp).clip(CircleShape)
                .background(if (activity.completed) Color(0xFF4CAF50) else Color(0xFFFF9800)))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (activity.completed) "Completado" else "Pendiente",
                fontSize = 12.sp,
                color = if (activity.completed) Color(0xFF4CAF50) else Color(0xFFFF9800)
            )
        }
    }
}

@Composable
private fun AddActividadDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, Int, Float?, String?) -> Unit,
    errorMessage: String
) {
    var title by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var distance by remember { mutableStateOf("") }
    var scheduledTime by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva Actividad") },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                OutlinedTextField(
                    value = title, onValueChange = { title = it },
                    label = { Text("Título *") }, modifier = Modifier.fillMaxWidth(), singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = duration, onValueChange = { duration = it },
                    label = { Text("Duración (minutos) *") }, modifier = Modifier.fillMaxWidth(),
                    singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = distance, onValueChange = { distance = it },
                    label = { Text("Distancia en km (opcional)") }, modifier = Modifier.fillMaxWidth(),
                    singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = scheduledTime, onValueChange = { scheduledTime = it },
                    label = { Text("Hora programada (ej. 5:00 PM)") }, modifier = Modifier.fillMaxWidth(), singleLine = true
                )
                if (errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = errorMessage, color = Color.Red, fontSize = 13.sp)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val dur = duration.toIntOrNull() ?: 0
                val dist = distance.toFloatOrNull()
                onConfirm(title, dur, dist, scheduledTime.takeIf { it.isNotBlank() })
            }) { Text("Guardar", color = Color(0xFF4CAF50)) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}
