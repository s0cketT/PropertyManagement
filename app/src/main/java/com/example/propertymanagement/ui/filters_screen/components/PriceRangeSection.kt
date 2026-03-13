package com.example.propertymanagement.ui.filters_screen.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.CurrencyType
import com.example.propertymanagement.ui.extensions.symbol
import com.example.propertymanagement.ui.theme.ButtonCornerRadius
import com.example.propertymanagement.ui.theme.CustomTextSelectionColors
import com.example.propertymanagement.ui.theme.HeightOutlinedTextField
import com.example.propertymanagement.ui.theme.IconSmall
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.PaddingMedium
import com.example.propertymanagement.ui.theme.PaddingSmall
import com.example.propertymanagement.ui.theme.SpacerTiny
import com.example.propertymanagement.ui.theme.TextFieldBorderFocused
import com.example.propertymanagement.ui.theme.TextFieldBorderInactive
import com.example.propertymanagement.ui.theme.TextFieldBorderWidth
import com.example.propertymanagement.ui.theme.TextFieldCursor
import com.example.propertymanagement.ui.theme.TextFieldHorizontalPadding
import com.example.propertymanagement.ui.theme.TextFieldLabelColor
import com.example.propertymanagement.ui.theme.TextFieldPrefixSpacing
import com.example.propertymanagement.ui.theme.TextFieldTextColor
import com.example.propertymanagement.ui.theme.TextFieldVerticalPadding

@Composable
fun PriceRangeSection(
    title: String,
    priceFrom: String,
    priceTo: String,
    selectedCurrency: CurrencyType,
    onPriceFromChange: (String) -> Unit,
    onPriceToChange: (String) -> Unit,
    onCurrencySelected: (CurrencyType) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = PaddingLarge)
    ) {

        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(PaddingMedium))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(PaddingMedium)
        ) {

            NumberOutlinedTextField(
                value = priceFrom,
                onValueChange = onPriceFromChange,
                label = stringResource(R.string.from),
                modifier = Modifier.weight(1f)
            )

            NumberOutlinedTextField(
                value = priceTo,
                onValueChange = onPriceToChange,
                label = stringResource(R.string.to),
                modifier = Modifier.weight(1f)
            )

            CurrencySelector(
                selectedCurrency = selectedCurrency,
                onCurrencySelected = onCurrencySelected,
                modifier = Modifier.weight(0.5f)
            )
        }
    }
}

@Composable
fun NumberOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {

    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val borderColor = if (isFocused) {
        TextFieldBorderFocused
    } else {
        TextFieldBorderInactive
    }

    CompositionLocalProvider(LocalTextSelectionColors provides CustomTextSelectionColors) {

        BasicTextField(
            value = value,
            onValueChange = { newValue ->
                if (newValue.all { it.isDigit() }) {
                    onValueChange(newValue)
                }
            },
            singleLine = true,
            interactionSource = interactionSource,

            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = TextFieldTextColor
            ),

            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),

            cursorBrush = SolidColor(TextFieldCursor),

            modifier = modifier
                .fillMaxWidth()
                .height(HeightOutlinedTextField)
                .border(
                    width = TextFieldBorderWidth,
                    color = borderColor,
                    shape = RoundedCornerShape(ButtonCornerRadius)
                )
                .clip(RoundedCornerShape(ButtonCornerRadius))
                .padding(
                    horizontal = TextFieldHorizontalPadding,
                    vertical = TextFieldVerticalPadding
                ),

            decorationBox = { innerTextField ->

                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (isFocused) TextFieldTextColor else TextFieldLabelColor
                    )

                    Spacer(modifier = Modifier.width(TextFieldPrefixSpacing))

                    innerTextField()
                }
            }
        )
    }
}

@Composable
fun CurrencySelector(
    selectedCurrency: CurrencyType,
    onCurrencySelected: (CurrencyType) -> Unit,
    modifier: Modifier = Modifier
) {

    var expanded by remember { mutableStateOf(false) }

    Box(modifier.height(HeightOutlinedTextField)) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(ButtonCornerRadius))
                .border(
                    width = TextFieldBorderWidth,
                    color = TextFieldBorderInactive,
                    shape = RoundedCornerShape(ButtonCornerRadius)
                )
                .clickable { expanded = true }
                .padding(horizontal = PaddingMedium, vertical = PaddingSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = selectedCurrency.symbol(),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.width(SpacerTiny))

            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                modifier = Modifier.size(IconSmall)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {

            CurrencyType.values().forEach { currency ->

                DropdownMenuItem(
                    text = {
                        Text(
                            text = currency.name,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    onClick = {
                        expanded = false
                        onCurrencySelected(currency)
                    }
                )
            }
        }
    }
}