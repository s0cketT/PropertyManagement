package com.example.propertymanagement.di

import com.example.propertymanagement.domain.use_case.CheckUserExistsUseCase
import com.example.propertymanagement.domain.use_case.CreateFullPropertyUseCase
import com.example.propertymanagement.domain.use_case.FilterPropertiesUseCase
import com.example.propertymanagement.domain.use_case.GetCurrentUserUseCase
import com.example.propertymanagement.domain.use_case.GetFilterPropertyUseCase
import com.example.propertymanagement.domain.use_case.GetTodayRatesUseCase
import com.example.propertymanagement.domain.use_case.GetPropertiesUseCase
import com.example.propertymanagement.domain.use_case.GetPropertyDetailPricesUseCase
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
import com.example.propertymanagement.domain.use_case.VerifyOtpUseCase
import com.example.propertymanagement.ui.auth_screen.AuthViewModel
import com.example.propertymanagement.ui.favorites_screen.FavoriteViewModel
import com.example.propertymanagement.ui.filters_screen.FiltersViewModel
import com.example.propertymanagement.ui.map.MapViewModel
import com.example.propertymanagement.ui.personal_info_screen.PersonalInfoViewModel
import com.example.propertymanagement.ui.profile_screen.ProfileViewModel
import com.example.propertymanagement.ui.property.ListPropertyViewModel
import com.example.propertymanagement.ui.property_detail_screen.PropertyDetailViewModel
import com.example.propertymanagement.ui.publish_screen.PublishViewModel
import com.example.propertymanagement.ui.settings_screen.SettingsViewModel
import com.example.propertymanagement.ui.splash_screen.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel<MapViewModel> {
        MapViewModel(
            observeLocationUseCase = get<ObserveLocationUseCase>(),
            getPropertiesUseCase = get<GetPropertiesUseCase>(),
            getCurrentUserUseCase = get<GetCurrentUserUseCase>(),
            getFilterPropertyUseCase = get<GetFilterPropertyUseCase>(),
            filterPropertiesUseCase = get<FilterPropertiesUseCase>(),
            getTodayRatesUseCase = get<GetTodayRatesUseCase>()
        )
    }

    viewModel<FiltersViewModel> {
        FiltersViewModel(
            getFilterPropertyUseCase = get<GetFilterPropertyUseCase>(),
            saveSelectedFiltersMarkerUseCase = get<SaveSelectedFiltersMarkerUseCase>(),
        )
    }

    viewModel {
        ListPropertyViewModel(
            getPropertiesUseCase = get<GetPropertiesUseCase>(),
            getCurrentUserUseCase = get<GetCurrentUserUseCase>(),
            toggleFavoriteUseCase = get<ToggleFavoriteUseCase>(),
            getFilterPropertyUseCase = get<GetFilterPropertyUseCase>(),
            filterPropertiesUseCase = get<FilterPropertiesUseCase>(),
            getTodayRatesUseCase = get<GetTodayRatesUseCase>()
        )
    }

    viewModel {
        PublishViewModel(
            createFullPropertyUseCase = get<CreateFullPropertyUseCase>(),
            getCurrentUserUseCase = get<GetCurrentUserUseCase>()
        )
    }

    viewModel {
        AuthViewModel(
            sendOtpUseCase = get<SendOtpUseCase>(),
            verifyOtpUseCase = get<VerifyOtpUseCase>(),
            checkUserExistsUseCase = get<CheckUserExistsUseCase>(),
            signUpUseCase = get<SignUpUseCase>(),
            signInUseCase = get<SignInUseCase>()
        )
    }

    viewModel {
        ProfileViewModel(
            getCurrentUserUseCase = get<GetCurrentUserUseCase>(),
            getUserProfileUseCase = get<GetUserProfileUseCase>()
        )
    }

    viewModel {
        SplashViewModel(
            getCurrentUserUseCase = get<GetCurrentUserUseCase>()
        )
    }

    viewModel {
        SettingsViewModel(
            setLanguageUseCase = get<SetLanguageUseCase>(),
            observeLanguageUseCase = get<ObserveLanguageUseCase>(),
            logoutUseCase = get<LogoutUseCase>(),
            setThemeUseCase = get<SetThemeUseCase>(),
            observeThemeUseCase = get<ObserveThemeUseCase>(),
        )
    }

    viewModel {
        PersonalInfoViewModel(
            updateUserAvatarUseCase = get<UpdateUserAvatarUseCase>()
        )
    }

    viewModel {
        FavoriteViewModel(
            getPropertiesUseCase = get<GetPropertiesUseCase>(),
            getCurrentUserUseCase = get<GetCurrentUserUseCase>(),
            toggleFavoriteUseCase = get<ToggleFavoriteUseCase>(),
            getTodayRatesUseCase = get<GetTodayRatesUseCase>()
        )
    }

    viewModel { (propertyId: Int, userId: String) ->
        PropertyDetailViewModel(
            propertyId = propertyId,
            userId = userId,
            getPropertiesUseCase = get<GetPropertiesUseCase>(),
            getTodayRatesUseCase = get<GetTodayRatesUseCase>(),
            getPropertyDetailPricesUseCase = get<GetPropertyDetailPricesUseCase>(),
            getCurrentUserUseCase = get<GetCurrentUserUseCase>(),
            toggleFavoriteUseCase = get<ToggleFavoriteUseCase>()
        )
    }
}