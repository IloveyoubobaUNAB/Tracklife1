package com.jpalomino502.vivebien.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jpalomino502.vivebien.core.domain.model.Appointment

@Entity(tableName = "appointments")
data class AppointmentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val doctorName: String,
    val specialty: String,
    val dateTimeDisplay: String,
    val location: String = "",
    val isVirtual: Boolean = false,
    val dateTimestamp: Long
)

fun AppointmentEntity.toDomain() = Appointment(
    id = id,
    title = title,
    doctorName = doctorName,
    specialty = specialty,
    dateTime = dateTimeDisplay,
    location = location,
    isPast = dateTimestamp < System.currentTimeMillis(),
    isVirtual = isVirtual,
    dateTimestamp = dateTimestamp
)
