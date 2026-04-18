package com.example.propertymanagement.ui.my_ads_screen.components

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.MyAdsListingFilter
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

@Composable
fun MyAdsScreen(navController: NavController) {

    val viewModel: MyAdsViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val intent = viewModel::processIntent
    val event: Flow<MyAdsEvent> by remember { mutableStateOf(viewModel.event) }

    val authRequiredMessage = stringResource(R.string.auth_required)
    val myAdsSignInMessage = stringResource(R.string.my_ads_sign_in)
    val myAdsEmptyMessage = stringResource(R.string.my_ads_empty)
    val tabLabels = MyAdsListingFilter.entries.map { filter ->
        filter to stringResource(filter.titleRes())
    }

    LaunchedEffect(Unit) {
        event.collect { ev ->
            when (ev) {
                MyAdsEvent.NavigateBack -> navController.popBackStack()
                is MyAdsEvent.NavigateToDetail -> {
                    navController.navigate(
                        Screens.PropertyDetailScreen.createRoute(
                            propertyId = ev.property.id,
                            userId = ev.userId
                        )
                    )
                }

                MyAdsEvent.ShowAuthRequired -> {
                    Toast.makeText(
                        navController.context,
                        authRequiredMessage,
                        Toast.LENGTH_SHORT
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
        tabLabels = tabLabels
    )
}

@Composable
private fun MyAdsContent(
    state: MyAdsState,
    intent: (MyAdsIntent) -> Unit,
    myAdsSignInMessage: String,
    myAdsEmptyMessage: String,
    tabLabels: List<Pair<MyAdsListingFilter, String>>
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
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = PaddingLarge),
                    horizontalArrangement = Arrangement.spacedBy(PaddingSmall)
                ) {
                    items(tabLabels, key = { it.first }) { (filter, label) ->
                        FilterChip(
                            selected = state.listingFilter == filter,
                            onClick = { intent(MyAdsIntent.SelectListingFilter(filter)) },
                            label = { Text(label) }
                        )
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
                            onFavoriteClick = { intent(MyAdsIntent.ToggleFavorite(it)) },
                            onItemClick = { intent(MyAdsIntent.OnPropertyClick(it)) },
                            bottomTrailing = { property ->
                                ModerationStatusBadge(status = property.moderationStatus)
                            }
                        )
                    }
                }
            }
        }
    }
}
