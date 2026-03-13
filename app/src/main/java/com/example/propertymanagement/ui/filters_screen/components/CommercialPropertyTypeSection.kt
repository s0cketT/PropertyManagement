package com.example.propertymanagement.ui.filters_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.domain.model.CommercialPropertyType
import com.example.propertymanagement.R
import com.example.propertymanagement.ui.extensions.title
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.PaddingMedium

@Composable
fun CommercialPropertyTypeSection(
    selectedType: CommercialPropertyType?,
    onTypeSelected: (CommercialPropertyType?) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = PaddingLarge)
    ) {

        Text(
            text = stringResource(R.string.filter_commercial_type_title),
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(PaddingMedium))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(PaddingMedium),
            verticalArrangement = Arrangement.spacedBy(PaddingMedium)
        ) {

            CommercialPropertyType.entries.forEach { type ->

                SellerFilterChip(
                    text = type.title(),
                    selected = selectedType == type,
                    onClick = {

                        if (selectedType == type) {
                            onTypeSelected(null)
                        } else {
                            onTypeSelected(type)
                        }

                    }
                )
            }
        }
    }
}