package com.jpalomino502.vivebien.feature.health.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class HeartRateData(
    val average: Int = 72,
    val min: Int = 58,
    val max: Int = 110
)

data class BloodPressureData(
    val systolic: Int = 120,
    val diastolic: Int = 80,
    val status: String = "Normal"
)

data class HealthUiState(
    val heartRate: HeartRateData = HeartRateData(),
    val bloodPressure: BloodPressureData = BloodPressureData(),
    val isLoading: Boolean = false
)

@HiltViewModel
class HealthViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(HealthUiState())
    val uiState: StateFlow<HealthUiState> = _uiState.asStateFlow()

    init {
        loadHealthData()
    }

    private fun loadHealthData() {
        _uiState.value = HealthUiState(
            heartRate = HeartRateData(average = 72, min = 58, max = 110),
            bloodPressure = BloodPressureData(systolic = 120, diastolic = 80, status = "Normal")
        )
    }
}
