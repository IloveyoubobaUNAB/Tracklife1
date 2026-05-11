package com.jpalomino502.vivebien.feature.health.domain.usecase

import com.jpalomino502.vivebien.core.domain.model.Medication
import com.jpalomino502.vivebien.feature.health.data.HealthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMedicationsUseCase @Inject constructor(private val repository: HealthRepository) {
    operator fun invoke(): Flow<List<Medication>> = repository.getMedications()
}
