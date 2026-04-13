package com.example.propertymanagement.ui.personal_info_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.propertymanagement.domain.model.UserProfile
import com.example.propertymanagement.ui.personal_info_screen.PersonalInfoState
import com.example.propertymanagement.ui.theme.BoxGrayHeight
import com.example.propertymanagement.ui.theme.SpacerMedium

@Composable
fun ProfileContent(
    state: PersonalInfoState,
    user: UserProfile?,
    avatarBytes: ByteArray?,
    onAvatarClick: () -> Unit,
    onRemoveClick: () -> Unit
) {

    EmailRow(email = state.user?.email.orEmpty())

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(BoxGrayHeight)
            .background(MaterialTheme.colorScheme.surfaceVariant)
    )

    Spacer(modifier = Modifier.height(SpacerMedium))

    ProfileInfoRow(
        user = user,
        avatarBytes = avatarBytes,
        onAvatarClick = onAvatarClick,
        onRemoveClick = onRemoveClick,
        isAvatarRemoved = state.isAvatarRemoved
    )
}
