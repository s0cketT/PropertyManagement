package com.example.propertymanagement.di

import com.example.propertymanagement.domain.use_case.CheckUserExistsUseCase
import com.example.propertymanagement.domain.use_case.CreateFullPropertyUseCase
import com.example.propertymanagement.domain.use_case.DeletePropertyUseCase
import com.example.propertymanagement.domain.use_case.GetMyPropertiesUseCase
import com.example.propertymanagement.domain.use_case.GetMyPropertyApplicationsUseCase
import com.example.propertymanagement.domain.use_case.GetNearbyMapPoisUseCase
import com.example.propertymanagement.domain.use_case.UpdateFullPropertyUseCase
import com.example.propertymanagement.domain.use_case.FilterPropertiesUseCase
import com.example.propertymanagement.domain.use_case.GetCurrentUserUseCase
import com.example.propertymanagement.domain.use_case.GetCitiesByRegionUseCase
import com.example.propertymanagement.domain.use_case.GetManagerCommissionPercentUseCase
import com.example.propertymanagement.domain.use_case.GetTodayRatesUseCase
import com.example.propertymanagement.domain.use_case.GetPropertiesUseCase
import com.example.propertymanagement.domain.use_case.GetPropertyDetailPricesUseCase
import com.example.propertymanagement.domain.use_case.GetRegionsUseCase
import com.example.propertymanagement.domain.use_case.GetUserProfileUseCase
import com.example.propertymanagement.domain.use_case.SaveAppRatingUseCase
import com.example.propertymanagement.domain.use_case.LogoutUseCase
import com.example.propertymanagement.domain.use_case.ObserveLanguageUseCase
import com.example.propertymanagement.domain.use_case.ObserveLocationUseCase
import com.example.propertymanagement.domain.use_case.ObserveThemeUseCase
import com.example.propertymanagement.domain.use_case.SaveSelectedFiltersMarkerUseCase
import com.example.propertymanagement.domain.use_case.RequestEmailChangeUseCase
import com.example.propertymanagement.domain.use_case.SyncProfileEmailIfAuthMatchesUseCase
import com.example.propertymanagement.domain.use_case.SendOtpUseCase
import com.example.propertymanagement.domain.use_case.SetLanguageUseCase
import com.example.propertymanagement.domain.use_case.SetThemeUseCase
import com.example.propertymanagement.domain.use_case.SignInUseCase
import com.example.propertymanagement.domain.use_case.SignUpUseCase
import com.example.propertymanagement.domain.use_case.SubmitPropertyApplicationUseCase
import com.example.propertymanagement.domain.use_case.ToggleFavoriteUseCase
import com.example.propertymanagement.domain.use_case.UpdateUserAvatarUseCase
import com.example.propertymanagement.domain.use_case.UpdatePasswordUseCase
import com.example.propertymanagement.domain.use_case.UpdateUserProfileUseCase
import com.example.propertymanagement.domain.use_case.VerifyOtpUseCase
import com.example.propertymanagement.domain.model.MyAdsListingFilter
import com.example.propertymanagement.domain.use_case.GetFilterPropertyUseCase
import com.example.propertymanagement.domain.use_case.GetGeosuggestUseCase
import com.example.propertymanagement.ui.auth_screen.AuthViewModel
import com.example.propertymanagement.ui.favorites_screen.FavoriteViewModel
import com.example.propertymanagement.ui.filters_screen.FiltersViewModel
import com.example.propertymanagement.ui.filters_screen.city_selection.CitySelectionViewModel
import com.example.propertymanagement.ui.filters_screen.region_selection.RegionSelectionViewModel
import com.example.propertymanagement.ui.map.MapViewModel
import com.example.propertymanagement.ui.my_ads_screen.MyAdsViewModel
import com.example.propertymanagement.ui.my_applications_screen.MyApplicationsViewModel
import com.example.propertymanagement.ui.personal_info_screen.PersonalInfoViewModel
import com.example.propertymanagement.ui.profile_screen.ProfileViewModel
import com.example.propertymanagement.ui.property.ListPropertyViewModel
import com.example.propertymanagement.ui.property_detail_screen.PropertyDetailViewModel
import com.example.propertymanagement.ui.edit_property_screen.EditPropertyViewModel
import com.example.propertymanagement.ui.publish_screen.PublishViewModel
import com.example.propertymanagement.ui.change_email_screen.ChangeEmailLinkViewModel
import com.example.propertymanagement.ui.change_email_screen.ChangeNewEmailViewModel
import com.example.propertymanagement.ui.change_password_screen.SetNewPasswordViewModel
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
            getTodayRatesUseCase = get<GetTodayRatesUseCase>(),
            getManagerCommissionPercentUseCase = get<GetManagerCommissionPercentUseCase>(),
        )
    }

    viewModel<FiltersViewModel> {
        FiltersViewModel(
            getFilterPropertyUseCase = get<GetFilterPropertyUseCase>(),
            saveSelectedFiltersMarkerUseCase = get<SaveSelectedFiltersMarkerUseCase>(),
            getPropertiesUseCase = get<GetPropertiesUseCase>(),
            getCurrentUserUseCase = get<GetCurrentUserUseCase>(),
            filterPropertiesUseCase = get<FilterPropertiesUseCase>(),
            getTodayRatesUseCase = get<GetTodayRatesUseCase>(),
        )
    }

    viewModel<RegionSelectionViewModel> {
        RegionSelectionViewModel(
            getRegionsUseCase = get<GetRegionsUseCase>(),
        )
    }

    viewModel<CitySelectionViewModel> { (regionId: Long, selectedCityIds: Set<Long>) ->
        CitySelectionViewModel(
            regionId = regionId,
            initialSelectedCityIds = selectedCityIds,
            getCitiesByRegionUseCase = get<GetCitiesByRegionUseCase>(),
        )
    }

    viewModel {
        ListPropertyViewModel(
            getPropertiesUseCase = get<GetPropertiesUseCase>(),
            getCurrentUserUseCase = get<GetCurrentUserUseCase>(),
            toggleFavoriteUseCase = get<ToggleFavoriteUseCase>(),
            getFilterPropertyUseCase = get<GetFilterPropertyUseCase>(),
            filterPropertiesUseCase = get<FilterPropertiesUseCase>(),
            getTodayRatesUseCase = get<GetTodayRatesUseCase>(),
            getManagerCommissionPercentUseCase = get<GetManagerCommissionPercentUseCase>(),
        )
    }

    viewModel {
        PublishViewModel(
            createFullPropertyUseCase = get<CreateFullPropertyUseCase>(),
            getCurrentUserUseCase = get<GetCurrentUserUseCase>(),
            getGeosuggestUseCase = get<GetGeosuggestUseCase>(),
        )
    }

    viewModel { (propertyId: Int) ->
        EditPropertyViewModel(
            propertyId = propertyId,
            updateFullPropertyUseCase = get<UpdateFullPropertyUseCase>(),
            getMyPropertiesUseCase = get<GetMyPropertiesUseCase>(),
            getCurrentUserUseCase = get<GetCurrentUserUseCase>(),
        )
    }

    viewModel {
        AuthViewModel(
            sendOtpUseCase = get<SendOtpUseCase>(),
            verifyOtpUseCase = get<VerifyOtpUseCase>(),
            checkUserExistsUseCase = get<CheckUserExistsUseCase>(),
            signUpUseCase = get<SignUpUseCase>(),
            signInUseCase = get<SignInUseCase>(),
            logoutUseCase = get(),
        )
    }

    viewModel {
        ProfileViewModel(
            getCurrentUserUseCase = get<GetCurrentUserUseCase>(),
            getUserProfileUseCase = get<GetUserProfileUseCase>(),
            saveAppRatingUseCase = get<SaveAppRatingUseCase>()
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
            getCurrentUserUseCase = get<GetCurrentUserUseCase>(),
            sendOtpUseCase = get<SendOtpUseCase>(),
        )
    }

    viewModel {
        SetNewPasswordViewModel(
            updatePasswordUseCase = get<UpdatePasswordUseCase>()
        )
    }

    viewModel {
        ChangeNewEmailViewModel(
            getCurrentUserUseCase = get<GetCurrentUserUseCase>(),
            checkUserExistsUseCase = get<CheckUserExistsUseCase>(),
            requestEmailChangeUseCase = get<RequestEmailChangeUseCase>()
        )
    }

    viewModel { (expectedNewEmail: String) ->
        ChangeEmailLinkViewModel(
            expectedNewEmail = expectedNewEmail,
            syncProfileEmailIfAuthMatchesUseCase = get<SyncProfileEmailIfAuthMatchesUseCase>()
        )
    }

    viewModel {
        PersonalInfoViewModel(
            updateUserAvatarUseCase = get<UpdateUserAvatarUseCase>(),
            updateUserProfileUseCase = get<UpdateUserProfileUseCase>(),
            getUserProfileUseCase = get<GetUserProfileUseCase>()
        )
    }

    viewModel {
        FavoriteViewModel(
            getPropertiesUseCase = get<GetPropertiesUseCase>(),
            getCurrentUserUseCase = get<GetCurrentUserUseCase>(),
            toggleFavoriteUseCase = get<ToggleFavoriteUseCase>(),
            getTodayRatesUseCase = get<GetTodayRatesUseCase>(),
            getManagerCommissionPercentUseCase = get<GetManagerCommissionPercentUseCase>(),
        )
    }

    viewModel { (initialListingFilter: MyAdsListingFilter) ->
        MyAdsViewModel(
            initialListingFilter = initialListingFilter,
            getMyPropertiesUseCase = get<GetMyPropertiesUseCase>(),
            deletePropertyUseCase = get<DeletePropertyUseCase>(),
            getCurrentUserUseCase = get<GetCurrentUserUseCase>(),
            getTodayRatesUseCase = get<GetTodayRatesUseCase>(),
        )
    }

    viewModel {
        MyApplicationsViewModel(
            getMyPropertyApplicationsUseCase = get<GetMyPropertyApplicationsUseCase>(),
            getCurrentUserUseCase = get<GetCurrentUserUseCase>(),
        )
    }

    viewModel { (propertyId: Int, userId: String, useMyPropertiesForDetail: Boolean) ->
        PropertyDetailViewModel(
            propertyId = propertyId,
            userId = userId,
            useMyPropertiesForDetail = useMyPropertiesForDetail,
            getPropertiesUseCase = get<GetPropertiesUseCase>(),
            getMyPropertiesUseCase = get<GetMyPropertiesUseCase>(),
            getTodayRatesUseCase = get<GetTodayRatesUseCase>(),
            getPropertyDetailPricesUseCase = get<GetPropertyDetailPricesUseCase>(),
            getCurrentUserUseCase = get<GetCurrentUserUseCase>(),
            toggleFavoriteUseCase = get<ToggleFavoriteUseCase>(),
            submitPropertyApplicationUseCase = get<SubmitPropertyApplicationUseCase>(),
            getManagerCommissionPercentUseCase = get<GetManagerCommissionPercentUseCase>(),
            getFilterPropertyUseCase = get<GetFilterPropertyUseCase>(),
            getNearbyMapPoisUseCase = get<GetNearbyMapPoisUseCase>(),
        )
    }
}