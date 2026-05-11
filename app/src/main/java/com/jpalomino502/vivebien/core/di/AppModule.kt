package com.jpalomino502.vivebien.core.di

import com.jpalomino502.vivebien.feature.activity.data.ActivityRepository
import com.jpalomino502.vivebien.feature.activity.data.ActivityRepositoryImpl
import com.jpalomino502.vivebien.feature.appointments.data.AppointmentsRepository
import com.jpalomino502.vivebien.feature.appointments.data.AppointmentsRepositoryImpl
import com.jpalomino502.vivebien.feature.auth.data.AuthRepository
import com.jpalomino502.vivebien.feature.auth.data.AuthRepositoryImpl
import com.jpalomino502.vivebien.feature.health.data.HealthRepository
import com.jpalomino502.vivebien.feature.health.data.HealthRepositoryImpl
import com.jpalomino502.vivebien.feature.home.data.HomeRepository
import com.jpalomino502.vivebien.feature.home.data.HomeRepositoryImpl
import com.jpalomino502.vivebien.feature.profile.data.ProfileRepository
import com.jpalomino502.vivebien.feature.profile.data.ProfileRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds @Singleton abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository
    @Binds @Singleton abstract fun bindActivityRepository(impl: ActivityRepositoryImpl): ActivityRepository
    @Binds @Singleton abstract fun bindHealthRepository(impl: HealthRepositoryImpl): HealthRepository
    @Binds @Singleton abstract fun bindAppointmentsRepository(impl: AppointmentsRepositoryImpl): AppointmentsRepository
    @Binds @Singleton abstract fun bindProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository
    @Binds @Singleton abstract fun bindHomeRepository(impl: HomeRepositoryImpl): HomeRepository
}
