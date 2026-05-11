package com.jpalomino502.vivebien.feature.home.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

data class HomeUiState(
    val greeting: String = "",
    val currentDate: String = "",
    val planProgress: Float = 0f,
    val planProgressPercent: Int = 0,
    val nextAppointment: String = "",
    val stepsToday: String = ""
)

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadDashboardData()
    }

    private fun loadDashboardData() {
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM", Locale("es", "CO"))
        val dateStr = today.format(formatter).replaceFirstChar { it.uppercase() }

        _uiState.value = HomeUiState(
            greeting = "Hola, María",
            currentDate = dateStr,
            planProgress = 0.68f,
            planProgressPercent = 68,
            nextAppointment = "15 Abr, 10:00 AM",
            stepsToday = "8,542 pasos"
        )
    }
}
