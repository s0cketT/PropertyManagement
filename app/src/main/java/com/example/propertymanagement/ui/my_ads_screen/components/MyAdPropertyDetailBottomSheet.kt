package com.example.propertymanagement.ui.my_ads_screen.components

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.Property
import com.example.propertymanagement.domain.model.PropertyDetailPrices
import com.example.propertymanagement.ui.list_property_screen.components.ErrorState
import com.example.propertymanagement.ui.list_property_screen.components.LoadingState
import com.example.propertymanagement.ui.list_property_screen.components.PropertyImagePager
import com.example.propertymanagement.ui.property_detail_screen.PropertyDetailEvent
import com.example.propertymanagement.ui.property_detail_screen.PropertyDetailIntent
import com.example.propertymanagement.ui.property_detail_screen.PropertyDetailState
import com.example.propertymanagement.ui.property_detail_screen.PropertyDetailViewModel
import com.example.propertymanagement.ui.property_detail_screen.components.PropertyDetailFullscreenMapDialog
import com.example.propertymanagement.ui.property_detail_screen.components.PropertyDetailImageViewerDialog
import com.example.propertymanagement.ui.property_detail_screen.components.PropertyDetailInfoSections
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.PropertyDetailHeroAspectRatio
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAdPropertyDetailBottomSheet(
    propertyId: Int,
    userId: String,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        key(propertyId, userId) {
            val viewModel: PropertyDetailViewModel = koinViewModel(
                parameters = { parametersOf(propertyId, userId, true) },
            )
            val state by viewModel.state.collectAsStateWithLifecycle()
            val intent = viewModel::processIntent
            val event: Flow<PropertyDetailEvent> by remember {
                mutableStateOf(viewModel.event)
            }

            val context = LocalContext.current
            val authRequiredText = stringResource(R.string.auth_required)
            val registrationRequiredText =
                stringResource(R.string.property_detail_request_requires_registration)
            val ownPropertyRequestNotAllowedText =
                stringResource(R.string.property_detail_own_request_not_allowed)
            val applicationSentText = stringResource(R.string.property_detail_application_sent)
            val applicationFailedText = stringResource(R.string.property_detail_application_failed)

            LaunchedEffect(
                event,
                authRequiredText,
                registrationRequiredText,
                ownPropertyRequestNotAllowedText,
                applicationSentText,
                applicationFailedText,
            ) {
                event.collect { e ->
                    when (e) {
                        PropertyDetailEvent.NavigateBack -> onDismiss()

                        PropertyDetailEvent.ShowAuthRequired -> {
                            Toast.makeText(context, authRequiredText, Toast.LENGTH_SHORT).show()
                        }

                        PropertyDetailEvent.ShowRegistrationRequiredForRequest -> {
                            Toast.makeText(
                                context,
                                registrationRequiredText,
                                Toast.LENGTH_SHORT,
                            ).show()
                        }

                        PropertyDetailEvent.ShowOwnPropertyRequestNotAllowed -> {
                            Toast.makeText(
                                context,
                                ownPropertyRequestNotAllowedText,
                                Toast.LENGTH_SHORT,
                            ).show()
                        }

                        PropertyDetailEvent.ApplicationSubmitted -> {
                            Toast.makeText(context, applicationSentText, Toast.LENGTH_SHORT).show()
                        }

                        PropertyDetailEvent.ApplicationSubmitFailed -> {
                            Toast.makeText(
                                context,
                                applicationFailedText,
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }
                }
            }

            MyAdPropertyDetailSheetBody(
                state = state,
                intent = intent,
                onDismiss = onDismiss,
            )
        }
    }
}

@Composable
private fun MyAdPropertyDetailSheetBody(
    state: PropertyDetailState,
    intent: (PropertyDetailIntent) -> Unit,
    onDismiss: () -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = PaddingLarge),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.close))
            }
        }

        when {
            state.isLoading -> LoadingState()

            state.error != null -> ErrorState(state.error)

            state.notFound -> {
                Text(
                    text = stringResource(R.string.property_detail_not_found),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(PaddingLarge),
                )
            }

            else -> state.property?.let { property ->
                MyAdPropertyDetailScrollableContent(
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
            onDismiss = { intent(PropertyDetailIntent.SetMapFullscreen(false)) },
        )
    }

    if (state.isImageViewerOpen && property != null && property.photos.isNotEmpty()) {
        PropertyDetailImageViewerDialog(
            photos = property.photos,
            initialPage = state.imageViewerInitialPage,
            onDismiss = { intent(PropertyDetailIntent.CloseImageViewer) },
        )
    }
}

@Composable
private fun MyAdPropertyDetailScrollableContent(
    property: Property,
    convertedPrices: PropertyDetailPrices?,
    intent: (PropertyDetailIntent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
    ) {
        PropertyImagePager(
            photos = property.photos,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(PropertyDetailHeroAspectRatio),
            onPhotoClick = { page ->
                intent(PropertyDetailIntent.OpenImageViewer(page))
            },
        )

        Column(modifier = Modifier.padding(PaddingLarge)) {
            PropertyDetailInfoSections(
                property = property,
                convertedPrices = convertedPrices,
                managerCommissionPercent = 0.0,
                showBuyerCommissionCaption = false,
                cardPriceLeadCurrency = null,
                onOpenMapFullscreen = {
                    intent(PropertyDetailIntent.SetMapFullscreen(true))
                },
                onSubmitRequest = null,
            )
        }
    }
}
