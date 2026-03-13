package com.example.propertymanagement.di

import android.content.Context
import com.example.propertymanagement.data.dao.FilterDao
import com.example.propertymanagement.data.remote.INbrbApi
import com.example.propertymanagement.data.repository.CurrencyRepositoryImpl
import com.example.propertymanagement.data.repository.FiltersRepositoryImpl
import com.example.propertymanagement.data.repository.LocationRepositoryImpl
import com.example.propertymanagement.data.repository.PropertyRepositoryImpl
import com.example.propertymanagement.domain.repository.ICurrencyRepository
import com.example.propertymanagement.domain.repository.IFiltersRepository
import com.example.propertymanagement.domain.repository.ILocationRepository
import com.example.propertymanagement.domain.repository.IPropertyRepository
import com.example.propertymanagement.domain.use_case.ClearSelectedFiltersMarkerUseCase
import com.example.propertymanagement.domain.use_case.GetSelectedPropertyMarkerUseCase
import com.example.propertymanagement.domain.use_case.GetMarkersUseCase
import com.example.propertymanagement.domain.use_case.ObserveLocationUseCase
import com.example.propertymanagement.domain.use_case.SaveSelectedFiltersMarkerUseCase
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

    single<IFiltersRepository> { FiltersRepositoryImpl(filterDao = get<FilterDao>()) }

    single<ICurrencyRepository> {
        CurrencyRepositoryImpl(apiService = get<INbrbApi>())
    }

    single<ILocationRepository> {
        LocationRepositoryImpl(
            fusedLocationClient = get<FusedLocationProviderClient>()
        )
    }

    single<IPropertyRepository> {
        PropertyRepositoryImpl()
    }

    // UseCase для маркеров
    factory<GetMarkersUseCase> {
        GetMarkersUseCase(
            propertyRepository = get<IPropertyRepository>()
        )
    }

    factory<ObserveLocationUseCase> {
        ObserveLocationUseCase(
            ILocationRepository = get<ILocationRepository>()
        )
    }

    factory<GetSelectedPropertyMarkerUseCase> {
        GetSelectedPropertyMarkerUseCase(
            filtersRepository = get<IFiltersRepository>()
        )
    }

    factory<SaveSelectedFiltersMarkerUseCase> {
        SaveSelectedFiltersMarkerUseCase(
            filterRepository = get<IFiltersRepository>()
        )
    }

    factory<ClearSelectedFiltersMarkerUseCase> {
        ClearSelectedFiltersMarkerUseCase(
            filterRepository = get<IFiltersRepository>()
        )
    }

    viewModel<MapViewModel> {
        MapViewModel(
            observeLocationUseCase = get<ObserveLocationUseCase>(),
            getMarkersUseCase = get<GetMarkersUseCase>()
            )
    }

    viewModel<FiltersViewModel> {
        FiltersViewModel(
            getSelectedPropertyMarkerUseCase = get<GetSelectedPropertyMarkerUseCase>(),
            saveSelectedFiltersMarkerUseCase = get<SaveSelectedFiltersMarkerUseCase>(),
        )
    }
}