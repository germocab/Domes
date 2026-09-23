package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.data.model.DomeColorEntity
import com.example.data.model.DomeEntity
import com.example.data.model.DomeWithColors
import kotlinx.coroutines.flow.Flow

@Dao
interface DomeDao {

    @Transaction
    @Query("SELECT * FROM domes ORDER BY createdAt DESC")
    fun getAllDomesWithColors(): Flow<List<DomeWithColors>>

    @Transaction
    @Query("SELECT * FROM domes WHERE id = :domeId")
    fun getDomeWithColors(domeId: Long): Flow<DomeWithColors?>

    @Query("SELECT * FROM dome_colors WHERE id = :colorId")
    fun getColorById(colorId: Long): Flow<DomeColorEntity?>

    @Query("SELECT * FROM dome_colors WHERE domeId = :domeId ORDER BY sortOrder ASC, id ASC")
    fun getColorsForDome(domeId: Long): Flow<List<DomeColorEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDome(dome: DomeEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertColors(colors: List<DomeColorEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertColor(color: DomeColorEntity): Long

    @Update
    suspend fun updateDome(dome: DomeEntity)

    @Update
    suspend fun updateColor(color: DomeColorEntity)

    @Query("DELETE FROM domes WHERE id = :domeId")
    suspend fun deleteDomeById(domeId: Long)

    @Query("DELETE FROM dome_colors WHERE id = :colorId")
    suspend fun deleteColorById(colorId: Long)

    @Query("SELECT COUNT(*) FROM domes")
    suspend fun getDomeCount(): Int
}
