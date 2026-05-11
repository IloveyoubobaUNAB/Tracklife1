package com.jpalomino502.vivebien.feature.health.domain.usecase

import com.jpalomino502.vivebien.core.domain.model.VitalSign
import com.jpalomino502.vivebien.core.domain.model.VitalSignType
import com.jpalomino502.vivebien.feature.health.data.HealthRepository
import javax.inject.Inject

class AddVitalSignUseCase @Inject constructor(private val repository: HealthRepository) {

    suspend fun addHeartRate(bpm: Int): Result<Unit> {
        if (bpm < 30 || bpm > 250) return Result.failure(IllegalArgumentException("Valor de frecuencia cardíaca inválido (30-250)"))
        repository.addVitalSign(VitalSign(type = VitalSignType.HEART_RATE, value = bpm.toFloat(), unit = "BPM"))
        return Result.success(Unit)
    }

    suspend fun addBloodPressure(systolic: Int, diastolic: Int): Result<Unit> {
        if (systolic < 60 || systolic > 300) return Result.failure(IllegalArgumentException("Valor sistólico inválido (60-300)"))
        if (diastolic < 40 || diastolic > 200) return Result.failure(IllegalArgumentException("Valor diastólico inválido (40-200)"))
        if (diastolic >= systolic) return Result.failure(IllegalArgumentException("La sistólica debe ser mayor que la diastólica"))
        repository.addVitalSign(VitalSign(type = VitalSignType.BLOOD_PRESSURE_SYSTOLIC, value = systolic.toFloat(), unit = "mmHg"))
        repository.addVitalSign(VitalSign(type = VitalSignType.BLOOD_PRESSURE_DIASTOLIC, value = diastolic.toFloat(), unit = "mmHg"))
        return Result.success(Unit)
    }
}
