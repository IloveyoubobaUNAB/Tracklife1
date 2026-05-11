package com.jpalomino502.vivebien.feature.home.domain.usecase

import com.jpalomino502.vivebien.feature.home.data.HomeDashboardData
import com.jpalomino502.vivebien.feature.home.data.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDashboardUseCase @Inject constructor(private val repository: HomeRepository) {
    operator fun invoke(): Flow<HomeDashboardData> = repository.getDashboardData()
}
