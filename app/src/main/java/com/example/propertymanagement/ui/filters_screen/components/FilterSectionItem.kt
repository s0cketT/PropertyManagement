package com.example.propertymanagement.ui.filters_screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.IntRangeFilter
import com.example.propertymanagement.ui.theme.DividerColor
import com.example.propertymanagement.ui.theme.DividerThickness
import com.example.propertymanagement.ui.theme.IconSizeArrow
import com.example.propertymanagement.ui.theme.OnSurfaceVariant
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.UnselectedGray
import com.example.propertymanagement.ui.theme.VerticalPaddingItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterSectionItem(
    titleResId: Int,
    values: List<Int>,
    displayMapper: (Int) -> String,
    range: IntRangeFilter? = null,
    onApply: (IntRangeFilter) -> Unit
) {
    var isSheetOpen by remember { mutableStateOf(false) }

    val displayText = range?.let {
        when {
            it.from != null && it.to != null -> stringResource(
                id = R.string.filter_from_to,
                displayMapper(it.from),
                displayMapper(it.to)
            )
            it.from != null -> stringResource(id = R.string.filter_from, displayMapper(it.from))
            it.to != null -> stringResource(id = R.string.filter_to, displayMapper(it.to))
            else -> stringResource(id = titleResId)
        }
    } ?: stringResource(id = titleResId)

    Column(modifier = Modifier.padding(horizontal = PaddingLarge)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isSheetOpen = true }
                .padding(vertical =
                    if (range?.from != null || range?.to != null) VerticalPaddingItem / 2 else VerticalPaddingItem
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                if (range?.from != null || range?.to != null) {
                    Text(
                        text = stringResource(id = titleResId),
                        style = MaterialTheme.typography.labelSmall,
                        color = UnselectedGray,

                    )
                }

                Text(
                    text = displayText,
                    style = MaterialTheme.typography.bodyLarge,
                    color = OnSurfaceVariant,
                )
            }

            if (range?.from != null || range?.to != null) {
                IconButton(onClick = { onApply(IntRangeFilter()) }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(id = R.string.clear_text),
                        tint = UnselectedGray
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = UnselectedGray,
                    modifier = Modifier.size(IconSizeArrow)
                )
            }
        }

        Divider(color = DividerColor, thickness = DividerThickness)
    }

    if (isSheetOpen) {
        FiltersBottomSheet(
            titleResId = titleResId,
            values = values,
            displayMapper = displayMapper,
            onClose = { isSheetOpen = false },
            onApply = {
                onApply(it)
                isSheetOpen = false
            }
        )
    }
}
