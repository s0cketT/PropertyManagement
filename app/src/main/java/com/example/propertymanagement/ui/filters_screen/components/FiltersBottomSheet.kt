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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersBottomSheet(
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
        onDismissRequest = {}
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

            Divider(
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