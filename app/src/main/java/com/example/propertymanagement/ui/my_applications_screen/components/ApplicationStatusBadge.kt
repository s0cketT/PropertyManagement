package com.example.propertymanagement.ui.my_applications_screen.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.ApplicationStatus
import com.example.propertymanagement.ui.theme.ApplicationStatusColors
import com.example.propertymanagement.ui.theme.ChipCornerRadius
import com.example.propertymanagement.ui.theme.DpZero
import com.example.propertymanagement.ui.theme.LocalAppDarkTheme
import com.example.propertymanagement.ui.theme.ModerationBadgePaddingHorizontal
import com.example.propertymanagement.ui.theme.PaddingSmall

@Composable
fun ApplicationStatusBadge(
    status: ApplicationStatus,
    modifier: Modifier = Modifier,
) {
    val palette = ApplicationStatusColors.palette(
        status = status,
        isDarkTheme = LocalAppDarkTheme.current,
    )
    val labelRes = when (status) {
        ApplicationStatus.PROCESSING -> R.string.application_status_processing
        ApplicationStatus.IN_PROGRESS -> R.string.application_status_in_progress
        ApplicationStatus.REJECTED -> R.string.application_status_rejected
        ApplicationStatus.COMPLETED -> R.string.application_status_completed
        ApplicationStatus.UNKNOWN -> R.string.application_status_unknown
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(ChipCornerRadius),
        color = palette.badgeContainer,
        shadowElevation = DpZero,
    ) {
        Text(
            text = stringResource(labelRes),
            style = MaterialTheme.typography.labelMedium,
            color = palette.badgeContent,
            modifier = Modifier.padding(
                horizontal = ModerationBadgePaddingHorizontal,
                vertical = PaddingSmall,
            ),
        )
    }
}
