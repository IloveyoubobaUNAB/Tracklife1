package com.jpalomino502.vivebien.ui.screens

import androidx.compose.foundation.background
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
fun PerfilScreen() {
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
            // Header con título
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
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
                            text = "👤",
                            color = Color.White,
                            fontSize = 12.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Perfil",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Foto de perfil (simulada con un círculo)
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
                    .align(Alignment.CenterHorizontally),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "MG",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Nombre y ID
            Text(
                text = "María García",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "ID: VB-78542",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Tabs de Información, Dispositivos, Ajustes
            var selectedTab by remember { mutableStateOf(0) }
            val tabs = listOf("Información", "Dispositivos", "Ajustes")

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
                        height = 0.dp
                    )
                },
                divider = { Divider(thickness = 0.dp) }
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

            when (selectedTab) {
                0 -> InformacionTabContent()
                1 -> DispositivosTabContent()
                2 -> AjustesTabContent()
            }
        }
    }
}

@Composable
fun InformacionTabContent() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Información Personal",
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                "Datos básicos de tu perfil",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            CampoInformacion(
                etiqueta = "Nombre completo",
                valor = "María García López"
            )

            Spacer(modifier = Modifier.height(16.dp))

            CampoInformacion(
                etiqueta = "Correo electrónico",
                valor = "maria.garcia@ejemplo.com"
            )

            Spacer(modifier = Modifier.height(16.dp))

            CampoInformacion(
                etiqueta = "Teléfono",
                valor = "+34 612 345 678"
            )

            Spacer(modifier = Modifier.height(16.dp))

            CampoInformacion(
                etiqueta = "Fecha de nacimiento",
                valor = "15/05/1957"
            )
        }
    }
}

@Composable
fun CampoInformacion(
    etiqueta: String,
    valor: String
) {
    Column {
        Text(
            text = etiqueta,
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(
            value = valor,
            onValueChange = { /* No editable */ },
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.LightGray,
                unfocusedContainerColor = Color(0xFFF5F5F5)
            )
        )
    }
}

@Composable
fun DispositivosTabContent() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Dispositivos",
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Dispositivo 1
            DispositivoItem(
                nombre = "Smartwatch XYZ",
                estado = "Conectado",
                ultimaSincronizacion = "Hoy, 10:30 AM"
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Dispositivo 2
            DispositivoItem(
                nombre = "Pulsera de actividad ABC",
                estado = "Desconectado",
                ultimaSincronizacion = "Ayer, 8:15 PM"
            )
        }
    }
}
@Composable
fun DispositivoItem(
    nombre: String,
    estado: String,
    ultimaSincronizacion: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF5F5F5)
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2196F3)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "⌚",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Información
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = nombre,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )

                Text(
                    text = estado,
                    fontSize = 14.sp,
                    color = if (estado == "Conectado") Color(0xFF4CAF50) else Color.Gray
                )

                Text(
                    text = "Última sincronización: $ultimaSincronizacion",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun AjustesTabContent() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Ajustes",
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Ajuste de notificaciones
            AjusteItem(
                titulo = "Notificaciones",
                descripcion = "Recibe alertas sobre tus citas y actividades",
                activado = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Ajuste de idioma
            AjusteItem(
                titulo = "Idioma",
                descripcion = "Español",
                esSwitch = false
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Ajuste de privacidad
            AjusteItem(
                titulo = "Privacidad",
                descripcion = "Gestiona tus datos personales",
                esSwitch = false
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Ajuste de tema oscuro
            AjusteItem(
                titulo = "Tema Oscuro",
                descripcion = "Cambia la apariencia de la aplicación",
                activado = false
            )
        }
    }
}

@Composable
fun AjusteItem(
    titulo: String,
    descripcion: String,
    activado: Boolean = false,
    esSwitch: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = titulo,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )

            Text(
                text = descripcion,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

        if (esSwitch) {
            Switch(
                checked = activado,
                onCheckedChange = { /* Acción al cambiar */ },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = Color(0xFF4CAF50),
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = Color.LightGray
                )
            )
        } else {
            Text(
                text = ">",
                fontSize = 18.sp,
                color = Color.Gray
            )
        }
    }
}
