package com.jpalomino502.vivebien.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jpalomino502.vivebien.core.domain.model.VitalSign
import com.jpalomino502.vivebien.core.domain.model.VitalSignType

@Entity(tableName = "vital_signs")
data class VitalSignEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String,
    val value: Float,
    val unit: String,
    val timestamp: Long = System.currentTimeMillis()
)

fun VitalSignEntity.toDomain() = VitalSign(
    id = id,
    type = VitalSignType.valueOf(type),
    value = value,
    unit = unit,
    timestamp = timestamp
)
