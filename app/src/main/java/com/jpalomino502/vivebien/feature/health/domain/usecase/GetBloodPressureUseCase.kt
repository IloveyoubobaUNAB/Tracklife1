package com.jpalomino502.vivebien.feature.health.domain.usecase

import com.jpalomino502.vivebien.feature.health.data.HealthRepository
import com.jpalomino502.vivebien.feature.health.domain.model.BloodPressureData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetBloodPressureUseCase @Inject constructor(private val repository: HealthRepository) {
    operator fun invoke(): Flow<BloodPressureData> =
        repository.getLatestBloodPressure().map { (systolic, diastolic) ->
            if (systolic == null || diastolic == null) return@map BloodPressureData()
            val s = systolic.value.toInt()
            val d = diastolic.value.toInt()
            val status = when {
                s < 120 && d < 80 -> "Normal"
                s < 130 && d < 80 -> "Elevada"
                s < 140 || d < 90 -> "Alta fase 1"
                else -> "Alta fase 2"
            }
            BloodPressureData(systolic = s, diastolic = d, status = status, hasData = true)
        }
}
