package com.example.propertymanagement.ui.filters_screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.example.propertymanagement.ui.theme.BottomBarTextLineHeight
import com.example.propertymanagement.ui.theme.DividerColor
import com.example.propertymanagement.ui.theme.DividerThickness
import com.example.propertymanagement.ui.theme.IconSizeArrow
import com.example.propertymanagement.ui.theme.OnSurfaceVariant
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.PaddingMedium
import com.example.propertymanagement.ui.theme.PrimaryBlue
import com.example.propertymanagement.ui.theme.SpacerMedium
import com.example.propertymanagement.ui.theme.UnselectedGray
import com.example.propertymanagement.ui.theme.VerticalPaddingItem
import com.example.propertymanagement.ui.theme.VerticalPaddingItemSmall

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RangeFilterItem(
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

    Column(modifier = Modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isSheetOpen = true }
                .padding(vertical =
                    if (range?.from != null || range?.to != null) VerticalPaddingItemSmall else VerticalPaddingItem,
                    horizontal = PaddingLarge
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
                    color = if (range?.from != null || range?.to != null) OnSurfaceVariant else UnselectedGray,
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
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = UnselectedGray,
                    modifier = Modifier.size(IconSizeArrow)
                )
            }
        }

        HorizontalDivider(color = DividerColor, thickness = DividerThickness, modifier = Modifier.padding(horizontal = PaddingLarge))
    }

    if (isSheetOpen) {
        RangeFilterBottomSheet(
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RangeFilterBottomSheet(
    titleResId: Int,
    values: List<Int>,
    displayMapper: (Int) -> String,
    onClose: () -> Unit,
    onApply: (IntRangeFilter) -> Unit
) {

    var fromValue by remember { mutableStateOf<Int?>(null) }
    var toValue by remember { mutableStateOf<Int?>(null) }

    var resetTrigger by remember { mutableStateOf(0) }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { newValue ->
            newValue != SheetValue.Hidden
        }
    )

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {},
        dragHandle = null,
    ) {

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = PaddingLarge, vertical = PaddingMedium),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = stringResource(id = titleResId),
                    style = MaterialTheme.typography.titleMedium,
                    color = OnSurfaceVariant
                )

                IconButton(onClick = { onClose() }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(id = R.string.close),
                        tint = UnselectedGray,
                        modifier = Modifier.size(IconSizeArrow)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(PaddingLarge),
                horizontalArrangement = Arrangement.spacedBy(SpacerMedium)
            ) {
                WheelPicker(
                    values = values,
                    initialValue = -1,
                    resetTrigger = resetTrigger,
                    displayMapper = displayMapper,
                    onValueSelected = { value -> fromValue = value.takeIf { it != -1 } },
                    modifier = Modifier.weight(1f)
                )

                WheelPicker(
                    values = values,
                    initialValue = -1,
                    resetTrigger = resetTrigger,
                    displayMapper = displayMapper,
                    onValueSelected = { value ->
                        toValue = value.takeIf { it != -1 }
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            HorizontalDivider(
                color = DividerColor,
                thickness = DividerThickness
            )

            Spacer(modifier = Modifier.height(SpacerMedium))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = PaddingLarge,
                        end = PaddingLarge,
                        bottom = PaddingLarge
                    ),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = stringResource(id = R.string.clear_text),
                    color = UnselectedGray,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        lineHeight = BottomBarTextLineHeight
                    ),
                    modifier = Modifier
                        .clickable {
                            fromValue = null
                            toValue = null

                            resetTrigger++
                        }
                        .padding(PaddingMedium)
                )

                Text(
                    text = stringResource(id = R.string.apply_text),
                    color = PrimaryBlue,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        lineHeight = BottomBarTextLineHeight
                    ),
                    modifier = Modifier
                        .clickable {
                            onApply(
                                IntRangeFilter(
                                    from = fromValue,
                                    to = toValue
                                )
                            )
                            onClose()
                        }
                        .padding(PaddingMedium)
                )
            }
        }
    }
}
