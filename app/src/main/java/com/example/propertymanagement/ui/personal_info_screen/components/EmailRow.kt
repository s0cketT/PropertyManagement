package com.example.propertymanagement.ui.personal_info_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.R
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.PaddingMedium
import com.example.propertymanagement.ui.theme.TextMedium
import com.example.propertymanagement.ui.theme.TextRegular

@Composable
fun EmailRow(
    email: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = PaddingLarge,
                vertical = PaddingMedium
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.profile_email),
            fontSize = TextMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = email,
            fontSize = TextRegular,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}