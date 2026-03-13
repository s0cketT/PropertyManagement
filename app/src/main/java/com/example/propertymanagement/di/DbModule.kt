package com.example.propertymanagement.di

import com.example.propertymanagement.data.common.DbFactory
import com.example.propertymanagement.data.dao.FilterDao
import com.example.propertymanagement.data.local.AppDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module


val databaseModule = module {

    single<AppDatabase> {
        DbFactory.createRoomDatabase(context = androidApplication())
    }

    single<FilterDao> {
        get<AppDatabase>().filterDao()
    }
}