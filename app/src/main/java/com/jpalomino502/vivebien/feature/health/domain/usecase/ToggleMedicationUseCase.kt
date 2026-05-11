package com.jpalomino502.vivebien.feature.health.domain.usecase

import com.jpalomino502.vivebien.core.domain.model.Medication
import com.jpalomino502.vivebien.feature.health.data.HealthRepository
import javax.inject.Inject

class ToggleMedicationUseCase @Inject constructor(private val repository: HealthRepository) {
    suspend operator fun invoke(medication: Medication) = repository.toggleMedication(medication)
}
