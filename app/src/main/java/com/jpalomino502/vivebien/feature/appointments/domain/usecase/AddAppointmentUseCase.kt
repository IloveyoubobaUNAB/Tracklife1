package com.jpalomino502.vivebien.feature.appointments.domain.usecase

import com.jpalomino502.vivebien.core.domain.model.Appointment
import com.jpalomino502.vivebien.feature.appointments.data.AppointmentsRepository
import javax.inject.Inject

class AddAppointmentUseCase @Inject constructor(private val repository: AppointmentsRepository) {
    suspend operator fun invoke(
        title: String,
        doctorName: String,
        specialty: String,
        dateTimeDisplay: String,
        dateTimestamp: Long,
        location: String,
        isVirtual: Boolean
    ): Result<Unit> {
        if (title.isBlank()) return Result.failure(IllegalArgumentException("El título es obligatorio"))
        if (doctorName.isBlank()) return Result.failure(IllegalArgumentException("El nombre del médico es obligatorio"))
        if (dateTimestamp <= 0) return Result.failure(IllegalArgumentException("La fecha es inválida"))
        repository.addAppointment(
            Appointment(
                title = title.trim(),
                doctorName = doctorName.trim(),
                specialty = specialty.trim(),
                dateTime = dateTimeDisplay,
                location = location.trim(),
                isVirtual = isVirtual,
                dateTimestamp = dateTimestamp
            )
        )
        return Result.success(Unit)
    }
}
