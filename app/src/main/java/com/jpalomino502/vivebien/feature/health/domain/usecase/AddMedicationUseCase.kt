package com.jpalomino502.vivebien.feature.health.domain.usecase

import com.jpalomino502.vivebien.core.domain.model.Medication
import com.jpalomino502.vivebien.feature.health.data.HealthRepository
import javax.inject.Inject

class AddMedicationUseCase @Inject constructor(private val repository: HealthRepository) {
    suspend operator fun invoke(name: String, dose: String, frequency: String): Result<Unit> {
        if (name.isBlank()) return Result.failure(IllegalArgumentException("El nombre del medicamento es obligatorio"))
        if (dose.isBlank()) return Result.failure(IllegalArgumentException("La dosis es obligatoria"))
        if (frequency.isBlank()) return Result.failure(IllegalArgumentException("La frecuencia es obligatoria"))
        repository.addMedication(Medication(name = name.trim(), dose = dose.trim(), frequency = frequency.trim()))
        return Result.success(Unit)
    }
}
