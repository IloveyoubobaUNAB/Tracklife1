package com.jpalomino502.vivebien.core.di

import com.jpalomino502.vivebien.feature.auth.data.AuthRepository
import com.jpalomino502.vivebien.feature.auth.data.AuthRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository
}
