package com.jpalomino502.vivebien.core.domain.model

data class Appointment(
    val id: Long = 0,
    val title: String,
    val doctorName: String,
    val specialty: String,
    val dateTime: String,
    val location: String = "",
    val isPast: Boolean = false,
    val isVirtual: Boolean = false,
    val dateTimestamp: Long = 0
)
