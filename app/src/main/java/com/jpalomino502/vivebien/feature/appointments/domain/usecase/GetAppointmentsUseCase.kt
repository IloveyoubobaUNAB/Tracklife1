package com.jpalomino502.vivebien.feature.appointments.domain.usecase

import com.jpalomino502.vivebien.core.domain.model.Appointment
import com.jpalomino502.vivebien.feature.appointments.data.AppointmentsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAppointmentsUseCase @Inject constructor(private val repository: AppointmentsRepository) {
    fun upcoming(): Flow<List<Appointment>> = repository.getUpcomingAppointments()
    fun past(): Flow<List<Appointment>> = repository.getPastAppointments()
}
