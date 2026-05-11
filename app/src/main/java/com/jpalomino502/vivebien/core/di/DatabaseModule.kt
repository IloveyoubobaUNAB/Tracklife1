package com.jpalomino502.vivebien.core.di

import android.content.Context
import androidx.room.Room
import com.jpalomino502.vivebien.core.data.local.AppDatabase
import com.jpalomino502.vivebien.core.data.local.dao.ActivityDao
import com.jpalomino502.vivebien.core.data.local.dao.AppointmentDao
import com.jpalomino502.vivebien.core.data.local.dao.MedicationDao
import com.jpalomino502.vivebien.core.data.local.dao.VitalSignDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "vivebien_db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides fun provideActivityDao(db: AppDatabase): ActivityDao = db.activityDao()
    @Provides fun provideAppointmentDao(db: AppDatabase): AppointmentDao = db.appointmentDao()
    @Provides fun provideMedicationDao(db: AppDatabase): MedicationDao = db.medicationDao()
    @Provides fun provideVitalSignDao(db: AppDatabase): VitalSignDao = db.vitalSignDao()
}
