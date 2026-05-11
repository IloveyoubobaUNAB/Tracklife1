package com.jpalomino502.vivebien.ui.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jpalomino502.vivebien.core.domain.model.Medication
import com.jpalomino502.vivebien.core.ui.components.CustomTabRow
import com.jpalomino502.vivebien.feature.health.domain.model.BloodPressureData
import com.jpalomino502.vivebien.feature.health.domain.model.HeartRateData
import com.jpalomino502.vivebien.feature.health.ui.HealthViewModel

@Composable
fun SaludScreen(viewModel: HealthViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.showBpDialog) {
        BloodPressureDialog(
            onDismiss = viewModel::onHideBpDialog,
            onConfirm = { s, d -> viewModel.onAddBloodPressure(s, d) },
            errorMessage = uiState.errorMessage
        )
    }
    if (uiState.showHrDialog) {
        HeartRateDialog(
            onDismiss = viewModel::onHideHrDialog,
            onConfirm = { bpm -> viewModel.onAddHeartRate(bpm) },
            errorMessage = uiState.errorMessage
        )
    }
    if (uiState.showMedDialog) {
        AddMedicationDialog(
            onDismiss = viewModel::onHideMedDialog,
            onConfirm = { name, dose, freq -> viewModel.onAddMedication(name, dose, freq) },
            errorMessage = uiState.errorMessage
        )
    }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFF8F8F8))) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(24.dp).clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFF4CAF50)).padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) { Text(text = "♥", color = Color.White, fontSize = 12.sp) }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Salud", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            var selectedTab by remember { mutableStateOf(0) }
            CustomTabRow(
                tabs = listOf("Signos Vitales", "Medicación", "Informes"),
                selectedIndex = selectedTab,
                onTabSelected = { selectedTab = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (selectedTab) {
                0 -> SignosVitalesContent(
                    heartRate = uiState.heartRate,
                    bloodPressure = uiState.bloodPressure,
                    isLoading = uiState.isLoading,
                    onNuevaMedicionBp = viewModel::onShowBpDialog,
                    onNuevaMedicionHr = viewModel::onShowHrDialog
                )
                1 -> MedicacionContent(
                    medications = uiState.medications,
                    onAdd = viewModel::onShowMedDialog,
                    onToggle = viewModel::onToggleMedication,
                    onDelete = viewModel::onDeleteMedication
                )
                2 -> InformesContent(
                    heartRate = uiState.heartRate,
                    bloodPressure = uiState.bloodPressure,
                    medicationCount = uiState.medications.count { it.isActive }
                )
            }
        }
    }
}

@Composable
fun SignosVitalesContent(
    heartRate: HeartRateData,
    bloodPressure: BloodPressureData,
    isLoading: Boolean,
    onNuevaMedicionBp: () -> Unit,
    onNuevaMedicionHr: () -> Unit
) {
    if (isLoading) {
        Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Color(0xFF4CAF50))
        }
        return
    }
    Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFEEF2F5))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Ritmo Cardíaco", fontWeight = FontWeight.Medium, fontSize = 18.sp, color = Color(0xFF2C3E50))
                Text("Últimas 24 horas", fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(16.dp))

                if (!heartRate.hasData) {
                    Text("Sin registros. Agrega tu primera medición.", color = Color.Gray, fontSize = 14.sp,
                        modifier = Modifier.padding(vertical = 8.dp))
                } else {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column(horizontalAlignment = Alignment.Start) {
                            Text("Promedio", fontSize = 14.sp, color = Color.Gray)
                            Text("${heartRate.average} BPM", fontSize = 18.sp, fontWeight = FontWeight.Medium, color = Color(0xFF2C3E50))
                        }
                        Column(horizontalAlignment = Alignment.Start) {
                            Text("Mínimo", fontSize = 14.sp, color = Color.Gray)
                            Text("${heartRate.min} BPM", fontSize = 18.sp, fontWeight = FontWeight.Medium, color = Color(0xFF2196F3))
                        }
                        Column(horizontalAlignment = Alignment.Start) {
                            Text("Máximo", fontSize = 14.sp, color = Color.Gray)
                            Text("${heartRate.max} BPM", fontSize = 18.sp, fontWeight = FontWeight.Medium, color = Color(0xFFE91E63))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onNuevaMedicionHr, modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C3E50), contentColor = Color.White),
                    shape = RoundedCornerShape(8.dp)
                ) { Text("+ Registrar frecuencia cardíaca") }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFEEF7EE))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Presión Arterial", fontWeight = FontWeight.Medium, fontSize = 18.sp, color = Color(0xFF2C3E50))
                Text("Última medición", fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(24.dp))

                if (!bloodPressure.hasData) {
                    Text("Sin registros. Agrega tu primera medición.", color = Color.Gray, fontSize = 14.sp,
                        modifier = Modifier.padding(vertical = 8.dp))
                } else {
                    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "${bloodPressure.systolic}/${bloodPressure.diastolic}",
                            fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50)
                        )
                        Text(bloodPressure.status, fontSize = 16.sp, color = Color(0xFF4CAF50))
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = onNuevaMedicionBp, modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color(0xFF4CAF50)),
                    shape = RoundedCornerShape(8.dp)
                ) { Text("+ Nueva medición de presión") }
            }
        }
    }
}

