package com.example.propertymanagement.ui.map_screen.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.propertymanagement.domain.model.PropertyStatus
import com.example.propertymanagement.ui.map_screen.MapIntent
import com.example.propertymanagement.ui.theme.PaddingExtraLarge
import com.example.propertymanagement.ui.theme.Primary
import kotlinx.coroutines.launch
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.PropertyType
import com.example.propertymanagement.ui.theme.ButtonCornerRadius
import com.example.propertymanagement.ui.theme.ChipCornerRadius
import com.example.propertymanagement.ui.theme.FilterChipIconSelected
import com.example.propertymanagement.ui.theme.FilterChipIconUnselected
import com.example.propertymanagement.ui.theme.FilterChipSelected
import com.example.propertymanagement.ui.theme.FilterChipTextSelected
import com.example.propertymanagement.ui.theme.FilterChipTextUnselected
import com.example.propertymanagement.ui.theme.FilterChipUnselected
import com.example.propertymanagement.ui.theme.IconSmall
import com.example.propertymanagement.ui.theme.OnPrimary
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.PaddingMedium
import com.example.propertymanagement.ui.theme.PaddingSmall
import com.example.propertymanagement.ui.theme.SpacerExtraLarge
import com.example.propertymanagement.ui.theme.SpacerLarge
import com.example.propertymanagement.ui.theme.SpacerMedium
import com.example.propertymanagement.ui.theme.SurfaceVariant

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersMarks(
    selectedStatuses: Set<PropertyStatus> = emptySet(),
    selectedTypes: Set<PropertyType> = emptySet(),
    intent: (MapIntent) -> Unit = {}
) {
    var showFilterSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {

        FloatingActionButton(
            onClick = {
                showFilterSheet = true
                coroutineScope.launch { sheetState.show() }
            },
            modifier = Modifier
                .padding(PaddingExtraLarge)
                .align(Alignment.BottomEnd),
            containerColor = Primary,
            contentColor = OnPrimary
        ) {
            Icon(
                imageVector = Icons.Default.Build,
                contentDescription = stringResource(R.string.filters_fab_content_description)
            )
        }

        if (showFilterSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showFilterSheet = false
                    coroutineScope.launch { sheetState.hide() }
                },
                sheetState = sheetState,
                dragHandle = { BottomSheetDefaults.DragHandle() }
            ) {
                FilterBottomSheetContent(
                    selectedStatuses = selectedStatuses,
                    selectedTypes = selectedTypes,
                    onStatusSelected = { status ->
                        intent(MapIntent.ApplyStatusFilter(status))
                    },
                    onTypeSelected = { type ->
                        intent(MapIntent.ApplyTypeFilter(type))
                    },
                    onResetAll = {
                        intent(MapIntent.ApplyStatusFilter(null))
                        intent(MapIntent.ApplyTypeFilter(null))
                        // можно закрыть шит после сброса
                        // coroutineScope.launch { sheetState.hide() }
                    }
                )
            }
        }
    }
}

@Composable
private fun FilterBottomSheetContent(
    selectedStatuses: Set<PropertyStatus> = emptySet(),
    selectedTypes: Set<PropertyType> = emptySet(),
    onStatusSelected: (PropertyStatus?) -> Unit,
    onTypeSelected: (PropertyType?) -> Unit,
    onResetAll: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = PaddingExtraLarge,
                vertical = PaddingLarge
            )
            .navigationBarsPadding()
    ) {
        Text(
            text = stringResource(R.string.filters_title),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = SpacerLarge)
        )

        Text(
            text = stringResource(R.string.status_section_title),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = PaddingMedium)
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(PaddingSmall),
            verticalArrangement = Arrangement.spacedBy(PaddingSmall),
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf(
                PropertyStatus.FOR_SALE to R.string.sale_chip,
                PropertyStatus.FOR_RENT to R.string.rent_chip,
                // Добавляй сюда SOLD, RENTED и т.д. при необходимости
            ).forEach { (status, stringRes) ->
                FilterChipCommon(
                    text = stringResource(stringRes),
                    selected = status in selectedStatuses,
                    onClick = { onStatusSelected(status) }
                )
            }
        }

        Spacer(modifier = Modifier.height(SpacerLarge))

        Text(
            text = stringResource(R.string.property_type_section_title),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = PaddingMedium)
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(PaddingSmall),
            verticalArrangement = Arrangement.spacedBy(PaddingSmall),
            modifier = Modifier.fillMaxWidth()
        ) {
            PropertyType.entries.forEach { type ->
                FilterChipCommon(
                    text = stringResource(
                        when (type) {
                            PropertyType.APARTMENT -> R.string.type_apartment
                            PropertyType.HOUSE     -> R.string.type_house
                            PropertyType.COMMERCIAL    -> R.string.type_commercial
                            PropertyType.GARAGE    -> R.string.type_garage
                            PropertyType.ROOM -> R.string.type_room
                            PropertyType.LAND ->R.string.type_garage
                        }
                    ),
                    selected = type in selectedTypes,
                    onClick = { onTypeSelected(type) }
                )
            }
        }

        Spacer(modifier = Modifier.height(SpacerExtraLarge))

        if (selectedStatuses.isNotEmpty() || selectedTypes.isNotEmpty()) {
            Button(
                onClick = onResetAll,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary,
                    contentColor = OnPrimary
                ),
                shape = RoundedCornerShape(ButtonCornerRadius)
            ) {
                Text(
                    text = stringResource(R.string.reset_all_filters),
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Spacer(modifier = Modifier.height(SpacerMedium))
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterChipCommon(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        leadingIcon = if (selected) {
            {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(IconSmall)
                )
            }
        } else null,
        colors = FilterChipDefaults.filterChipColors(
            containerColor     = FilterChipUnselected,
            labelColor         = FilterChipTextUnselected,

            selectedContainerColor     = FilterChipSelected,
            selectedLabelColor         = FilterChipTextSelected,
            selectedLeadingIconColor   = FilterChipIconSelected,

            disabledContainerColor     = FilterChipUnselected.copy(alpha = 0.38f),
            disabledLabelColor         = FilterChipTextUnselected.copy(alpha = 0.38f),
            disabledLeadingIconColor   = FilterChipIconUnselected.copy(alpha = 0.38f),

            disabledSelectedContainerColor = FilterChipSelected.copy(alpha = 0.38f)
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selected,
            borderWidth = 1.dp,
            borderColor = if (selected) Primary else SurfaceVariant
        ),
        shape = RoundedCornerShape(ChipCornerRadius)
    )
}

