package com.example.propertymanagement.di

import android.content.Context
import com.example.propertymanagement.data.repository.LocationRepositoryImpl
import com.example.propertymanagement.data.repository.PropertyRepositoryImpl
import com.example.propertymanagement.domain.repository.LocationRepository
import com.example.propertymanagement.domain.repository.PropertyRepository
import com.example.propertymanagement.domain.use_case.GetMarkersUseCase
import com.example.propertymanagement.domain.use_case.ObserveLocationUseCase
import com.example.propertymanagement.ui.components.MapHelper
import com.example.propertymanagement.ui.filters_screen.FiltersViewModel
import com.example.propertymanagement.ui.map.MapViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<MapHelper> { MapHelper() }

    single<FusedLocationProviderClient> {
        LocationServices.getFusedLocationProviderClient(get<Context>())
    }

    single<LocationRepository> {
        LocationRepositoryImpl(
            fusedLocationClient = get<FusedLocationProviderClient>()
        )
    }

    // Property repository
    single<PropertyRepository> {
        PropertyRepositoryImpl()
    }

    // UseCase для маркеров
    factory<GetMarkersUseCase> {
        GetMarkersUseCase(
            propertyRepository = get<PropertyRepository>()
        )
    }

    factory<ObserveLocationUseCase> {
        ObserveLocationUseCase(
            locationRepository = get<LocationRepository>()
        )
    }

    viewModel<MapViewModel> {
        MapViewModel(
            observeLocationUseCase = get<ObserveLocationUseCase>(),
            getMarkersUseCase = get<GetMarkersUseCase>()
            )
    }

    viewModel<FiltersViewModel> {
        FiltersViewModel()
    }
}