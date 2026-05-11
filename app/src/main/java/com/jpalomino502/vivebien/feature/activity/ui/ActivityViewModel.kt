package com.jpalomino502.vivebien.feature.activity.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jpalomino502.vivebien.core.domain.model.ActivityRecord
import com.jpalomino502.vivebien.core.domain.model.DailyStats
import com.jpalomino502.vivebien.feature.activity.domain.usecase.AddActivityUseCase
import com.jpalomino502.vivebien.feature.activity.domain.usecase.GetDailyStatsUseCase
import com.jpalomino502.vivebien.feature.activity.domain.usecase.GetTodayActivitiesUseCase
import com.jpalomino502.vivebien.feature.activity.domain.usecase.ToggleActivityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ActivityUiState(
    val dailyStats: DailyStats = DailyStats(),
    val todayActivities: List<ActivityRecord> = emptyList(),
    val isLoading: Boolean = true,
    val showAddDialog: Boolean = false,
    val errorMessage: String = ""
)

@HiltViewModel
class ActivityViewModel @Inject constructor(
    private val getTodayActivities: GetTodayActivitiesUseCase,
    private val getDailyStats: GetDailyStatsUseCase,
    private val addActivity: AddActivityUseCase,
    private val toggleActivity: ToggleActivityUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ActivityUiState())
    val uiState: StateFlow<ActivityUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(getTodayActivities(), getDailyStats()) { activities, stats ->
                activities to stats
            }.collect { (activities, stats) ->
                _uiState.value = _uiState.value.copy(
                    todayActivities = activities,
                    dailyStats = stats,
                    isLoading = false
                )
            }
        }
    }

    fun onToggleActivity(activity: ActivityRecord) {
        viewModelScope.launch { toggleActivity(activity) }
    }

    fun onShowAddDialog() { _uiState.value = _uiState.value.copy(showAddDialog = true, errorMessage = "") }
    fun onHideAddDialog() { _uiState.value = _uiState.value.copy(showAddDialog = false, errorMessage = "") }

    fun onAddActivity(title: String, durationMinutes: Int, distanceKm: Float?, scheduledTime: String?) {
        viewModelScope.launch {
            val result = addActivity(title, durationMinutes, distanceKm, scheduledTime)
            result.fold(
                onSuccess = { _uiState.value = _uiState.value.copy(showAddDialog = false, errorMessage = "") },
                onFailure = { _uiState.value = _uiState.value.copy(errorMessage = it.message ?: "Error al guardar") }
            )
        }
    }
}
