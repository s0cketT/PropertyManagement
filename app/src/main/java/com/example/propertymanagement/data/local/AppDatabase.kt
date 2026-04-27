package com.example.propertymanagement.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.propertymanagement.data.dao.FilterDao
import com.example.propertymanagement.data.model.FiltersPropertyDbModel

@Database(
    entities = [FiltersPropertyDbModel::class],
    version = 5,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun filterDao(): FilterDao
}