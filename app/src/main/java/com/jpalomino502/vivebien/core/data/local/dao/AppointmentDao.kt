package com.jpalomino502.vivebien.core.data.local.dao

import androidx.room.*
import com.jpalomino502.vivebien.core.data.local.entity.AppointmentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppointmentDao {
    @Query("SELECT * FROM appointments ORDER BY dateTimestamp ASC")
    fun getAll(): Flow<List<AppointmentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(appointment: AppointmentEntity): Long

    @Delete
    suspend fun delete(appointment: AppointmentEntity)
}
