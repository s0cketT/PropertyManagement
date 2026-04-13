package com.example.propertymanagement.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.propertymanagement.data.dao.FilterDao
import com.example.propertymanagement.data.local.dataStore
import com.example.propertymanagement.data.remote.INbrbApi
import com.example.propertymanagement.data.remote.ISupabaseApi
import com.example.propertymanagement.data.remote.auth.AuthService
import com.example.propertymanagement.data.remote.auth.AuthServiceImpl
import com.example.propertymanagement.data.repository.AuthRepositoryImpl
import com.example.propertymanagement.data.repository.CurrencyRepositoryImpl
import com.example.propertymanagement.data.repository.FavoriteRepositoryImpl
import com.example.propertymanagement.data.repository.FiltersRepositoryImpl
import com.example.propertymanagement.data.repository.IUserRepositoryImpl
import com.example.propertymanagement.data.repository.LocationRepositoryImpl
import com.example.propertymanagement.data.repository.PropertyRepositoryImpl
import com.example.propertymanagement.data.repository.SettingsRepositoryImpl
import com.example.propertymanagement.data.repository.StorageRepositoryImpl
import com.example.propertymanagement.data.repository.ThemeRepositoryImpl
import com.example.propertymanagement.domain.repository.AuthRepository
import com.example.propertymanagement.domain.repository.ICurrencyRepository
import com.example.propertymanagement.domain.repository.IFavoriteRepository
import com.example.propertymanagement.domain.repository.IFiltersRepository
import com.example.propertymanagement.domain.repository.ILocationRepository
import com.example.propertymanagement.domain.repository.IPropertyRepository
import com.example.propertymanagement.domain.repository.ISettingsRepository
import com.example.propertymanagement.domain.repository.IStorageRepository
import com.example.propertymanagement.domain.repository.IThemeRepository
import com.example.propertymanagement.domain.repository.IUserRepository
import com.google.android.gms.location.FusedLocationProviderClient
import io.github.jan.supabase.SupabaseClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module {

    single<IFiltersRepository> { FiltersRepositoryImpl(filterDao = get<FilterDao>()) }

    single<ICurrencyRepository> {
        CurrencyRepositoryImpl(apiService = get<INbrbApi>())
    }

    single<ILocationRepository> {
        LocationRepositoryImpl(
            fusedLocationClient = get<FusedLocationProviderClient>()
        )
    }

    single<IStorageRepository> {
        StorageRepositoryImpl(
            supabase = get<SupabaseClient>()
        )
    }

    single<IPropertyRepository> {
        PropertyRepositoryImpl(
            supabaseApi = get<ISupabaseApi>(),
            storageRepository = get<IStorageRepository>()
        )
    }

    single<AuthService> {
        AuthServiceImpl(supabase = get<SupabaseClient>())
    }

    single<AuthRepository> {
        AuthRepositoryImpl(service = get<AuthService>())
    }

    single<IUserRepository> {
        IUserRepositoryImpl(
            supabaseApi = get<ISupabaseApi>(),
            supabase = get<SupabaseClient>()
        )
    }

    single<DataStore<Preferences>> {
        androidContext().dataStore
    }

    single<ISettingsRepository> {
        SettingsRepositoryImpl(dataStore = get())
    }

    single<IThemeRepository> {
        ThemeRepositoryImpl(dataStore = get())
    }

    single<IFavoriteRepository> {
        FavoriteRepositoryImpl(supabaseApi = get<ISupabaseApi>())
    }
}