package com.jpalomino502.vivebien.feature.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jpalomino502.vivebien.feature.home.domain.usecase.GetDashboardUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val greeting: String = "",
    val currentDate: String = "",
    val planProgress: Float = 0f,
    val planProgressPercent: Int = 0,
    val nextAppointment: String = "Sin citas próximas",
    val stepsToday: String = "0 pasos",
    val isLoading: Boolean = true
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getDashboard: GetDashboardUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getDashboard().collect { data ->
                _uiState.value = HomeUiState(
                    greeting = "Hola, ${data.displayName}",
                    currentDate = data.currentDate,
                    planProgress = data.planProgress,
                    planProgressPercent = data.planProgressPercent,
                    nextAppointment = data.nextAppointment,
                    stepsToday = "%,d pasos".format(data.dailyStats.steps),
                    isLoading = false
                )
            }
        }
    }
}
