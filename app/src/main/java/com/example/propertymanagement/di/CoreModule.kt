package com.example.propertymanagement.di

import android.content.Context
import com.example.propertymanagement.ui.components.MapHelper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.koin.dsl.module

val coreModule = module {
    single<MapHelper> { MapHelper() }

    single<FusedLocationProviderClient> {
        LocationServices.getFusedLocationProviderClient(get<Context>())
    }
}