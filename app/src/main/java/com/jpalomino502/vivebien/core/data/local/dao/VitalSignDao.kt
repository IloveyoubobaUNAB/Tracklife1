package com.jpalomino502.vivebien.core.data.local.dao

import androidx.room.*
import com.jpalomino502.vivebien.core.data.local.entity.VitalSignEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VitalSignDao {
    @Query("SELECT * FROM vital_signs WHERE type = :type AND timestamp >= :since ORDER BY timestamp ASC")
    fun getByTypeSince(type: String, since: Long): Flow<List<VitalSignEntity>>

    @Query("SELECT * FROM vital_signs WHERE type = :type ORDER BY timestamp DESC LIMIT :limit")
    fun getLatestByType(type: String, limit: Int = 1): Flow<List<VitalSignEntity>>

    @Insert
    suspend fun insert(vitalSign: VitalSignEntity): Long
}
