package com.example.propertymanagement.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.propertymanagement.data.model.FiltersPropertyDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface FilterDao {
    @Query("SELECT * FROM selected_property_marker LIMIT 1")
    fun getSelectedMarker(): Flow<FiltersPropertyDbModel?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: FiltersPropertyDbModel)

    @Query("DELETE FROM selected_property_marker")
    suspend fun clearSelectedMarker()
}