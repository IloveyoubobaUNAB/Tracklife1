package com.jpalomino502.vivebien.feature.health.domain.usecase

import com.jpalomino502.vivebien.core.domain.model.Medication
import com.jpalomino502.vivebien.feature.health.data.HealthRepository
import javax.inject.Inject

class DeleteMedicationUseCase @Inject constructor(private val repository: HealthRepository) {
    suspend operator fun invoke(medication: Medication) = repository.deleteMedication(medication)
}
