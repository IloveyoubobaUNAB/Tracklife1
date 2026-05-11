package com.jpalomino502.vivebien.feature.appointments.data

import com.jpalomino502.vivebien.core.data.local.dao.AppointmentDao
import com.jpalomino502.vivebien.core.data.local.entity.AppointmentEntity
import com.jpalomino502.vivebien.core.data.local.entity.toDomain
import com.jpalomino502.vivebien.core.domain.model.Appointment
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppointmentsRepositoryImpl @Inject constructor(
    private val dao: AppointmentDao
) : AppointmentsRepository {

    private fun allFlow(): Flow<List<Appointment>> =
        dao.getAll().map { list -> list.map { it.toDomain() } }

    override fun getUpcomingAppointments(): Flow<List<Appointment>> =
        allFlow().map { list -> list.filter { !it.isPast } }

    override fun getPastAppointments(): Flow<List<Appointment>> =
        allFlow().map { list -> list.filter { it.isPast } }

    override fun getNextAppointment(): Flow<Appointment?> =
        allFlow().map { list -> list.filter { !it.isPast }.minByOrNull { it.dateTimestamp } }

    override suspend fun addAppointment(appointment: Appointment) {
        dao.insert(
            AppointmentEntity(
                title = appointment.title,
                doctorName = appointment.doctorName,
                specialty = appointment.specialty,
                dateTimeDisplay = appointment.dateTime,
                location = appointment.location,
                isVirtual = appointment.isVirtual,
                dateTimestamp = appointment.dateTimestamp
            )
        )
    }

    override suspend fun deleteAppointment(appointment: Appointment) {
        dao.delete(
            AppointmentEntity(
                id = appointment.id,
                title = appointment.title,
                doctorName = appointment.doctorName,
                specialty = appointment.specialty,
                dateTimeDisplay = appointment.dateTime,
                location = appointment.location,
                isVirtual = appointment.isVirtual,
                dateTimestamp = appointment.dateTimestamp
            )
        )
    }
}
