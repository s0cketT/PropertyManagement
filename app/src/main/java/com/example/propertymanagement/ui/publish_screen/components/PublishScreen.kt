package com.example.propertymanagement.ui.publish_screen.components

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.PropertyType
import com.example.propertymanagement.ui.bottom_nav.Screens
import com.example.propertymanagement.ui.publish_screen.PublishEvent
import com.example.propertymanagement.ui.publish_screen.PublishIntent
import com.example.propertymanagement.ui.publish_screen.PublishViewModel
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel

@Composable
fun PublishScreen(navController: NavController) {

    val publishViewModel: PublishViewModel = koinViewModel()
    val state by publishViewModel.state.collectAsStateWithLifecycle()
    val intent = publishViewModel::processIntent
    val event: Flow<PublishEvent> by remember { mutableStateOf(publishViewModel.event) }

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

        intent(PublishIntent.ImagesSelectedBytes(imageBytes))
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
            intent(PublishIntent.SetPropertyType(selectedType))
            savedStateHandle.remove<String>("selected_property_type")
        }
    }

    LaunchedEffect(Unit) {
        event.collect { ev ->
            when (ev) {
                is PublishEvent.NavigateToCategorySelection -> {
                    navController.navigate(Screens.CategorySelection.route)
                }

                is PublishEvent.OpenGallery -> {
                    launcher.launch("image/*")
                }

                is PublishEvent.NavigateBack -> {
                    navController.navigate(Screens.Advertisements.route)
                }

                is PublishEvent.ShowAuthRequired -> {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }

                is PublishEvent.ShowValidationError -> {
                    Toast.makeText(
                        context,
                        ev.message,
                        Toast.LENGTH_SHORT,
                    ).show()
                }

                is PublishEvent.ShowValidationErrorRes -> {
                    Toast.makeText(
                        context,
                        validationLocationMessage,
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
        }
    }

    PropertyEditorFormContent(
        state = state,
        intent = intent,
        topBarTitleRes = R.string.new_ad_title,
        submitButtonRes = R.string.publish_button,
    )
}
