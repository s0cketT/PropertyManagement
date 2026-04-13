package com.example.propertymanagement.ui.publish_screen.components

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
import com.example.propertymanagement.ui.components.WheelPicker
import com.example.propertymanagement.ui.filters_screen.components.NumberOutlinedTextField
import com.example.propertymanagement.ui.theme.DividerThickness
import com.example.propertymanagement.ui.theme.IconSizeArrow
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.SpacerMedium
import com.example.propertymanagement.ui.theme.VerticalPaddingItem
import com.example.propertymanagement.ui.theme.VerticalPaddingItemSmall

@Composable
fun SingleValueFilterItem(
    titleResId: Int,
    value: Int?,
    onApply: (Int?) -> Unit,
    values: List<Int>? = null,
    displayMapper: ((Int) -> String)? = null
) {

    var isSheetOpen by remember { mutableStateOf(false) }

    val displayText = value?.let {
        displayMapper?.invoke(it) ?: it.toString()
    } ?: stringResource(id = titleResId)

    Column {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isSheetOpen = true }
                .padding(
                    vertical = if (value != null) VerticalPaddingItemSmall else VerticalPaddingItem,
                    horizontal = PaddingLarge
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(modifier = Modifier.weight(1f)) {

                if (value != null) {
                    Text(
                        text = stringResource(id = titleResId),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Text(
                    text = displayText,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (value != null)
                        MaterialTheme.colorScheme.onSurface
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (value != null) {
                IconButton(onClick = { onApply(null) }) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(IconSizeArrow)
                )
            }
        }

        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant,
            thickness = DividerThickness,
            modifier = Modifier.padding(horizontal = PaddingLarge)
        )
    }

    if (isSheetOpen) {
        SingleValueBottomSheet(
            titleResId = titleResId,
            value = value,
            values = values,
            displayMapper = displayMapper,
            onClose = { isSheetOpen = false },
            onApply = onApply
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SingleValueBottomSheet(
    titleResId: Int,
    value: Int?,
    values: List<Int>? = null,
    displayMapper: ((Int) -> String)? = null,
    onClose: () -> Unit,
    onApply: (Int?) -> Unit
) {

    var textValue by remember { mutableStateOf(value?.toString().orEmpty()) }
    var wheelValue by remember { mutableStateOf(value ?: values?.firstOrNull()) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onClose,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {

        Column {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(PaddingLarge),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = stringResource(titleResId),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                IconButton(onClick = onClose) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (values != null) {
                WheelPicker(
                    values = values,
                    initialValue = wheelValue ?: values.first(),
                    displayMapper = displayMapper ?: { it.toString() },
                    onValueSelected = { wheelValue = it },
                    modifier = Modifier.padding(horizontal = PaddingLarge)
                )
            } else {
                NumberOutlinedTextField(
                    value = textValue,
                    onValueChange = { textValue = it },
                    label = stringResource(titleResId),
                    modifier = Modifier.padding(horizontal = PaddingLarge)
                )
            }

            Spacer(modifier = Modifier.height(SpacerMedium))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(PaddingLarge),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = stringResource(R.string.clear_text),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.clickable {
                        textValue = ""
                        wheelValue = null
                    }
                )

                Text(
                    text = stringResource(R.string.apply_text),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {

                        val result = if (values != null) {
                            wheelValue
                        } else {
                            textValue.toIntOrNull()
                        }

                        onApply(result)
                        onClose()
                    }
                )
            }
        }
    }
}