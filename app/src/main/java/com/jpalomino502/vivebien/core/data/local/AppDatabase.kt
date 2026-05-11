package com.jpalomino502.vivebien.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jpalomino502.vivebien.core.data.local.dao.ActivityDao
import com.jpalomino502.vivebien.core.data.local.dao.AppointmentDao
import com.jpalomino502.vivebien.core.data.local.dao.MedicationDao
import com.jpalomino502.vivebien.core.data.local.dao.VitalSignDao
import com.jpalomino502.vivebien.core.data.local.entity.ActivityEntity
import com.jpalomino502.vivebien.core.data.local.entity.AppointmentEntity
import com.jpalomino502.vivebien.core.data.local.entity.MedicationEntity
import com.jpalomino502.vivebien.core.data.local.entity.VitalSignEntity

@Database(
    entities = [
        ActivityEntity::class,
        AppointmentEntity::class,
        MedicationEntity::class,
        VitalSignEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun activityDao(): ActivityDao
    abstract fun appointmentDao(): AppointmentDao
    abstract fun medicationDao(): MedicationDao
    abstract fun vitalSignDao(): VitalSignDao
}
