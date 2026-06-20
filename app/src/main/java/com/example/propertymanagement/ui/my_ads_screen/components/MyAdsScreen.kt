package com.example.propertymanagement.ui.my_ads_screen.components

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.ModerationStatus
import com.example.propertymanagement.domain.model.MyAdsListingFilter
import com.example.propertymanagement.domain.model.Property
import com.example.propertymanagement.ui.bottom_nav.Screens
import com.example.propertymanagement.ui.components.AppTopBar
import com.example.propertymanagement.ui.list_property_screen.components.ErrorState
import com.example.propertymanagement.ui.list_property_screen.components.LoadingState
import com.example.propertymanagement.ui.list_property_screen.components.ModerationStatusBadge
import com.example.propertymanagement.ui.list_property_screen.components.PropertyList
import com.example.propertymanagement.ui.mapper.titleRes
import com.example.propertymanagement.ui.my_ads_screen.MyAdsEvent
import com.example.propertymanagement.ui.my_ads_screen.MyAdsIntent
import com.example.propertymanagement.ui.my_ads_screen.MyAdsState
import com.example.propertymanagement.ui.my_ads_screen.MyAdsViewModel
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.PaddingSmall
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun MyAdsScreen(
    navController: NavController,
    initialListingFilter: MyAdsListingFilter = MyAdsListingFilter.PUBLISHED,
) {

    val viewModel: MyAdsViewModel = koinViewModel(
        parameters = { parametersOf(initialListingFilter) },
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val intent = viewModel::processIntent
    val event: Flow<MyAdsEvent> by remember { mutableStateOf(viewModel.event) }

    val authRequiredMessage = stringResource(R.string.auth_required)
    val deleteFailedMessage = stringResource(R.string.my_ads_delete_failed)
    val myAdsSignInMessage = stringResource(R.string.my_ads_sign_in)
    val myAdsEmptyMessage = stringResource(R.string.my_ads_empty)
    val tabLabels = MyAdsListingFilter.entries.map { filter ->
        filter to stringResource(filter.titleRes())
    }

    LaunchedEffect(Unit) {
        event.collect { ev ->
            when (ev) {
                MyAdsEvent.NavigateBack -> navController.popBackStack()

                MyAdsEvent.ShowAuthRequired -> {
                    Toast.makeText(
                        navController.context,
                        authRequiredMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                MyAdsEvent.ShowDeleteFailed -> {
                    Toast.makeText(
                        navController.context,
                        deleteFailedMessage,
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
        }
    }

    BackHandler {
        intent(MyAdsIntent.NavigateBack)
    }

    MyAdsContent(
        state = state,
        intent = intent,
        myAdsSignInMessage = myAdsSignInMessage,
        myAdsEmptyMessage = myAdsEmptyMessage,
        tabLabels = tabLabels,
        onEditProperty = { property ->
            navController.navigate(Screens.EditPropertyScreen.createRoute(property.id))
        },
    )

    state.detailSheetKey?.let { sheetKey ->
        key(sheetKey.propertyId) {
            MyAdPropertyDetailBottomSheet(
                propertyId = sheetKey.propertyId,
                userId = sheetKey.userId,
                onDismiss = { intent(MyAdsIntent.DismissPropertyDetailSheet) },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyAdsContent(
    state: MyAdsState,
    intent: (MyAdsIntent) -> Unit,
    myAdsSignInMessage: String,
    myAdsEmptyMessage: String,
    tabLabels: List<Pair<MyAdsListingFilter, String>>,
    onEditProperty: (Property) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(
            title = R.string.my_ads,
            onBackClick = { intent(MyAdsIntent.NavigateBack) }
        )

        when {
            state.currentUserId == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(PaddingLarge),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = myAdsSignInMessage,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            else -> {
                val selectedTabIndex = tabLabels.indexOfFirst { it.first == state.listingFilter }
                    .coerceAtLeast(0)

                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = PaddingLarge),
                ) {
                    tabLabels.forEachIndexed { index, (filter, label) ->
                        SegmentedButton(
                            shape = SegmentedButtonDefaults.itemShape(
                                index = index,
                                count = tabLabels.size,
                            ),
                            onClick = { intent(MyAdsIntent.SelectListingFilter(filter)) },
                            selected = index == selectedTabIndex,
                            icon = { },
                        ) {
                            Text(
                                text = label,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.labelMedium,
                            )
                        }
                    }
                }

                when {
                    state.isLoading -> LoadingState()
                    state.error != null -> ErrorState(state.error)
                    state.visibleList.isEmpty() -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(PaddingLarge),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = myAdsEmptyMessage,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    else -> {
                        PropertyList(
                            list = state.visibleList,
                            currencyRates = state.currencyRates,
                            managerCommissionPercent = 0.0,
                            onFavoriteClick = { },
                            onItemClick = { intent(MyAdsIntent.OnPropertyClick(it)) },
                            bottomTrailing = { property ->
                                Column {
                                    ModerationStatusBadge(status = property.moderationStatus)
                                    val moderationComment = property.moderationComment?.trim().orEmpty()
                                    if (
                                        property.moderationStatus == ModerationStatus.REJECTED &&
                                        moderationComment.isNotEmpty()
                                    ) {
                                        Text(
                                            text = stringResource(
                                                R.string.my_ads_rejection_comment,
                                                moderationComment,
                                            ),
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.error,
                                            modifier = Modifier.padding(top = PaddingSmall),
                                        )
                                    }
                                }
                            },
                            showFavoriteButton = false,
                            onEditClick = onEditProperty,
                            showEditFor = { property ->
                                property.moderationStatus == ModerationStatus.APPROVED ||
                                    property.moderationStatus == ModerationStatus.REJECTED
                            },
                            onDeleteClick = { property ->
                                intent(MyAdsIntent.RequestDeleteProperty(property))
                            },
                            showDeleteFor = { property ->
                                property.moderationStatus == ModerationStatus.APPROVED ||
                                    property.moderationStatus == ModerationStatus.REJECTED
                            },
                        )
                    }
                }
            }
        }
    }

    val deleteCandidate = state.deleteCandidate
    if (deleteCandidate != null) {
        AlertDialog(
            onDismissRequest = { intent(MyAdsIntent.DismissDeleteDialog) },
            title = { Text(text = stringResource(R.string.my_ads_delete_dialog_title)) },
            text = {
                Text(
                    text = stringResource(
                        R.string.my_ads_delete_dialog_message,
                        deleteCandidate.title,
                    ),
                )
            },
            confirmButton = {
                TextButton(
                    onClick = { intent(MyAdsIntent.ConfirmDeleteProperty) },
                    enabled = !state.isDeleting,
                ) {
                    Text(
                        text = stringResource(
                            if (state.isDeleting) {
                                R.string.my_ads_delete_dialog_deleting
                            } else {
                                R.string.my_ads_delete_dialog_confirm
                            },
                        ),
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { intent(MyAdsIntent.DismissDeleteDialog) },
                    enabled = !state.isDeleting,
                ) {
                    Text(text = stringResource(R.string.my_ads_delete_dialog_cancel))
                }
            },
        )
    }
}
