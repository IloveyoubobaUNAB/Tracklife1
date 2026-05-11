package com.jpalomino502.vivebien.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jpalomino502.vivebien.core.domain.model.Appointment
import com.jpalomino502.vivebien.core.ui.components.CustomTabRow
import com.jpalomino502.vivebien.feature.appointments.ui.AppointmentsViewModel
import java.text.SimpleDateFormat
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun CitasScreen(viewModel: AppointmentsViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.showAddDialog) {
        AddCitaDialog(
            onDismiss = viewModel::onHideAddDialog,
            onConfirm = { title, doctor, specialty, display, timestamp, location, isVirtual ->
                viewModel.onAddAppointment(title, doctor, specialty, display, timestamp, location, isVirtual)
            },
            errorMessage = uiState.errorMessage
        )
    }

    val allAppointments = uiState.upcomingAppointments + uiState.pastAppointments

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
                    ) { Text(text = "📅", color = Color.White, fontSize = 12.sp) }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Citas", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = viewModel::onShowAddDialog,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    shape = RoundedCornerShape(8.dp)
                ) { Text("+ Nueva Cita") }
            }

            Spacer(modifier = Modifier.height(16.dp))

            CalendarioMes(allAppointments = allAppointments)

            Spacer(modifier = Modifier.height(16.dp))

            var selectedTab by remember { mutableStateOf(0) }
            CustomTabRow(
                tabs = listOf("Próximas", "Pasadas"),
                selectedIndex = selectedTab,
                onTabSelected = { selectedTab = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (selectedTab) {
                0 -> CitasContent(
                    appointments = uiState.upcomingAppointments,
                    isLoading = uiState.isLoading,
                    emptyMessage = "No hay citas próximas.",
                    onDelete = viewModel::onDeleteAppointment
                )
                1 -> CitasContent(
                    appointments = uiState.pastAppointments,
                    isLoading = uiState.isLoading,
                    emptyMessage = "No hay citas pasadas.",
                    onDelete = viewModel::onDeleteAppointment
                )
            }
        }
    }
}

@Composable
fun CitasContent(
    appointments: List<Appointment>,
    isLoading: Boolean,
    emptyMessage: String,
    onDelete: (Appointment) -> Unit
) {
    when {
        isLoading -> Box(
            modifier = Modifier.fillMaxWidth().padding(32.dp),
            contentAlignment = Alignment.Center
        ) { CircularProgressIndicator(color = Color(0xFF4CAF50)) }

        appointments.isEmpty() -> Box(
            modifier = Modifier.fillMaxWidth().padding(32.dp),
            contentAlignment = Alignment.Center
        ) { Text(emptyMessage, color = Color.Gray, fontSize = 14.sp) }

        else -> Column(modifier = Modifier.fillMaxWidth()) {
            appointments.forEachIndexed { index, appointment ->
                if (index > 0) Spacer(modifier = Modifier.height(12.dp))
                CitaCard(appointment = appointment, onDelete = onDelete)
            }
        }
    }
}

