package com.jpalomino502.vivebien.core.domain.model

data class VitalSign(
    val id: Long = 0,
    val type: VitalSignType,
    val value: Float,
    val unit: String,
    val timestamp: Long = System.currentTimeMillis()
)

enum class VitalSignType {
    HEART_RATE,
    BLOOD_PRESSURE_SYSTOLIC,
    BLOOD_PRESSURE_DIASTOLIC,
    OXYGEN_SATURATION
}
