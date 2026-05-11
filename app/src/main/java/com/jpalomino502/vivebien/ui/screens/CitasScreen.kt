package com.jpalomino502.vivebien.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.jpalomino502.vivebien.core.domain.model.Appointment
import com.jpalomino502.vivebien.core.ui.components.CustomTabRow
import com.jpalomino502.vivebien.feature.appointments.ui.AppointmentsViewModel

@Composable
fun CitasScreen(viewModel: AppointmentsViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFF4CAF50))
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "📅", color = Color.White, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Citas",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("+ Nueva Cita")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = java.time.YearMonth.now().format(
                    java.time.format.DateTimeFormatter.ofPattern("MMMM yyyy", java.util.Locale("es"))
                ).replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            CalendarioMes()

            Spacer(modifier = Modifier.height(16.dp))

            var selectedTab by remember { mutableStateOf(0) }
            CustomTabRow(
                tabs = listOf("Próximas", "Pasadas"),
                selectedIndex = selectedTab,
                onTabSelected = { selectedTab = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (selectedTab) {
                0 -> CitasContent(appointments = uiState.upcomingAppointments)
                1 -> CitasContent(appointments = uiState.pastAppointments)
            }
        }
    }
}

@Composable
fun CitasContent(appointments: List<Appointment>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        appointments.forEachIndexed { index, appointment ->
            if (index > 0) Spacer(modifier = Modifier.height(16.dp))
            CitaCard(appointment = appointment)
        }
    }
}

@Composable
fun CitaCard(appointment: Appointment) {
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
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(accentColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = if (appointment.isVirtual) "📱" else "📅",
                    color = Color.White,
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = appointment.title, fontWeight = FontWeight.Medium, fontSize = 16.sp)
                Text(
                    text = "${appointment.doctorName} - ${appointment.specialty}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "⏱ ${appointment.dateTime}", fontSize = 14.sp, color = Color.Gray)
                if (appointment.location.isNotBlank()) {
                    Text(text = "📍 ${appointment.location}", fontSize = 14.sp, color = Color.Gray)
                }
            }

            if (!appointment.isPast) {
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = accentColor
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Reprogramar")
                }
            }
        }
    }
}

@Composable
fun CalendarioMes() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "< ",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    modifier = Modifier.clickable { }
                )
                Text(
                    text = java.time.YearMonth.now().format(
                        java.time.format.DateTimeFormatter.ofPattern("MMMM yyyy", java.util.Locale("es"))
                    ).replaceFirstChar { it.uppercase() },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = " >",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    modifier = Modifier.clickable { }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                listOf("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa").forEach { dia ->
                    Text(
                        text = dia,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            val today = java.time.LocalDate.now()
            val firstDayOfMonth = today.withDayOfMonth(1)
            val startOffset = firstDayOfMonth.dayOfWeek.value % 7
            val daysInMonth = today.lengthOfMonth()

            val calendarDays = buildList {
                repeat(startOffset) { add(-1) }
                for (d in 1..daysInMonth) add(d)
                val remaining = (7 - size % 7) % 7
                repeat(remaining) { add(-1) }
            }

            calendarDays.chunked(7).forEach { week ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    week.forEach { day ->
                        DiaCalendario(
                            dia = if (day > 0) day else 0,
                            estaSeleccionado = day == today.dayOfMonth,
                            esDelMesActual = day > 0
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun DiaCalendario(dia: Int, estaSeleccionado: Boolean, esDelMesActual: Boolean, tieneEvento: Boolean = false) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
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
                fontSize = 14.sp,
                fontWeight = if (estaSeleccionado || tieneEvento) FontWeight.Medium else FontWeight.Normal,
                color = when {
                    estaSeleccionado -> Color.White
                    !esDelMesActual -> Color.LightGray
                    tieneEvento -> Color(0xFF4CAF50)
                    else -> Color.Black
                }
            )
        }
    }
}
