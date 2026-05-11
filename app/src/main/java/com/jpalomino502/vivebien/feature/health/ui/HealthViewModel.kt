package com.jpalomino502.vivebien.feature.health.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jpalomino502.vivebien.core.domain.model.Medication
import com.jpalomino502.vivebien.feature.health.domain.model.BloodPressureData
import com.jpalomino502.vivebien.feature.health.domain.model.HeartRateData
import com.jpalomino502.vivebien.feature.health.domain.usecase.AddMedicationUseCase
import com.jpalomino502.vivebien.feature.health.domain.usecase.AddVitalSignUseCase
import com.jpalomino502.vivebien.feature.health.domain.usecase.DeleteMedicationUseCase
import com.jpalomino502.vivebien.feature.health.domain.usecase.GetBloodPressureUseCase
import com.jpalomino502.vivebien.feature.health.domain.usecase.GetHeartRateUseCase
import com.jpalomino502.vivebien.feature.health.domain.usecase.GetMedicationsUseCase
import com.jpalomino502.vivebien.feature.health.domain.usecase.ToggleMedicationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HealthUiState(
    val heartRate: HeartRateData = HeartRateData(),
    val bloodPressure: BloodPressureData = BloodPressureData(),
    val medications: List<Medication> = emptyList(),
    val isLoading: Boolean = true,
    val showBpDialog: Boolean = false,
    val showHrDialog: Boolean = false,
    val showMedDialog: Boolean = false,
    val errorMessage: String = ""
)

@HiltViewModel
class HealthViewModel @Inject constructor(
    private val getHeartRate: GetHeartRateUseCase,
    private val getBloodPressure: GetBloodPressureUseCase,
    private val getMedications: GetMedicationsUseCase,
    private val addVitalSign: AddVitalSignUseCase,
    private val addMedication: AddMedicationUseCase,
    private val toggleMedication: ToggleMedicationUseCase,
    private val deleteMedication: DeleteMedicationUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HealthUiState())
    val uiState: StateFlow<HealthUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(getHeartRate(), getBloodPressure(), getMedications()) { hr, bp, meds ->
                Triple(hr, bp, meds)
            }.collect { (hr, bp, meds) ->
                _uiState.value = _uiState.value.copy(
                    heartRate = hr, bloodPressure = bp, medications = meds, isLoading = false
                )
            }
        }
    }

    fun onShowBpDialog() { _uiState.value = _uiState.value.copy(showBpDialog = true, errorMessage = "") }
    fun onHideBpDialog() { _uiState.value = _uiState.value.copy(showBpDialog = false, errorMessage = "") }

    fun onShowHrDialog() { _uiState.value = _uiState.value.copy(showHrDialog = true, errorMessage = "") }
    fun onHideHrDialog() { _uiState.value = _uiState.value.copy(showHrDialog = false, errorMessage = "") }

    fun onShowMedDialog() { _uiState.value = _uiState.value.copy(showMedDialog = true, errorMessage = "") }
    fun onHideMedDialog() { _uiState.value = _uiState.value.copy(showMedDialog = false, errorMessage = "") }

    fun onAddBloodPressure(systolic: Int, diastolic: Int) {
        viewModelScope.launch {
            val result = addVitalSign.addBloodPressure(systolic, diastolic)
            result.fold(
                onSuccess = { _uiState.value = _uiState.value.copy(showBpDialog = false, errorMessage = "") },
                onFailure = { _uiState.value = _uiState.value.copy(errorMessage = it.message ?: "") }
            )
        }
    }

    fun onAddHeartRate(bpm: Int) {
        viewModelScope.launch {
            val result = addVitalSign.addHeartRate(bpm)
            result.fold(
                onSuccess = { _uiState.value = _uiState.value.copy(showHrDialog = false, errorMessage = "") },
                onFailure = { _uiState.value = _uiState.value.copy(errorMessage = it.message ?: "") }
            )
        }
    }

    fun onAddMedication(name: String, dose: String, frequency: String) {
        viewModelScope.launch {
            val result = addMedication(name, dose, frequency)
            result.fold(
                onSuccess = { _uiState.value = _uiState.value.copy(showMedDialog = false, errorMessage = "") },
                onFailure = { _uiState.value = _uiState.value.copy(errorMessage = it.message ?: "") }
            )
        }
    }

    fun onToggleMedication(medication: Medication) {
        viewModelScope.launch { toggleMedication(medication) }
    }

    fun onDeleteMedication(medication: Medication) {
        viewModelScope.launch { deleteMedication(medication) }
    }
}
