package com.jpalomino502.vivebien.feature.appointments.ui

import androidx.lifecycle.ViewModel
import com.jpalomino502.vivebien.core.domain.model.Appointment
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class AppointmentsUiState(
    val upcomingAppointments: List<Appointment> = emptyList(),
    val pastAppointments: List<Appointment> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class AppointmentsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(AppointmentsUiState())
    val uiState: StateFlow<AppointmentsUiState> = _uiState.asStateFlow()

    init {
        loadAppointments()
    }

    private fun loadAppointments() {
        _uiState.value = AppointmentsUiState(
            upcomingAppointments = listOf(
                Appointment(
                    id = 1,
                    title = "Control Médico",
                    doctorName = "Dr. Martínez",
                    specialty = "Medicina General",
                    dateTime = "15 de Abril, 10:00 AM",
                    location = "Centro Médico ViveBien"
                ),
                Appointment(
                    id = 2,
                    title = "Consulta Virtual",
                    doctorName = "Dra. López",
                    specialty = "Nutrición",
                    dateTime = "20 de Abril, 3:00 PM",
                    isVirtual = true
                )
            ),
            pastAppointments = listOf(
                Appointment(
                    id = 3,
                    title = "Consulta Virtual",
                    doctorName = "Dr. Ramírez",
                    specialty = "Cardiología",
                    dateTime = "10 de Marzo, 3:00 PM",
                    isPast = true,
                    isVirtual = true
                )
            )
        )
    }
}
