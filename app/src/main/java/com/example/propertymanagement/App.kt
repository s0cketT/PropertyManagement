package com.example.propertymanagement

import android.app.Application
import com.example.propertymanagement.data.common.Constants.API_KEY
import com.example.propertymanagement.di.appModule
import com.example.propertymanagement.di.databaseModule
import com.example.propertymanagement.di.networkModule
import com.yandex.mapkit.MapKitFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(API_KEY)
        startKoin {
            androidContext(this@App)
            modules(appModule, databaseModule, networkModule)
        }
    }
}