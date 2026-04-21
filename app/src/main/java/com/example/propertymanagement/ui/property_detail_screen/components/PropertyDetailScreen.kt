package com.example.propertymanagement.ui.property_detail_screen.components

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.Property
import com.example.propertymanagement.domain.model.PropertyDetailPrices
import com.example.propertymanagement.ui.bottom_nav.Screens
import com.example.propertymanagement.ui.components.AppTopBar
import com.example.propertymanagement.ui.list_property_screen.components.ErrorState
import com.example.propertymanagement.ui.list_property_screen.components.FavoriteButton
import com.example.propertymanagement.ui.list_property_screen.components.LoadingState
import com.example.propertymanagement.ui.list_property_screen.components.PropertyImagePager
import com.example.propertymanagement.ui.property_detail_screen.PropertyDetailEvent
import com.example.propertymanagement.ui.property_detail_screen.PropertyDetailIntent
import com.example.propertymanagement.ui.property_detail_screen.PropertyDetailState
import com.example.propertymanagement.ui.property_detail_screen.PropertyDetailViewModel
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.PropertyDetailHeroAspectRatio
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun PropertyDetailScreen(
    navController: NavController,
    propertyId: Int,
    userId: String
) {
    val viewModel: PropertyDetailViewModel = koinViewModel(
        parameters = { parametersOf(propertyId, userId, false) }
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val intent = viewModel::processIntent
    val event: Flow<PropertyDetailEvent> by remember { mutableStateOf(viewModel.event) }
    val context = LocalContext.current
    val authRequiredText = stringResource(R.string.auth_required)
    val registrationRequiredText = stringResource(R.string.property_detail_request_requires_registration)
    val applicationSentText = stringResource(R.string.property_detail_application_sent)
    val applicationFailedText = stringResource(R.string.property_detail_application_failed)

    LaunchedEffect(
        event,
        authRequiredText,
        registrationRequiredText,
        applicationSentText,
        applicationFailedText
    ) {
        event.collect { e ->
            when (e) {
                PropertyDetailEvent.NavigateBack -> {
                    navController.navigate(Screens.Advertisements.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }

                PropertyDetailEvent.ShowAuthRequired -> {
                    Toast.makeText(
                        context,
                        authRequiredText,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                PropertyDetailEvent.ShowRegistrationRequiredForRequest -> {
                    Toast.makeText(
                        context,
                        registrationRequiredText,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                PropertyDetailEvent.ApplicationSubmitted -> {
                    Toast.makeText(
                        context,
                        applicationSentText,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                PropertyDetailEvent.ApplicationSubmitFailed -> {
                    Toast.makeText(
                        context,
                        applicationFailedText,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    BackHandler {
        intent(PropertyDetailIntent.NavigateBack)
    }

    PropertyDetailUI(
        state = state,
        intent = intent
    )
}

@Composable
private fun PropertyDetailUI(
    state: PropertyDetailState,
    intent: (PropertyDetailIntent) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(
            title = R.string.property_detail_title,
            onBackClick = { intent(PropertyDetailIntent.NavigateBack) },
            actions = {
                state.property?.let { property ->
                    FavoriteButton(
                        isFavorite = property.isFavorite,
                        onClick = { intent(PropertyDetailIntent.ToggleFavorite) },
                        contentDescription = stringResource(
                            if (property.isFavorite) {
                                R.string.property_detail_favorite_remove_cd
                            } else {
                                R.string.property_detail_favorite_add_cd
                            },
                        ),
                    )
                }
            }
        )

        when {
            state.isLoading -> LoadingState()
            state.error != null -> ErrorState(state.error)
            state.notFound -> ErrorState(stringResource(R.string.property_detail_not_found))
            else -> state.property?.let { property ->
                PropertyDetailLoadedContent(
                    property = property,
                    convertedPrices = state.convertedPrices,
                    intent = intent,
                )
            }
        }
    }

    val property = state.property
    if (state.isMapFullscreen && property != null) {
        PropertyDetailFullscreenMapDialog(
            property = property,
            onDismiss = { intent(PropertyDetailIntent.SetMapFullscreen(false)) }
        )
    }

    if (state.isImageViewerOpen && property != null && property.photos.isNotEmpty()) {
        PropertyDetailImageViewerDialog(
            photos = property.photos,
            initialPage = state.imageViewerInitialPage,
            onDismiss = { intent(PropertyDetailIntent.CloseImageViewer) }
        )
    }

    if (state.isApplicationSheetOpen) {
        PropertyDetailApplicationBottomSheet(
            state = state,
            intent = intent,
            onDismiss = { intent(PropertyDetailIntent.DismissApplicationSheet) }
        )
    }
}

@Composable
private fun PropertyDetailLoadedContent(
    property: Property,
    convertedPrices: PropertyDetailPrices?,
    intent: (PropertyDetailIntent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        PropertyImagePager(
            photos = property.photos,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(PropertyDetailHeroAspectRatio),
            onPhotoClick = { page ->
                intent(PropertyDetailIntent.OpenImageViewer(page))
            }
        )

        Column(modifier = Modifier.padding(PaddingLarge)) {
            PropertyDetailInfoSections(
                property = property,
                convertedPrices = convertedPrices,
                onOpenMapFullscreen = {
                    intent(PropertyDetailIntent.SetMapFullscreen(true))
                },
                onSubmitRequest = { intent(PropertyDetailIntent.SubmitRequest) },
            )
        }
    }
}
