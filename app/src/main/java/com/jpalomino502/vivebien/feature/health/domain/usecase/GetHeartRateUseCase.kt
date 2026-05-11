package com.jpalomino502.vivebien.feature.health.domain.usecase

import com.jpalomino502.vivebien.feature.health.data.HealthRepository
import com.jpalomino502.vivebien.feature.health.domain.model.HeartRateData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetHeartRateUseCase @Inject constructor(private val repository: HealthRepository) {
    operator fun invoke(): Flow<HeartRateData> =
        repository.getHeartRateLast24h().map { signs ->
            if (signs.isEmpty()) HeartRateData()
            else HeartRateData(
                average = signs.map { it.value.toInt() }.average().toInt(),
                min = signs.minOf { it.value.toInt() },
                max = signs.maxOf { it.value.toInt() },
                hasData = true
            )
        }
}
