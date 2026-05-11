package com.jpalomino502.vivebien.feature.appointments.domain.usecase

import com.jpalomino502.vivebien.core.domain.model.Appointment
import com.jpalomino502.vivebien.feature.appointments.data.AppointmentsRepository
import javax.inject.Inject

class DeleteAppointmentUseCase @Inject constructor(private val repository: AppointmentsRepository) {
    suspend operator fun invoke(appointment: Appointment) = repository.deleteAppointment(appointment)
}
