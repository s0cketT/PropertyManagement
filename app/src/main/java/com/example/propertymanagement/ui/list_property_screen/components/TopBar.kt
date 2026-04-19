package com.example.propertymanagement.ui.list_property_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.R
import com.example.propertymanagement.ui.publish_screen.components.PropertyTitleTextField
import com.example.propertymanagement.ui.theme.ButtonCornerRadius
import com.example.propertymanagement.ui.theme.HorizontalPadding
import com.example.propertymanagement.ui.theme.PaddingMedium
import com.example.propertymanagement.ui.theme.TopBarEndIconBoxSize

@Composable
fun TopBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onFilterClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = HorizontalPadding, vertical = PaddingMedium),
        verticalAlignment = Alignment.CenterVertically
    ) {

        PropertyTitleTextField(
            value = query,
            placeholder = stringResource(R.string.search),
            onValueChange = onQueryChange,
            modifier = Modifier.weight(1f)
        )

        Box(
            modifier = Modifier
                .padding(start = PaddingMedium)
                .size(TopBarEndIconBoxSize)
                .clip(RoundedCornerShape(ButtonCornerRadius))
                .background(MaterialTheme.colorScheme.primary)
                .clickable { onFilterClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Tune, // лучше чем Build
                contentDescription = stringResource(R.string.filters_fab_content_description),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}
