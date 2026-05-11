package com.jpalomino502.vivebien.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jpalomino502.vivebien.core.domain.model.Medication

@Entity(tableName = "medications")
data class MedicationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val dose: String,
    val frequency: String,
    val isActive: Boolean = true
)

fun MedicationEntity.toDomain() = Medication(
    id = id, name = name, dose = dose, frequency = frequency, isActive = isActive
)

fun Medication.toEntity() = MedicationEntity(
    id = id, name = name, dose = dose, frequency = frequency, isActive = isActive
)
