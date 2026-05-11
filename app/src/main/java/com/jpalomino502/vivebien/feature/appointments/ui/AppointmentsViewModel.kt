package com.jpalomino502.vivebien.feature.appointments.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jpalomino502.vivebien.core.domain.model.Appointment
import com.jpalomino502.vivebien.feature.appointments.domain.usecase.AddAppointmentUseCase
import com.jpalomino502.vivebien.feature.appointments.domain.usecase.DeleteAppointmentUseCase
import com.jpalomino502.vivebien.feature.appointments.domain.usecase.GetAppointmentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AppointmentsUiState(
    val upcomingAppointments: List<Appointment> = emptyList(),
    val pastAppointments: List<Appointment> = emptyList(),
    val isLoading: Boolean = true,
    val showAddDialog: Boolean = false,
    val errorMessage: String = ""
)

@HiltViewModel
class AppointmentsViewModel @Inject constructor(
    private val getAppointments: GetAppointmentsUseCase,
    private val addAppointmentUseCase: AddAppointmentUseCase,
    private val deleteAppointmentUseCase: DeleteAppointmentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppointmentsUiState())
    val uiState: StateFlow<AppointmentsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                getAppointments.upcoming(),
                getAppointments.past()
            ) { upcoming, past -> upcoming to past }
            .collect { (upcoming, past) ->
                _uiState.value = _uiState.value.copy(
                    upcomingAppointments = upcoming,
                    pastAppointments = past,
                    isLoading = false
                )
            }
        }
    }

    fun onShowAddDialog() { _uiState.value = _uiState.value.copy(showAddDialog = true, errorMessage = "") }
    fun onHideAddDialog() { _uiState.value = _uiState.value.copy(showAddDialog = false, errorMessage = "") }

    fun onAddAppointment(
        title: String, doctorName: String, specialty: String,
        dateTimeDisplay: String, dateTimestamp: Long,
        location: String, isVirtual: Boolean
    ) {
        viewModelScope.launch {
            val result = addAppointmentUseCase(title, doctorName, specialty, dateTimeDisplay, dateTimestamp, location, isVirtual)
            result.fold(
                onSuccess = { _uiState.value = _uiState.value.copy(showAddDialog = false, errorMessage = "") },
                onFailure = { _uiState.value = _uiState.value.copy(errorMessage = it.message ?: "") }
            )
        }
    }

    fun onDeleteAppointment(appointment: Appointment) {
        viewModelScope.launch { deleteAppointmentUseCase(appointment) }
    }
}
