package com.jpalomino502.vivebien.feature.health.domain.model

data class HeartRateData(
    val average: Int = 0,
    val min: Int = 0,
    val max: Int = 0,
    val hasData: Boolean = false
)

data class BloodPressureData(
    val systolic: Int = 120,
    val diastolic: Int = 80,
    val status: String = "Sin datos",
    val hasData: Boolean = false
)
