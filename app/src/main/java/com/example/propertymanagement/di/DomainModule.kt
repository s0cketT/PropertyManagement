package com.example.propertymanagement.di

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
import com.example.propertymanagement.domain.use_case.CheckUserExistsUseCase
import com.example.propertymanagement.domain.use_case.ClearSelectedFiltersMarkerUseCase
import com.example.propertymanagement.domain.use_case.CreateFullPropertyUseCase
import com.example.propertymanagement.domain.use_case.FilterPropertiesUseCase
import com.example.propertymanagement.domain.use_case.GetCurrentUserUseCase
import com.example.propertymanagement.domain.use_case.GetFilterPropertyUseCase
import com.example.propertymanagement.domain.use_case.GetMyPropertiesUseCase
import com.example.propertymanagement.domain.use_case.GetPropertiesUseCase
import com.example.propertymanagement.domain.use_case.GetPropertyDetailPricesUseCase
import com.example.propertymanagement.domain.use_case.GetTodayRatesUseCase
import com.example.propertymanagement.domain.use_case.GetUserProfileUseCase
import com.example.propertymanagement.domain.use_case.LogoutUseCase
import com.example.propertymanagement.domain.use_case.ObserveLanguageUseCase
import com.example.propertymanagement.domain.use_case.ObserveLocationUseCase
import com.example.propertymanagement.domain.use_case.ObserveThemeUseCase
import com.example.propertymanagement.domain.use_case.SaveSelectedFiltersMarkerUseCase
import com.example.propertymanagement.domain.use_case.SendOtpUseCase
import com.example.propertymanagement.domain.use_case.SetLanguageUseCase
import com.example.propertymanagement.domain.use_case.SetThemeUseCase
import com.example.propertymanagement.domain.use_case.SignInUseCase
import com.example.propertymanagement.domain.use_case.SignUpUseCase
import com.example.propertymanagement.domain.use_case.ToggleFavoriteUseCase
import com.example.propertymanagement.domain.use_case.UpdateUserAvatarUseCase
import com.example.propertymanagement.domain.use_case.UpdateUserProfileUseCase
import com.example.propertymanagement.domain.use_case.RequestEmailChangeUseCase
import com.example.propertymanagement.domain.use_case.SyncProfileEmailIfAuthMatchesUseCase
import com.example.propertymanagement.domain.use_case.UpdatePasswordUseCase
import com.example.propertymanagement.domain.use_case.VerifyOtpUseCase
import org.koin.dsl.module

val domainModule = module {

    factory<ObserveLocationUseCase> {
        ObserveLocationUseCase(
            ILocationRepository = get<ILocationRepository>()
        )
    }

    factory<GetFilterPropertyUseCase> {
        GetFilterPropertyUseCase(
            filtersRepository = get<IFiltersRepository>()
        )
    }

    factory<SaveSelectedFiltersMarkerUseCase> {
        SaveSelectedFiltersMarkerUseCase(
            filterRepository = get<IFiltersRepository>()
        )
    }

    factory<ClearSelectedFiltersMarkerUseCase> { ClearSelectedFiltersMarkerUseCase(filterRepository = get<IFiltersRepository>()) }

    factory { GetPropertiesUseCase(propertyRepository = get<IPropertyRepository>()) }

    factory { GetMyPropertiesUseCase(propertyRepository = get<IPropertyRepository>()) }

    factory { CreateFullPropertyUseCase(
        propertyRepository = get<IPropertyRepository>(),
        storageRepository = get<IStorageRepository>())
    }
    factory {
        UpdateUserAvatarUseCase(
            userRepository = get<IUserRepository>(),
            storageRepository = get<IStorageRepository>()
        )
    }

    factory { UpdateUserProfileUseCase(userRepository = get<IUserRepository>()) }

    factory { LogoutUseCase(authRepository = get<AuthRepository>()) }

    factory { CheckUserExistsUseCase(IUserRepository = get<IUserRepository>()) }

    factory { SendOtpUseCase(authRepository = get<AuthRepository>()) }
    factory { VerifyOtpUseCase(authRepository = get<AuthRepository>()) }

    factory { SignUpUseCase(authRepository = get<AuthRepository>()) }
    factory { SignInUseCase(authRepository = get<AuthRepository>()) }
    factory { UpdatePasswordUseCase(authRepository = get<AuthRepository>()) }
    factory { RequestEmailChangeUseCase(authRepository = get<AuthRepository>()) }
    factory {
        SyncProfileEmailIfAuthMatchesUseCase(
            authRepository = get<AuthRepository>(),
            userRepository = get<IUserRepository>()
        )
    }
    factory { GetCurrentUserUseCase(authRepository = get<AuthRepository>()) }
    factory { GetUserProfileUseCase(userRepository = get<IUserRepository>()) }

    factory { SetLanguageUseCase(settingsRepository = get<ISettingsRepository>()) }
    factory { ObserveLanguageUseCase(settingsRepository = get<ISettingsRepository>()) }

    factory { SetThemeUseCase(themeRepository = get<IThemeRepository>()) }
    factory { ObserveThemeUseCase(themeRepository = get<IThemeRepository>()) }


    factory { ToggleFavoriteUseCase(favoriteRepository = get<IFavoriteRepository>()) }

    factory { FilterPropertiesUseCase() }

    factory { GetTodayRatesUseCase(currencyRepository = get<ICurrencyRepository>()) }

    factory { GetPropertyDetailPricesUseCase() }

}