@Composable
fun MedicacionContent(
    medications: List<Medication>,
    onAdd: () -> Unit,
    onToggle: (Medication) -> Unit,
    onDelete: (Medication) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Medicación", fontWeight = FontWeight.Medium, fontSize = 18.sp)
            Button(
                onClick = onAdd,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                shape = RoundedCornerShape(8.dp)
            ) { Text("+ Agregar") }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (medications.isEmpty()) {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    Text("No hay medicamentos registrados.", color = Color.Gray, fontSize = 14.sp)
                }
            }
        } else {
            medications.forEach { med ->
                MedicamentItem(medication = med, onToggle = onToggle, onDelete = onDelete)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun MedicamentItem(
    medication: Medication,
    onToggle: (Medication) -> Unit,
    onDelete: (Medication) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = medication.name, fontWeight = FontWeight.Medium, fontSize = 16.sp)
                Text(text = "${medication.dose} · ${medication.frequency}", fontSize = 14.sp, color = Color.Gray)
            }
            Switch(
                checked = medication.isActive,
                onCheckedChange = { onToggle(medication) },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFF4CAF50),
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Color.LightGray
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            TextButton(onClick = { onDelete(medication) }) {
                Text("✕", color = Color.Gray, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun InformesContent(
    heartRate: HeartRateData,
    bloodPressure: BloodPressureData,
    medicationCount: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Resumen de Salud", fontWeight = FontWeight.Medium, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(16.dp))

            ResumenItem(titulo = "Frecuencia Cardíaca (prom.)",
                valor = if (heartRate.hasData) "${heartRate.average} BPM" else "Sin datos")
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            ResumenItem(titulo = "Presión Arterial",
                valor = if (bloodPressure.hasData) "${bloodPressure.systolic}/${bloodPressure.diastolic} · ${bloodPressure.status}" else "Sin datos")
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            ResumenItem(titulo = "Medicamentos activos", valor = "$medicationCount")
        }
    }
}

@Composable
private fun ResumenItem(titulo: String, valor: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = titulo, fontSize = 14.sp, color = Color.Gray)
        Text(text = valor, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun BloodPressureDialog(onDismiss: () -> Unit, onConfirm: (Int, Int) -> Unit, errorMessage: String) {
    var systolic by remember { mutableStateOf("") }
    var diastolic by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva Medición de Presión") },
        text = {
            Column {
                OutlinedTextField(
                    value = systolic, onValueChange = { systolic = it },
                    label = { Text("Sistólica (mmHg)") }, modifier = Modifier.fillMaxWidth(),
                    singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = diastolic, onValueChange = { diastolic = it },
                    label = { Text("Diastólica (mmHg)") }, modifier = Modifier.fillMaxWidth(),
                    singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                if (errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = errorMessage, color = Color.Red, fontSize = 13.sp)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirm(systolic.toIntOrNull() ?: 0, diastolic.toIntOrNull() ?: 0)
            }) { Text("Guardar", color = Color(0xFF4CAF50)) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

@Composable
private fun HeartRateDialog(onDismiss: () -> Unit, onConfirm: (Int) -> Unit, errorMessage: String) {
    var bpm by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Registrar Frecuencia Cardíaca") },
        text = {
            Column {
                OutlinedTextField(
                    value = bpm, onValueChange = { bpm = it },
                    label = { Text("BPM") }, modifier = Modifier.fillMaxWidth(),
                    singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                if (errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = errorMessage, color = Color.Red, fontSize = 13.sp)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(bpm.toIntOrNull() ?: 0) }) { Text("Guardar", color = Color(0xFF4CAF50)) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

@Composable
private fun AddMedicationDialog(onDismiss: () -> Unit, onConfirm: (String, String, String) -> Unit, errorMessage: String) {
    var name by remember { mutableStateOf("") }
    var dose by remember { mutableStateOf("") }
    var frequency by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agregar Medicamento") },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre *") },
                    modifier = Modifier.fillMaxWidth(), singleLine = true)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = dose, onValueChange = { dose = it }, label = { Text("Dosis (ej. 500mg) *") },
                    modifier = Modifier.fillMaxWidth(), singleLine = true)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = frequency, onValueChange = { frequency = it }, label = { Text("Frecuencia (ej. Cada 8h) *") },
                    modifier = Modifier.fillMaxWidth(), singleLine = true)
                if (errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = errorMessage, color = Color.Red, fontSize = 13.sp)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(name, dose, frequency) }) { Text("Guardar", color = Color(0xFF4CAF50)) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}
