package com.example.propertymanagement.ui.settings_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.R
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.PaddingMedium
import com.example.propertymanagement.ui.theme.SpacerMedium

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordInfoBottomSheet(
    isSending: Boolean,
    onDismiss: () -> Unit,
    onContinue: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = PaddingLarge)
                .padding(bottom = PaddingLarge)
        ) {
            Text(
                text = stringResource(R.string.change_password_sheet_title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(PaddingMedium))

            Text(
                text = stringResource(R.string.change_password_sheet_message),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(SpacerMedium))

            Button(
                onClick = onContinue,
                enabled = !isSending,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.change_password_sheet_continue))
            }

            TextButton(
                onClick = onDismiss,
                enabled = !isSending,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.change_password_sheet_cancel))
            }
        }
    }
}
