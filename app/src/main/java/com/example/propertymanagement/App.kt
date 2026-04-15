package com.example.propertymanagement

import android.app.Application
import com.example.propertymanagement.data.common.Constants.API_KEY_MAPKIT
import com.example.propertymanagement.di.coreModule
import com.example.propertymanagement.di.dataModule
import com.example.propertymanagement.di.databaseModule
import com.example.propertymanagement.di.domainModule
import com.example.propertymanagement.di.networkModule
import com.example.propertymanagement.di.uiModule
import com.yandex.mapkit.MapKitFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(API_KEY_MAPKIT)
        startKoin {
            androidContext(this@App)
            modules(databaseModule, networkModule, dataModule, domainModule, uiModule, coreModule)
        }
    }
}