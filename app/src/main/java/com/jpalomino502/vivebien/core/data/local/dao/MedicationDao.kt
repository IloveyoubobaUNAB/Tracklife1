package com.jpalomino502.vivebien.core.data.local.dao

import androidx.room.*
import com.jpalomino502.vivebien.core.data.local.entity.MedicationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicationDao {
    @Query("SELECT * FROM medications ORDER BY name ASC")
    fun getAll(): Flow<List<MedicationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(medication: MedicationEntity): Long

    @Update
    suspend fun update(medication: MedicationEntity)

    @Delete
    suspend fun delete(medication: MedicationEntity)
}
