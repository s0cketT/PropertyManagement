package com.example.propertymanagement.ui.profile_screen.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.propertymanagement.R
import com.example.propertymanagement.ui.theme.BottomSheetDragHandlePaddingVertical
import com.example.propertymanagement.ui.theme.BottomSheetDragHandleWidth
import com.example.propertymanagement.ui.theme.BottomSheetTopCornerRadius
import com.example.propertymanagement.ui.theme.ButtonCornerRadius
import com.example.propertymanagement.ui.theme.CornerRadiusExtraSmall
import com.example.propertymanagement.ui.theme.IconSizeActionSquare
import com.example.propertymanagement.ui.theme.IconSizeArrowLarge
import com.example.propertymanagement.ui.theme.IconSizeStarRow
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.PropertyDetailMapCornerRadius
import com.example.propertymanagement.ui.theme.RateAppEmojiContainerSize
import com.example.propertymanagement.ui.theme.RateAppSubmitButtonSize
import com.example.propertymanagement.ui.theme.SpacerHeightTight
import com.example.propertymanagement.ui.theme.SpacerMedium
import com.example.propertymanagement.ui.theme.SpacerSmall
import com.example.propertymanagement.ui.theme.SpacerTiny
import com.example.propertymanagement.ui.theme.StarStrokeWidth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RateAppBottomSheet(
    visible: Boolean,
    isSubmitting: Boolean,
    onDismiss: () -> Unit,
    onSubmitRating: (Int) -> Unit
) {
    if (!visible) return

    var selectedStars by remember { mutableIntStateOf(0) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val haptic = LocalHapticFeedback.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(
            topStart = BottomSheetTopCornerRadius,
            topEnd = BottomSheetTopCornerRadius
        ),
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = BottomSheetDragHandlePaddingVertical)
                    .size(width = BottomSheetDragHandleWidth, height = SpacerTiny)
                    .background(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.28f),
                        shape = RoundedCornerShape(CornerRadiusExtraSmall)
                    )
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = PaddingLarge)
                .padding(bottom = PaddingLarge)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
                                MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.35f)
                            )
                        ),
                        shape = RoundedCornerShape(PropertyDetailMapCornerRadius)
                    )
                    .padding(PaddingLarge)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(RateAppEmojiContainerSize)
                            .background(
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.18f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            modifier = Modifier.size(IconSizeArrowLarge),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(SpacerSmall))
                    Text(
                        text = stringResource(R.string.rate_app_sheet_title),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(SpacerHeightTight))
                    Text(
                        text = stringResource(R.string.rate_app_sheet_subtitle),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(SpacerMedium))

            Text(
                text = stringResource(R.string.rate_app_stars_hint),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(SpacerSmall))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (index in 1..5) {
                    StarRatingButton(
                        index = index,
                        selected = selectedStars,
                        enabled = !isSubmitting,
                        onSelect = { stars ->
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            selectedStars = stars
                        }
                    )
                }
            }

            if (selectedStars > 0) {
                Spacer(modifier = Modifier.height(SpacerSmall))
                Text(
                    text = stringResource(R.string.rate_app_stars_picked, selectedStars),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(SpacerMedium))

            Button(
                onClick = { onSubmitRating(selectedStars) },
                enabled = selectedStars > 0 && !isSubmitting,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(ButtonCornerRadius),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f)
                )
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(IconSizeStarRow),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = StarStrokeWidth
                    )
                } else {
                    Text(stringResource(R.string.rate_app_submit))
                }
            }

            TextButton(
                onClick = onDismiss,
                enabled = !isSubmitting,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.rate_app_maybe_later))
            }
        }
    }
}

@Composable
private fun StarRatingButton(
    index: Int,
    selected: Int,
    enabled: Boolean,
    onSelect: (Int) -> Unit
) {
    val filled = index <= selected
    val scale by animateFloatAsState(
        targetValue = if (filled) 1.08f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "starScale"
    )
    val cd = stringResource(R.string.rate_app_star_cd, index)
    IconButton(
        onClick = { onSelect(index) },
        enabled = enabled,
        modifier = Modifier
            .size(RateAppSubmitButtonSize)
            .semantics {
                contentDescription = cd
                role = Role.Button
            }
    ) {
        Icon(
            imageVector = if (filled) Icons.Filled.Star else Icons.Outlined.StarOutline,
            contentDescription = null,
            modifier = Modifier
                .size(IconSizeActionSquare)
                .scale(scale),
            tint = if (filled) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outline.copy(alpha = 0.45f)
            }
        )
    }
}
