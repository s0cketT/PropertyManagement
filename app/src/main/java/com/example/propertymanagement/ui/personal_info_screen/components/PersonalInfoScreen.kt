package com.example.propertymanagement.ui.personal_info_screen.components

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.UserProfile
import com.example.propertymanagement.ui.bottom_nav.Screens
import com.example.propertymanagement.ui.components.AppTopBar
import com.example.propertymanagement.ui.components.PrimaryActionButton
import com.example.propertymanagement.ui.personal_info_screen.PersonalInfoEvent
import com.example.propertymanagement.ui.personal_info_screen.PersonalInfoIntent
import com.example.propertymanagement.ui.personal_info_screen.PersonalInfoState
import com.example.propertymanagement.ui.personal_info_screen.PersonalInfoViewModel
import com.example.propertymanagement.ui.theme.SpacerMedium
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel

@Composable
fun PersonalInfoScreen(
    navController: NavController,
    user: UserProfile,
    ) {

    val personalInfoViewModel: PersonalInfoViewModel = koinViewModel()
    val state by personalInfoViewModel.state.collectAsStateWithLifecycle()
    val intent = personalInfoViewModel::processIntent
    val event: Flow<PersonalInfoEvent> by remember { mutableStateOf(personalInfoViewModel.event) }
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        val bytes = uri?.let {
            try {
                context.contentResolver.openInputStream(it)?.use { stream ->
                    stream.readBytes()
                }
            } catch (e: Exception) {
                null
            }
        }
        bytes?.let {
            intent(PersonalInfoIntent.AvatarSelected(it))
        }
    }

    LaunchedEffect(Unit) {
        intent(PersonalInfoIntent.SetUser(user))
    }

    LaunchedEffect(Unit) {
        event.collect { event ->
            when (event) {

                is PersonalInfoEvent.NavigateBack -> {
                    navController.navigate(Screens.Profile.route)
                }

                is PersonalInfoEvent.OpenGallery -> {
                    launcher.launch("image/*")
                }

                is PersonalInfoEvent.SaveSuccess -> {
                    Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
                }

                is PersonalInfoEvent.SaveError -> {
                    Toast.makeText(
                        context,
                        event.message ?: "Error",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    UI(
        state = state,
        intent = intent
    )
}

@Composable
private fun UI(
    state: PersonalInfoState,
    intent: (PersonalInfoIntent) -> Unit,

) {
    Column {
        AppTopBar(
            title = R.string.personal_info_title,
            onBackClick = { intent(PersonalInfoIntent.OnBackClick) }
        )

        Spacer(modifier = Modifier.height(SpacerMedium))

        ProfileContent(
            state = state,
            user = state.user,
            avatarBytes = state.avatarBytes,
            onAvatarClick = { intent(PersonalInfoIntent.OnAvatarClick) },
            onRemoveClick = { intent(PersonalInfoIntent.RemoveAvatar) }
        )

        Spacer(modifier = Modifier.height(SpacerMedium))

        PrimaryActionButton(
            text = R.string.save,
            onClick = { intent(PersonalInfoIntent.Save) }
        )
    }
}
