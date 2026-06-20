package com.example.propertymanagement.ui.my_applications_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.ApplicationStatus
import com.example.propertymanagement.domain.model.PropertyApplication
import com.example.propertymanagement.ui.common.formatPropertyPublicationTime
import com.example.propertymanagement.ui.theme.ApplicationStatusColors
import com.example.propertymanagement.ui.theme.LocalAppDarkTheme
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.PaddingSmall
import com.example.propertymanagement.ui.theme.ProfileSectionCardElevation
import com.example.propertymanagement.ui.theme.SpacerSmall
import android.content.Intent
import android.net.Uri
import java.util.Locale

private val ApplicationCardAccentWidth = 5.dp
private val ApplicationCardShape = RoundedCornerShape(16.dp)

@Composable
fun MyApplicationCard(
    application: PropertyApplication,
    locale: Locale,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val palette = ApplicationStatusColors.palette(
        status = application.status,
        isDarkTheme = LocalAppDarkTheme.current,
    )
    val canCallManager = application.status == ApplicationStatus.IN_PROGRESS
    val shouldMaskManagerEmail = application.status == ApplicationStatus.PROCESSING ||
        application.status == ApplicationStatus.REJECTED

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = ApplicationCardShape,
        colors = CardDefaults.cardColors(
            containerColor = palette.cardContainer,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = ProfileSectionCardElevation),
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .width(ApplicationCardAccentWidth)
                    .fillMaxHeight()
                    .clip(
                        RoundedCornerShape(
                            topStart = 16.dp,
                            bottomStart = 16.dp,
                        ),
                    )
                    .background(palette.accent),
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = PaddingLarge + ApplicationCardAccentWidth,
                        end = PaddingLarge,
                        top = PaddingLarge,
                        bottom = PaddingLarge,
                    ),
            ) {
                Text(
                    text = application.propertyTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Spacer(modifier = Modifier.height(PaddingSmall))

                ApplicationStatusBadge(status = application.status)

                formatPropertyPublicationTime(application.createdAt, locale)?.let { createdAtLabel ->
                    Spacer(modifier = Modifier.height(SpacerSmall))
                    Text(
                        text = stringResource(R.string.my_applications_created_at, createdAtLabel),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                Spacer(modifier = Modifier.height(PaddingSmall))

                Text(
                    text = stringResource(R.string.my_applications_manager_section),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Spacer(modifier = Modifier.height(PaddingSmall))

                if (application.hasManagerContact) {
                    application.managerName?.let { name ->
                        ManagerContactRow(
                            label = stringResource(R.string.my_applications_manager_name),
                            value = name,
                        )
                    }
                    application.managerEmail?.let { email ->
                        val displayEmail = if (shouldMaskManagerEmail) {
                            maskEmail(email)
                        } else {
                            email
                        }
                        ManagerEmailRow(
                            label = stringResource(R.string.my_applications_manager_email),
                            value = displayEmail,
                            isClickable = canCallManager && !shouldMaskManagerEmail,
                            onClick = {
                                val normalized = email.trim()
                                if (normalized.isEmpty()) {
                                    return@ManagerEmailRow
                                }
                                val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                                    data = Uri.parse("mailto:$normalized")
                                }
                                val chooser = Intent.createChooser(emailIntent, null)
                                context.startActivity(chooser)
                            },
                        )
                    }
                    application.managerPhone?.let { phone ->
                        val displayPhone = if (canCallManager) {
                            phone
                        } else {
                            maskPhone(phone)
                        }

                        ManagerPhoneRow(
                            label = stringResource(R.string.my_applications_manager_phone),
                            value = displayPhone,
                            isClickable = canCallManager,
                            onClick = {
                                val normalized = normalizeDialPhone(phone)
                                if (normalized.isEmpty()) {
                                    return@ManagerPhoneRow
                                }
                                val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                                    data = Uri.parse("tel:$normalized")
                                }
                                val chooser = Intent.createChooser(dialIntent, null)
                                context.startActivity(chooser)
                            },
                        )
                    }
                } else {
                    Text(
                        text = stringResource(R.string.my_applications_manager_not_assigned),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Composable
private fun ManagerContactRow(
    label: String,
    value: String,
) {
    Text(
        text = stringResource(R.string.my_applications_manager_contact_line, label, value),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(bottom = PaddingSmall),
    )
}

@Composable
private fun ManagerEmailRow(
    label: String,
    value: String,
    isClickable: Boolean,
    onClick: () -> Unit,
) {
    Row(modifier = Modifier.padding(bottom = PaddingSmall)) {
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isClickable) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface
            },
            textDecoration = if (isClickable) TextDecoration.Underline else TextDecoration.None,
            modifier = if (isClickable) {
                Modifier.clickable(onClick = onClick)
            } else {
                Modifier
            },
        )
    }
}

@Composable
private fun ManagerPhoneRow(
    label: String,
    value: String,
    isClickable: Boolean,
    onClick: () -> Unit,
) {
    Row(modifier = Modifier.padding(bottom = PaddingSmall)) {
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isClickable) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface
            },
            textDecoration = if (isClickable) TextDecoration.Underline else TextDecoration.None,
            modifier = if (isClickable) {
                Modifier.clickable(onClick = onClick)
            } else {
                Modifier
            },
        )
    }
}

private fun maskPhone(phone: String): String {
    val digits = phone.filter { it.isDigit() }
    if (digits.isEmpty()) {
        return "***"
    }
    val tail = digits.takeLast(4)
    return "***$tail"
}

private fun maskEmail(email: String): String {
    val normalized = email.trim()
    if (normalized.isEmpty()) {
        return "***"
    }

    val atIndex = normalized.indexOf('@')
    if (atIndex <= 0 || atIndex == normalized.lastIndex) {
        return "***"
    }

    val domain = normalized.substring(atIndex + 1)
    if (domain.isBlank()) {
        return "***"
    }

    return "***@$domain"
}

private fun normalizeDialPhone(phone: String): String {
    return buildString {
        phone.forEachIndexed { index, char ->
            if (char.isDigit()) {
                append(char)
            } else if (char == '+' && index == 0) {
                append(char)
            }
        }
    }
}
