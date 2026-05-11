package com.jpalomino502.vivebien.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jpalomino502.vivebien.core.domain.model.ActivityRecord

@Entity(tableName = "activities")
data class ActivityEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val durationMinutes: Int,
    val distanceKm: Float?,
    val scheduledTime: String?,
    val completed: Boolean = false,
    val date: Long = System.currentTimeMillis()
)

fun ActivityEntity.toDomain() = ActivityRecord(
    id = id,
    title = title,
    durationMinutes = durationMinutes,
    distanceKm = distanceKm,
    scheduledTime = scheduledTime,
    completed = completed,
    date = date
)

fun ActivityRecord.toEntity() = ActivityEntity(
    id = id,
    title = title,
    durationMinutes = durationMinutes,
    distanceKm = distanceKm,
    scheduledTime = scheduledTime,
    completed = completed,
    date = date
)
