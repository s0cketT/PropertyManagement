package com.example.propertymanagement.data.common

import android.content.Context
import androidx.room.Room
import com.example.propertymanagement.data.local.AppDatabase


object DbFactory {
    fun createRoomDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "property_management.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}