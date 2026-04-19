package com.example.propertymanagement.ui.publish_screen.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.CurrencyType
import com.example.propertymanagement.ui.filters_screen.components.CurrencySelector
import com.example.propertymanagement.ui.filters_screen.components.NumberOutlinedTextField
import com.example.propertymanagement.ui.theme.ButtonCornerRadius
import com.example.propertymanagement.ui.theme.DpZero
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.PaddingSmall
import com.example.propertymanagement.ui.theme.SpacerSmall
import com.example.propertymanagement.ui.theme.TextFieldBorderError
import com.example.propertymanagement.ui.theme.TextFieldBorderWidth

@Composable
fun PriceSection(
    title: String,
    price: String,
    currency: CurrencyType,
    area: Int?,
    isError: Boolean,
    onPriceChange: (String) -> Unit,
    onCurrencySelected: (CurrencyType) -> Unit
) {

    Column(
        modifier = Modifier.padding(horizontal = PaddingLarge)
    ) {

        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(PaddingSmall))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .weight(1f)
                    .border(
                        width = if (isError) TextFieldBorderWidth else DpZero,
                        color = if (isError) TextFieldBorderError else Color.Transparent,
                        shape = RoundedCornerShape(ButtonCornerRadius)
                    )
            ) {
                NumberOutlinedTextField(
                    value = price,
                    onValueChange = onPriceChange,
                    label = title,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.width(PaddingSmall))

            CurrencySelector(
                selectedCurrency = currency,
                onCurrencySelected = onCurrencySelected,
                modifier = Modifier.weight(0.3f)
            )
        }

        Spacer(modifier = Modifier.height(SpacerSmall))

        val pricePerMeter = remember(price, area) {
            val p = price.toDoubleOrNull()
            val a = area?.toDouble()
            if (p != null && a != null && a != 0.0) (p / a).toInt() else null
        }

        pricePerMeter?.let {
            Text(
                text = "${stringResource(R.string.price_per_meter)}: $it",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}