package com.example.propertymanagement.ui.filters_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.DealType
import com.example.propertymanagement.domain.model.SellerType
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.PaddingMedium
import com.example.propertymanagement.ui.theme.SpacerMedium
import com.example.propertymanagement.ui.theme.SpacerSmall

@Composable
fun SellerTypeSection(
    selectedType: SellerType?,
    dealType: DealType?,
    onTypeSelected: (SellerType?) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = PaddingLarge)
    ) {

        val titleRes = when (dealType) {
            DealType.RENT -> R.string.filter_seller_title_landlord
            DealType.BUY -> R.string.filter_seller_title
            else -> R.string.filter_seller_title
        }

        Text(
            text = stringResource(titleRes),
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(SpacerSmall))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(PaddingMedium)
        ) {

            SellerType.entries.forEach { type ->

                val textRes = when (type) {
                    SellerType.OWNER -> R.string.filter_seller_owner

                    SellerType.AGENT_BUILDER -> {
                        if (dealType == DealType.RENT)
                            R.string.filter_seller_agency
                        else
                            R.string.filter_seller_agent
                    }
                }

                SellerFilterChip(
                    text = stringResource(textRes),
                    selected = selectedType == type,
                    onClick = { onTypeSelected(type.takeIf { selectedType != it }) }
                )
            }
        }

        Spacer(modifier = Modifier.height(SpacerMedium))
    }
}