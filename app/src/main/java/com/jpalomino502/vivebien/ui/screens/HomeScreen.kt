package com.jpalomino502.vivebien.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jpalomino502.vivebien.core.ui.components.CustomTabRow
import com.jpalomino502.vivebien.feature.home.ui.HomeViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            HeaderSection(greeting = uiState.greeting, date = uiState.currentDate)
            Spacer(modifier = Modifier.height(16.dp))
            PlanPersonalizadoCard(
                progress = uiState.planProgress,
                progressPercent = uiState.planProgressPercent
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ProximaCitaCard(nextAppointment = uiState.nextAppointment, modifier = Modifier.weight(1f))
                PasosHoyCard(stepsToday = uiState.stepsToday, modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(16.dp))
            SaludActividadesTabs()
            Spacer(modifier = Modifier.height(16.dp))
            RitmoCardiacoCard()
        }
    }
}

@Composable
fun HeaderSection(greeting: String, date: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF4CAF50))
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "♥", color = Color.White, fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "ViveBien",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4CAF50)
            )
        }
        Text(text = date, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
    }

    Spacer(modifier = Modifier.height(16.dp))

    Text(text = greeting, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Medium)
}

@Composable
fun PlanPersonalizadoCard(progress: Float, progressPercent: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8EFFF))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Plan Personalizado",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF673AB7)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Progreso general",
                fontSize = 14.sp,
                color = Color(0xFF673AB7).copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier.weight(1f).height(8.dp).clip(RoundedCornerShape(4.dp)),
                    color = Color(0xFF4CAF50),
                    trackColor = Color(0xFFE1D4F2)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "$progressPercent%",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF673AB7)
                )
            }
        }
    }
}

@Composable
fun ProximaCitaCard(nextAppointment: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2196F3))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f))
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "📅", fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text("Próxima Cita", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.White)
            Spacer(modifier = Modifier.height(4.dp))
            Text(nextAppointment, fontSize = 14.sp, color = Color.White.copy(alpha = 0.9f))
        }
    }
}

@Composable
fun PasosHoyCard(stepsToday: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f))
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "📊", fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text("Pasos Hoy", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.White)
            Spacer(modifier = Modifier.height(4.dp))
            Text(stepsToday, fontSize = 14.sp, color = Color.White.copy(alpha = 0.9f))
        }
    }
}

@Composable
fun SaludActividadesTabs() {
    var selectedTab by remember { mutableStateOf(0) }
    CustomTabRow(
        tabs = listOf("Salud", "Actividades", "Dispositivos"),
        selectedIndex = selectedTab,
        onTabSelected = { selectedTab = it }
    )
}

@Composable
fun RitmoCardiacoCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Ritmo Cardíaco", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Últimas 24 horas", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Gráfico de ritmo cardíaco", color = Color.Gray)
            }
        }
    }
}
