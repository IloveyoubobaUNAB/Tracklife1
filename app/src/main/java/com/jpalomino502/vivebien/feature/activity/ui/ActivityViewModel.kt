package com.jpalomino502.vivebien.feature.activity.ui

import androidx.lifecycle.ViewModel
import com.jpalomino502.vivebien.core.domain.model.ActivityRecord
import com.jpalomino502.vivebien.core.domain.model.DailyStats
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class ActivityUiState(
    val dailyStats: DailyStats = DailyStats(),
    val todayActivities: List<ActivityRecord> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class ActivityViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ActivityUiState())
    val uiState: StateFlow<ActivityUiState> = _uiState.asStateFlow()

    init {
        loadActivityData()
    }

    private fun loadActivityData() {
        _uiState.value = ActivityUiState(
            dailyStats = DailyStats(
                steps = 8542,
                calories = 320,
                distanceKm = 4.2f,
                goalPercent = 65
            ),
            todayActivities = listOf(
                ActivityRecord(
                    id = 1,
                    title = "Caminata",
                    durationMinutes = 30,
                    distanceKm = 2.5f,
                    completed = true
                ),
                ActivityRecord(
                    id = 2,
                    title = "Ejercicios de estiramiento",
                    durationMinutes = 15,
                    completed = true
                ),
                ActivityRecord(
                    id = 3,
                    title = "Caminata vespertina",
                    durationMinutes = 30,
                    scheduledTime = "5:00 PM",
                    completed = false
                )
            )
        )
    }
}
