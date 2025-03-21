package com.jpalomino502.vivebien.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CitasScreen() {
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
            // Header con título y botón de nueva cita
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
                        Text(
                            text = "📅",
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Citas",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = { /* Acción para nueva cita */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("+ Nueva Cita")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Título del mes
            Text(
                text = "Abril 2025",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Calendario
            CalendarioMes()

            Spacer(modifier = Modifier.height(16.dp))

            // Tabs de Próximas y Pasadas
            var selectedTab by remember { mutableStateOf(0) }
            val tabs = listOf("Próximas", "Pasadas")

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color(0xFFEEF2F5),
                contentColor = Color(0xFF4CAF50),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        height = 0.dp // Sin indicador visible
                    )
                },
                divider = { Divider(thickness = 0.dp) } // Sin divisor
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        modifier = Modifier
                            .padding(4.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (selectedTab == index) Color.White else Color.Transparent
                            ),
                        text = {
                            Text(
                                title,
                                fontWeight = if (selectedTab == index) FontWeight.Medium else FontWeight.Normal,
                                color = if (selectedTab == index) Color(0xFF4CAF50) else Color.Gray
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Contenido según el tab seleccionado
            when (selectedTab) {
                0 -> ProximasCitasContent()
                1 -> PasadasCitasContent()
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
            // Navegación del mes
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
                    modifier = Modifier.clickable { /* Mes anterior */ }
                )

                Text(
                    text = "March 2025",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = " >",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray,
                    modifier = Modifier.clickable { /* Mes siguiente */ }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Días de la semana
            Row(modifier = Modifier.fillMaxWidth()) {
                val diasSemana = listOf("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa")
                diasSemana.forEach { dia ->
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

            // Semana 1
            Row(modifier = Modifier.fillMaxWidth()) {
                for (i in 23..29) {
                    val esDelMesActual = i >= 25
                    DiaCalendario(
                        dia = i,
                        estaSeleccionado = false,
                        esDelMesActual = esDelMesActual
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Semana 2
            Row(modifier = Modifier.fillMaxWidth()) {
                for (i in 1..7) {
                    DiaCalendario(
                        dia = i,
                        estaSeleccionado = false,
                        esDelMesActual = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Semana 3
            Row(modifier = Modifier.fillMaxWidth()) {
                for (i in 8..14) {
                    DiaCalendario(
                        dia = i,
                        estaSeleccionado = i == 11,
                        esDelMesActual = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Semana 4
            Row(modifier = Modifier.fillMaxWidth()) {
                for (i in 15..21) {
                    DiaCalendario(
                        dia = i,
                        estaSeleccionado = i == 18,
                        esDelMesActual = true,
                        tieneEvento = i == 18
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Semana 5
            Row(modifier = Modifier.fillMaxWidth()) {
                for (i in 22..28) {
                    DiaCalendario(
                        dia = i,
                        estaSeleccionado = false,
                        esDelMesActual = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Semana 6
            Row(modifier = Modifier.fillMaxWidth()) {
                for (i in 29..35) {
                    val esDelMesActual = i <= 31
                    DiaCalendario(
                        dia = if (i > 31) i - 31 else i,
                        estaSeleccionado = false,
                        esDelMesActual = esDelMesActual
                    )
                }
            }
        }
    }
}

@Composable
fun DiaCalendario(
    dia: Int,
    estaSeleccionado: Boolean,
    esDelMesActual: Boolean,
    tieneEvento: Boolean = false
) {
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
    )
    {
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

@Composable
fun ProximasCitasContent() {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Cita 1: Control Médico
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icono
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF2196F3)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "📅",
                        color = Color.White,
                        fontSize = 20.sp
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Información
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Control Médico",
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Dr. Martínez - Medicina General",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "⏱ 15 de Abril, 10:00 AM",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "📍 Centro Médico ViveBien",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }

                // Botón de reprogramar
                Button(
                    onClick = { /* Acción para reprogramar */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFF2196F3)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Reprogramar")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Cita 2: Consulta Virtual
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icono
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF9C27B0)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "📱",
                        color = Color.White,
                        fontSize = 20.sp
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Información
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Consulta Virtual",
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Dra. López - Nutrición",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "⏱ 20 de Abril, 3:00 PM",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }

                // Botón de reprogramar
                Button(
                    onClick = { /* Acción para reprogramar */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFF9C27B0)
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
fun PasadasCitasContent() {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Cita pasada
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icono
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF9E9E9E)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "📱",
                        color = Color.White,
                        fontSize = 20.sp
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Información
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Consulta Virtual",
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Dr. Ramírez - Cardiología",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "⏱ 10 de Marzo, 3:00 PM",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}
