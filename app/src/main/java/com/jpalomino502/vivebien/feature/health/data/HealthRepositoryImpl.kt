package com.jpalomino502.vivebien.feature.health.data

import com.jpalomino502.vivebien.core.data.local.dao.MedicationDao
import com.jpalomino502.vivebien.core.data.local.dao.VitalSignDao
import com.jpalomino502.vivebien.core.data.local.entity.VitalSignEntity
import com.jpalomino502.vivebien.core.data.local.entity.toDomain
import com.jpalomino502.vivebien.core.data.local.entity.toEntity
import com.jpalomino502.vivebien.core.domain.model.Medication
import com.jpalomino502.vivebien.core.domain.model.VitalSign
import com.jpalomino502.vivebien.core.domain.model.VitalSignType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HealthRepositoryImpl @Inject constructor(
    private val vitalSignDao: VitalSignDao,
    private val medicationDao: MedicationDao
) : HealthRepository {

    override fun getHeartRateLast24h(): Flow<List<VitalSign>> {
        val since = System.currentTimeMillis() - 24 * 60 * 60 * 1000L
        return vitalSignDao.getByTypeSince(VitalSignType.HEART_RATE.name, since)
            .map { it.map { e -> e.toDomain() } }
    }

    override fun getLatestBloodPressure(): Flow<Pair<VitalSign?, VitalSign?>> =
        combine(
            vitalSignDao.getLatestByType(VitalSignType.BLOOD_PRESSURE_SYSTOLIC.name),
            vitalSignDao.getLatestByType(VitalSignType.BLOOD_PRESSURE_DIASTOLIC.name)
        ) { sys, dia -> sys.firstOrNull()?.toDomain() to dia.firstOrNull()?.toDomain() }

    override fun getMedications(): Flow<List<Medication>> =
        medicationDao.getAll().map { it.map { e -> e.toDomain() } }

    override suspend fun addVitalSign(vitalSign: VitalSign) {
        vitalSignDao.insert(
            VitalSignEntity(type = vitalSign.type.name, value = vitalSign.value, unit = vitalSign.unit, timestamp = vitalSign.timestamp)
        )
    }

    override suspend fun addMedication(medication: Medication) = medicationDao.insert(medication.toEntity()).let {}
    override suspend fun toggleMedication(medication: Medication) = medicationDao.update(medication.copy(isActive = !medication.isActive).toEntity())
    override suspend fun deleteMedication(medication: Medication) = medicationDao.delete(medication.toEntity())
}
