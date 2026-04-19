package com.example.propertymanagement.ui.list_property_screen.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.ModerationStatus
import com.example.propertymanagement.ui.theme.ChipCornerRadius
import com.example.propertymanagement.ui.theme.DpZero
import com.example.propertymanagement.ui.theme.ModerationBadgePaddingHorizontal
import com.example.propertymanagement.ui.theme.PaddingSmall

@Composable
fun ModerationStatusBadge(
    status: ModerationStatus,
    modifier: Modifier = Modifier
) {
    val scheme = MaterialTheme.colorScheme
    val (labelRes, container, content) = when (status) {
        ModerationStatus.APPROVED -> Triple(
            R.string.moderation_chip_approved,
            scheme.primaryContainer,
            scheme.onPrimaryContainer
        )

        ModerationStatus.REJECTED -> Triple(
            R.string.moderation_chip_rejected,
            scheme.errorContainer,
            scheme.onErrorContainer
        )

        ModerationStatus.PENDING,
        ModerationStatus.UNKNOWN -> Triple(
            R.string.moderation_chip_pending,
            scheme.secondaryContainer,
            scheme.onSecondaryContainer
        )
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(ChipCornerRadius),
        color = container,
        shadowElevation = DpZero
    ) {
        Text(
            text = stringResource(labelRes),
            style = MaterialTheme.typography.labelMedium,
            color = content,
            modifier = Modifier.padding(
                horizontal = ModerationBadgePaddingHorizontal,
                vertical = PaddingSmall
            )
        )
    }
}
