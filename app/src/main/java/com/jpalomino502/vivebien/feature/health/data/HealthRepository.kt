package com.jpalomino502.vivebien.feature.health.data

import com.jpalomino502.vivebien.core.domain.model.Medication
import com.jpalomino502.vivebien.core.domain.model.VitalSign
import kotlinx.coroutines.flow.Flow

interface HealthRepository {
    fun getHeartRateLast24h(): Flow<List<VitalSign>>
    fun getLatestBloodPressure(): Flow<Pair<VitalSign?, VitalSign?>>
    fun getMedications(): Flow<List<Medication>>
    suspend fun addVitalSign(vitalSign: VitalSign)
    suspend fun addMedication(medication: Medication)
    suspend fun toggleMedication(medication: Medication)
    suspend fun deleteMedication(medication: Medication)
}
