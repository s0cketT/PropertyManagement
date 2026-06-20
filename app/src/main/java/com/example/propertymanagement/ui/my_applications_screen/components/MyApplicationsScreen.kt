package com.example.propertymanagement.ui.my_applications_screen.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.propertymanagement.R
import com.example.propertymanagement.ui.components.AppTopBar
import com.example.propertymanagement.ui.list_property_screen.components.ErrorState
import com.example.propertymanagement.ui.list_property_screen.components.LoadingState
import com.example.propertymanagement.ui.my_applications_screen.MyApplicationsEvent
import com.example.propertymanagement.ui.my_applications_screen.MyApplicationsIntent
import com.example.propertymanagement.ui.my_applications_screen.MyApplicationsState
import com.example.propertymanagement.ui.my_applications_screen.MyApplicationsViewModel
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.SpacerMedium
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import java.util.Locale

@Composable
fun MyApplicationsScreen(
    navController: NavController,
) {
    val viewModel: MyApplicationsViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val intent = viewModel::processIntent
    val event: Flow<MyApplicationsEvent> by remember { mutableStateOf(viewModel.event) }

    LaunchedEffect(Unit) {
        event.collect { ev ->
            when (ev) {
                MyApplicationsEvent.NavigateBack -> navController.popBackStack()
            }
        }
    }

    BackHandler {
        intent(MyApplicationsIntent.NavigateBack)
    }

    UI(state = state, intent = intent)
}

@Composable
private fun UI(
    state: MyApplicationsState,
    intent: (MyApplicationsIntent) -> Unit,
) {
    val locale = LocalConfiguration.current.locales[0] ?: Locale.getDefault()
    val signInMessage = stringResource(R.string.my_applications_sign_in)
    val emptyMessage = stringResource(R.string.my_applications_empty)

    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(
            title = R.string.my_applications,
            onBackClick = { intent(MyApplicationsIntent.NavigateBack) },
        )

        when {
            state.currentUserId == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(PaddingLarge),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = signInMessage,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            state.isLoading -> LoadingState()

            state.error != null -> ErrorState(state.error)

            state.applications.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(PaddingLarge),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = emptyMessage,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(PaddingLarge),
                    verticalArrangement = Arrangement.spacedBy(SpacerMedium),
                ) {
                    items(
                        items = state.applications,
                        key = { it.id },
                    ) { application ->
                        MyApplicationCard(
                            application = application,
                            locale = locale,
                        )
                    }
                }
            }
        }
    }
}
