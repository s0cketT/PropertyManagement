package com.example.propertymanagement.ui.filters_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.R
import com.example.propertymanagement.ui.filters_screen.FiltersIntent
import com.example.propertymanagement.ui.map_screen.MapIntent
import com.example.propertymanagement.ui.theme.SmallHorizontalPadding
import com.example.propertymanagement.ui.theme.SpacerBetweenElements
import com.example.propertymanagement.ui.theme.TopBarBackground
import com.example.propertymanagement.ui.theme.TopBarHeight

@Composable
fun CustomFilterTopBar(
    onCloseClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .background(TopBarBackground)
                .fillMaxWidth()
                .height(TopBarHeight)
                .padding(horizontal = SmallHorizontalPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Крестик слева
            IconButton(onClick = onCloseClick) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(R.string.close),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.width(SpacerBetweenElements))

            // Заголовок
            Text(
                text = stringResource(R.string.filters_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )

            // Очистить справа
            TextButton(onClick = { /* TODO: очистить все фильтры */ }) {
                Text(
                    text = stringResource(R.string.clear_filters),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }

    }
}