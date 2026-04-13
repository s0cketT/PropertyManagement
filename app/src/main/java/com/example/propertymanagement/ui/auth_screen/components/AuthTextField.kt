package com.example.propertymanagement.ui.auth_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.ui.auth_screen.AuthError
import com.example.propertymanagement.ui.auth_screen.AuthInfo
import com.example.propertymanagement.ui.mapper.asString
import com.example.propertymanagement.ui.publish_screen.components.PropertyTitleTextField

@Composable
fun AuthTextField(
    value: String,
    placeholderRes: Int,
    error: AuthError?,
    info: AuthInfo? = null,
    isPassword: Boolean = false,
    onValueChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {

        PropertyTitleTextField(
            value = value,
            placeholder = stringResource(placeholderRes),
            isError = error != null,
            isPassword = isPassword,
            onValueChange = onValueChange
        )

        error?.let {
            Text(
                text = it.asString(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        info?.let {
            Text(
                text = it.asString(),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}