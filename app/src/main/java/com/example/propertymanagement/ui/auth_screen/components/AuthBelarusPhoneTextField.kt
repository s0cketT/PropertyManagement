package com.example.propertymanagement.ui.auth_screen.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import com.example.propertymanagement.R
import com.example.propertymanagement.ui.auth_screen.AuthError
import com.example.propertymanagement.ui.mapper.asString
import com.example.propertymanagement.ui.theme.ButtonCornerRadius
import com.example.propertymanagement.ui.theme.HeightOutlinedTextField
import com.example.propertymanagement.ui.theme.TextFieldBorderWidth
import com.example.propertymanagement.ui.theme.TextFieldHorizontalPadding
import com.example.propertymanagement.ui.theme.TextFieldVerticalPadding

@Composable
fun AuthBelarusPhoneTextField(
    nationalDigits: String,
    error: AuthError?,
    onNationalDigitsChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val nationalFiltered = nationalDigits.filter { it.isDigit() }.take(9)

    var fieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = formatBelarusPhoneDisplay(""),
                selection = TextRange(belarusPhoneCaretPositionAfterDigitCount(0)),
            ),
        )
    }

    LaunchedEffect(nationalFiltered) {
        val expectedText = formatBelarusPhoneDisplay(nationalFiltered)
        if (fieldValue.text != expectedText) {
            val pos = belarusPhoneCaretPositionAfterDigitCount(nationalFiltered.length)
            fieldValue = TextFieldValue(expectedText, TextRange(pos))
        }
    }

    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val focusManager = LocalFocusManager.current

    val borderColor = when {
        error != null -> MaterialTheme.colorScheme.error
        isFocused -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outline
    }

    BackHandler(enabled = isFocused) {
        focusManager.clearFocus()
    }

    Column(modifier = modifier.fillMaxWidth()) {
        CompositionLocalProvider(
            LocalTextSelectionColors provides TextSelectionColors(
                backgroundColor = MaterialTheme.colorScheme.primary,
                handleColor = MaterialTheme.colorScheme.primary,
            ),
        ) {
            BasicTextField(
                value = fieldValue,
                onValueChange = { incoming ->
                    val parsed = normalizeNationalPhoneInput(incoming.text).take(9)
                    val previousNational = nationalDigits.filter { it.isDigit() }.take(9)
                    if (parsed != previousNational) {
                        onNationalDigitsChange(parsed)
                    }
                    val display = formatBelarusPhoneDisplay(parsed)
                    val pos = belarusPhoneCaretPositionAfterDigitCount(parsed.length)
                    fieldValue = TextFieldValue(display, TextRange(pos))
                },
                singleLine = true,
                interactionSource = interactionSource,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(HeightOutlinedTextField)
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            val d = normalizeNationalPhoneInput(fieldValue.text).take(9)
                            val display = formatBelarusPhoneDisplay(d)
                            val pos = belarusPhoneCaretPositionAfterDigitCount(d.length)
                            fieldValue = TextFieldValue(display, TextRange(pos))
                        }
                    }
                    .border(
                        width = TextFieldBorderWidth,
                        color = borderColor,
                        shape = RoundedCornerShape(ButtonCornerRadius),
                    )
                    .clip(RoundedCornerShape(ButtonCornerRadius))
                    .padding(
                        horizontal = TextFieldHorizontalPadding,
                        vertical = TextFieldVerticalPadding,
                    ),
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.CenterStart,
                        ) {
                            if (nationalFiltered.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.phone_placeholder),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                            innerTextField()
                        }
                    }
                },
            )
        }

        error?.let {
            Text(
                text = it.asString(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}
