package com.example.propertymanagement.ui.edit_property_screen.components

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import com.example.propertymanagement.domain.model.PropertyType
import com.example.propertymanagement.ui.bottom_nav.Screens
import com.example.propertymanagement.ui.components.AppTopBar
import com.example.propertymanagement.ui.edit_property_screen.EditPropertyEvent
import com.example.propertymanagement.ui.edit_property_screen.EditPropertyIntent
import com.example.propertymanagement.ui.edit_property_screen.EditPropertyState
import com.example.propertymanagement.ui.edit_property_screen.EditPropertyViewModel
import com.example.propertymanagement.ui.list_property_screen.components.LoadingState
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun EditPropertyScreen(
    navController: NavController,
    propertyId: Int,
) {
    val editPropertyViewModel: EditPropertyViewModel = koinViewModel(
        parameters = { parametersOf(propertyId) },
    )
    val state by editPropertyViewModel.state.collectAsStateWithLifecycle()
    val intent = editPropertyViewModel::processIntent
    val event: Flow<EditPropertyEvent> by remember { mutableStateOf(editPropertyViewModel.event) }

    val context = LocalContext.current
    val message = stringResource(R.string.auth_required)
    val validationLocationMessage = stringResource(R.string.publish_validation_location)

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
    ) { uris ->
        val imageBytes = uris.mapNotNull { uri ->
            try {
                context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
            } catch (e: Exception) {
                null
            }
        }

        intent(EditPropertyIntent.ImagesSelectedBytes(imageBytes))
    }

    val backStackEntry = navController.currentBackStackEntry!!
    val savedStateHandle = backStackEntry.savedStateHandle

    val selectedTypeName by savedStateHandle
        .getStateFlow<String?>("selected_property_type", null)
        .collectAsStateWithLifecycle()

    val selectedType = selectedTypeName?.let {
        try {
            PropertyType.valueOf(it)
        } catch (_: Exception) {
            null
        }
    }

    LaunchedEffect(selectedType) {
        if (selectedType != null) {
            intent(EditPropertyIntent.SetPropertyType(selectedType))
            savedStateHandle.remove<String>("selected_property_type")
        }
    }

    LaunchedEffect(Unit) {
        event.collect { ev ->
            when (ev) {
                is EditPropertyEvent.NavigateToCategorySelection -> {
                    navController.navigate(Screens.CategorySelection.route)
                }

                is EditPropertyEvent.OpenGallery -> { launcher.launch("image/*") }

                is EditPropertyEvent.SaveSuccess -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.edit_property_sent_to_moderation),
                        Toast.LENGTH_SHORT,
                    ).show()
                    navController.navigate(Screens.MyAdsScreen.route) {
                        popUpTo(Screens.Profile.route) {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                }

                is EditPropertyEvent.NavigateBack -> { navController.popBackStack() }

                is EditPropertyEvent.ShowAuthRequired -> { Toast.makeText(context, message, Toast.LENGTH_SHORT).show() }

                is EditPropertyEvent.ShowValidationError -> {
                    Toast.makeText(
                        context,
                        ev.message,
                        Toast.LENGTH_SHORT,
                    ).show()
                }

                is EditPropertyEvent.ShowValidationErrorRes -> {
                    Toast.makeText(
                        context,
                        validationLocationMessage,
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
        }
    }

    BackHandler() {
        intent(EditPropertyIntent.NavigateBack)
    }

    UI(state = state, intent = intent)
}

@Composable
private fun UI(
    state: EditPropertyState,
    intent: (EditPropertyIntent) -> Unit,
) {
    if (state.isLoadingEditPayload) {
        Column(modifier = Modifier.fillMaxSize()) {
            AppTopBar(
                title = R.string.edit_property_title,
                onBackClick = { intent(EditPropertyIntent.NavigateBack) },
            )
            Box(modifier = Modifier.weight(1f)) {
                LoadingState()
            }
        }
        return
    }

    EditPropertyFormContent(
        state = state,
        intent = intent,
        topBarTitleRes = R.string.edit_property_title,
        submitButtonRes = R.string.save_property_button,
    )
}
