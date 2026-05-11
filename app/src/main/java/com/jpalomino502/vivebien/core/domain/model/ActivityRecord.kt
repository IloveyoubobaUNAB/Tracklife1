package com.jpalomino502.vivebien.core.domain.model

data class ActivityRecord(
    val id: Long = 0,
    val title: String,
    val durationMinutes: Int,
    val distanceKm: Float? = null,
    val scheduledTime: String? = null,
    val completed: Boolean = false,
    val date: Long = System.currentTimeMillis()
)

data class DailyStats(
    val steps: Int = 0,
    val calories: Int = 0,
    val distanceKm: Float = 0f,
    val goalPercent: Int = 0
)