@Composable
fun CitaCard(appointment: Appointment, onDelete: (Appointment) -> Unit = {}) {
    val accentColor = when {
        appointment.isPast -> Color(0xFF9E9E9E)
        appointment.isVirtual -> Color(0xFF9C27B0)
        else -> Color(0xFF2196F3)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(48.dp).clip(CircleShape).background(accentColor),
                contentAlignment = Alignment.Center
            ) {
                Text(text = if (appointment.isVirtual) "📱" else "📅", color = Color.White, fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = appointment.title, fontWeight = FontWeight.Medium, fontSize = 16.sp)
                Text(
                    text = "${appointment.doctorName} - ${appointment.specialty}",
                    fontSize = 14.sp, color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "⏱ ${appointment.dateTime}", fontSize = 14.sp, color = Color.Gray)
                if (appointment.location.isNotBlank()) {
                    Text(text = "📍 ${appointment.location}", fontSize = 14.sp, color = Color.Gray)
                }
            }

            TextButton(onClick = { onDelete(appointment) }) {
                Text("✕", color = Color.Gray, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun CalendarioMes(allAppointments: List<Appointment> = emptyList()) {
    var displayedMonth by remember { mutableStateOf(YearMonth.now()) }

    // Calcula qué días del mes mostrado tienen citas
    val appointmentDays: Set<Int> = remember(allAppointments, displayedMonth) {
        allAppointments.mapNotNull { appt ->
            if (appt.dateTimestamp <= 0L) return@mapNotNull null
            val cal = java.util.Calendar.getInstance().apply { timeInMillis = appt.dateTimestamp }
            val y = cal.get(java.util.Calendar.YEAR)
            val m = cal.get(java.util.Calendar.MONTH) + 1   // Calendar es 0-based
            if (y == displayedMonth.year && m == displayedMonth.monthValue)
                cal.get(java.util.Calendar.DAY_OF_MONTH)
            else null
        }.toSet()
    }

    val today = java.time.LocalDate.now()
    val firstDay = displayedMonth.atDay(1)
    // DayOfWeek: MONDAY=1 … SUNDAY=7  →  offset domingo-primero: SUNDAY(7)%7=0, MON=1, … SAT=6
    val startOffset = firstDay.dayOfWeek.value % 7
    val daysInMonth = displayedMonth.lengthOfMonth()

    val calendarDays = buildList {
        repeat(startOffset) { add(-1) }
        for (d in 1..daysInMonth) add(d)
        val rem = (7 - size % 7) % 7
        repeat(rem) { add(-1) }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp)) {

            // ── Encabezado con navegación ──────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { displayedMonth = displayedMonth.minusMonths(1) },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Mes anterior",
                        tint = Color(0xFF4CAF50)
                    )
                }

                Text(
                    text = displayedMonth
                        .format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale("es")))
                        .replaceFirstChar { it.uppercase() },
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )

                IconButton(
                    onClick = { displayedMonth = displayedMonth.plusMonths(1) },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Mes siguiente",
                        tint = Color(0xFF4CAF50)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ── Cabecera de días ───────────────────────────────────────────
            Row(modifier = Modifier.fillMaxWidth()) {
                listOf("Do", "Lu", "Ma", "Mi", "Ju", "Vi", "Sa").forEach { label ->
                    Text(
                        text = label,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // ── Semanas ────────────────────────────────────────────────────
            calendarDays.chunked(7).forEach { week ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    week.forEach { day ->
                        val isToday = day > 0
                            && displayedMonth.year == today.year
                            && displayedMonth.monthValue == today.monthValue
                            && day == today.dayOfMonth
                        DiaCalendario(
                            modifier = Modifier.weight(1f),
                            dia = if (day > 0) day else 0,
                            estaSeleccionado = isToday,
                            tieneEvento = day > 0 && appointmentDays.contains(day)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
            }
        }
    }
}

@Composable
fun DiaCalendario(
    modifier: Modifier = Modifier,
    dia: Int,
    estaSeleccionado: Boolean,
    tieneEvento: Boolean = false
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(2.dp)
            .clip(CircleShape)
            .background(
                when {
                    estaSeleccionado -> Color(0xFF4CAF50)
                    tieneEvento -> Color(0xFFE8F5E9)
                    else -> Color.Transparent
                }
            )
            .border(
                width = if (tieneEvento && !estaSeleccionado) 1.dp else 0.dp,
                color = if (tieneEvento && !estaSeleccionado) Color(0xFF4CAF50) else Color.Transparent,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        if (dia > 0) {
            Text(
                text = dia.toString(),
                fontSize = 13.sp,
                fontWeight = if (estaSeleccionado || tieneEvento) FontWeight.SemiBold else FontWeight.Normal,
                color = when {
                    estaSeleccionado -> Color.White
                    tieneEvento -> Color(0xFF2E7D32)
                    else -> Color.Black
                }
            )
        }
    }
}

@Composable
private fun AddCitaDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String, Long, String, Boolean) -> Unit,
    errorMessage: String
) {
    var title by remember { mutableStateOf("") }
    var doctor by remember { mutableStateOf("") }
    var specialty by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var isVirtual by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva Cita") },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                OutlinedTextField(
                    value = title, onValueChange = { title = it },
                    label = { Text("Título *") }, modifier = Modifier.fillMaxWidth(), singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = doctor, onValueChange = { doctor = it },
                    label = { Text("Médico *") }, modifier = Modifier.fillMaxWidth(), singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = specialty, onValueChange = { specialty = it },
                    label = { Text("Especialidad") }, modifier = Modifier.fillMaxWidth(), singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = date, onValueChange = { date = it },
                    label = { Text("Fecha (dd/MM/yyyy) *") }, modifier = Modifier.fillMaxWidth(), singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = time, onValueChange = { time = it },
                    label = { Text("Hora (HH:mm) *") }, modifier = Modifier.fillMaxWidth(), singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = location, onValueChange = { location = it },
                    label = { Text("Lugar (opcional)") }, modifier = Modifier.fillMaxWidth(), singleLine = true
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isVirtual, onCheckedChange = { isVirtual = it },
                        colors = CheckboxDefaults.colors(checkedColor = Color(0xFF4CAF50))
                    )
                    Text("Cita virtual", fontSize = 14.sp)
                }
                if (errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = errorMessage, color = Color.Red, fontSize = 13.sp)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val timestamp = parseTimestamp("$date $time")
                val displayDate = "$date, $time"
                onConfirm(title, doctor, specialty, displayDate, timestamp, location, isVirtual)
            }) { Text("Guardar", color = Color(0xFF4CAF50)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

private fun parseTimestamp(dateTimeStr: String): Long {
    return try {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("es"))
        sdf.parse(dateTimeStr.trim())?.time ?: 0L
    } catch (e: Exception) { 0L }
}
