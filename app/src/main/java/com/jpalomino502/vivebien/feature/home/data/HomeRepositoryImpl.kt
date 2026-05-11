package com.jpalomino502.vivebien.feature.home.data

import com.jpalomino502.vivebien.feature.activity.data.ActivityRepository
import com.jpalomino502.vivebien.feature.appointments.data.AppointmentsRepository
import com.jpalomino502.vivebien.feature.auth.data.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HomeRepositoryImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val appointmentsRepository: AppointmentsRepository,
    private val activityRepository: ActivityRepository
) : HomeRepository {

    override fun getDashboardData(): Flow<HomeDashboardData> {
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("EEEE, d 'de' MMMM", Locale("es", "CO"))
        val dateStr = today.format(formatter).replaceFirstChar { it.uppercase() }

        return combine(
            authRepository.getDisplayName(),
            appointmentsRepository.getNextAppointment(),
            activityRepository.getDailyStats()
        ) { displayName, nextAppt, stats ->
            HomeDashboardData(
                displayName = displayName.ifBlank { "Usuario" },
                currentDate = dateStr,
                planProgress = stats.goalPercent / 100f,
                planProgressPercent = stats.goalPercent,
                nextAppointment = nextAppt?.dateTime ?: "Sin citas próximas",
                dailyStats = stats
            )
        }
    }
}
