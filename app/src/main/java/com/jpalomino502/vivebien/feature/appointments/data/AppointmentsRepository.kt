package com.jpalomino502.vivebien.feature.appointments.data

import com.jpalomino502.vivebien.core.domain.model.Appointment
import kotlinx.coroutines.flow.Flow

interface AppointmentsRepository {
    fun getUpcomingAppointments(): Flow<List<Appointment>>
    fun getPastAppointments(): Flow<List<Appointment>>
    fun getNextAppointment(): Flow<Appointment?>
    suspend fun addAppointment(appointment: Appointment)
    suspend fun deleteAppointment(appointment: Appointment)
}
