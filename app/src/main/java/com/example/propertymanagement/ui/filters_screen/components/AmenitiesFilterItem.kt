package com.example.propertymanagement.ui.filters_screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import com.example.propertymanagement.R
import com.example.propertymanagement.ui.extensions.toggle
import com.example.propertymanagement.ui.theme.DividerThickness
import com.example.propertymanagement.ui.theme.IconSizeArrow
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.VerticalPaddingItem
import com.example.propertymanagement.ui.theme.VerticalPaddingItemSmall

@Composable
fun <T : Enum<T>> AmenitiesFilterItem(
    titleResId: Int,
    items: List<T>,
    selected: Set<T>,
    titleRes: (T) -> Int,
    onApply: (Set<T>) -> Unit
) {

    var isSheetOpen by rememberSaveable { mutableStateOf(false) }
    var local by rememberSaveable { mutableStateOf(selected) }

    Column {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    local = selected
                    isSheetOpen = true
                }
                .padding(
                    vertical = VerticalPaddingItem,
                    horizontal = PaddingLarge
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = buildAnnotatedString {

                    if (selected.isEmpty()) {
                        append(stringResource(titleResId))
                    } else {

                        selected.forEachIndexed { index, item ->

                            append(stringResource(titleRes(item)))

                            if (index < selected.size - 1) {
                                append(", ")
                            }
                        }
                    }
                },
                style = MaterialTheme.typography.bodyLarge,
                color = if (selected.isEmpty())
                    MaterialTheme.colorScheme.onSurfaceVariant
                else
                    MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = { if (selected.isNotEmpty()) onApply(emptySet()) },
                enabled = selected.isNotEmpty(),
                modifier = Modifier.size(IconSizeArrow)
            ) {

                Icon(
                    imageVector = if (selected.isEmpty())
                        Icons.AutoMirrored.Filled.KeyboardArrowRight
                    else
                        Icons.Default.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        HorizontalDivider(
                modifier = Modifier.padding(horizontal = PaddingLarge),
        thickness = DividerThickness,
            color = MaterialTheme.colorScheme.outlineVariant
        )
    }

    if (isSheetOpen) {

        AmenitiesFilterBottomSheet(
            titleResId = titleResId,
            items = items,
            selected = local,
            titleRes = titleRes,
            onToggle = { item -> local = local.toggle(item) },
            onClear = { local = emptySet() },
            onApply = {
                onApply(local)
                isSheetOpen = false
            },
            onDismiss = { isSheetOpen = false }
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T : Enum<T>> AmenitiesFilterBottomSheet(
    titleResId: Int,
    items: List<T>,
    selected: Set<T>,
    titleRes: (T) -> Int,
    onToggle: (T) -> Unit,
    onClear: () -> Unit,
    onApply: () -> Unit,
    onDismiss: () -> Unit
) {

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { it != SheetValue.Hidden }
    )

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {},
        dragHandle = null,
        properties = ModalBottomSheetProperties(
            shouldDismissOnBackPress = false
        ),
        containerColor = MaterialTheme.colorScheme.surface
    ) {

        Column(Modifier.fillMaxWidth()) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(PaddingLarge),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = stringResource(titleResId),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = onDismiss) {
                    Icon(
                        Icons.Default.Close,
                        null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.weight(1f, false)
            ) {

                items(items) { item ->

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onToggle(item) }
                            .padding(
                                horizontal = PaddingLarge,
                                vertical = VerticalPaddingItemSmall
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = stringResource(titleRes(item)),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )

                        Checkbox(
                            checked = item in selected,
                            onCheckedChange = { onToggle(item) }
                        )
                    }

                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant,
                        thickness = DividerThickness
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(PaddingLarge),
                verticalAlignment = Alignment.CenterVertically
            ) {

                if (selected.isNotEmpty()) {

                    Text(
                        text = stringResource(R.string.clear_text),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.clickable { onClear() }
                    )
                }

                Spacer(Modifier.weight(1f))

                Text(
                    text = stringResource(R.string.apply_text),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { onApply() }
                )
            }
        }
    }
}