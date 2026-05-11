package com.jpalomino502.vivebien.core.data.local.dao

import androidx.room.*
import com.jpalomino502.vivebien.core.data.local.entity.ActivityEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {
    @Query("SELECT * FROM activities WHERE date >= :start AND date < :end ORDER BY completed ASC, date ASC")
    fun getByDateRange(start: Long, end: Long): Flow<List<ActivityEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(activity: ActivityEntity): Long

    @Update
    suspend fun update(activity: ActivityEntity)

    @Delete
    suspend fun delete(activity: ActivityEntity)
}
