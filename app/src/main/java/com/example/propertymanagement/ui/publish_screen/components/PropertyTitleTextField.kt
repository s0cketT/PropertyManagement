package com.example.propertymanagement.ui.publish_screen.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import com.example.propertymanagement.ui.theme.ButtonCornerRadius
import com.example.propertymanagement.ui.theme.HeightOutlinedTextField
import com.example.propertymanagement.ui.theme.IconPassword
import com.example.propertymanagement.ui.theme.TextFieldBorderWidth
import com.example.propertymanagement.ui.theme.TextFieldHorizontalPadding
import com.example.propertymanagement.ui.theme.TextFieldVerticalPadding

@Composable
fun PropertyTitleTextField(
    value: String,
    placeholder: String,
    isError: Boolean = false,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
) {

    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val focusManager = LocalFocusManager.current

    var passwordVisible by remember { mutableStateOf(false) }

    val borderColor = when {
        isError -> MaterialTheme.colorScheme.error
        isFocused -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outline
    }

    val visualTransformation =
        if (isPassword && !passwordVisible) PasswordVisualTransformation()
        else VisualTransformation.None

    BackHandler(enabled = isFocused) {
        focusManager.clearFocus()
    }

    CompositionLocalProvider(
        LocalTextSelectionColors provides TextSelectionColors(
            backgroundColor = MaterialTheme.colorScheme.primary,
            handleColor = MaterialTheme.colorScheme.primary
        )
    ) {

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            interactionSource = interactionSource,
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Text
            ),
            visualTransformation = visualTransformation,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),

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

                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {

                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        innerTextField()
                    }

                    if (isPassword) {
                        IconButton(
                            onClick = { passwordVisible = !passwordVisible },
                            modifier = Modifier.size(IconPassword)
                        ) {
                            Icon(
                                imageVector = if (passwordVisible)
                                    Icons.Default.Visibility
                                else
                                    Icons.Default.VisibilityOff,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        )
    }
}