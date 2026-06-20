package com.example.propertymanagement.ui.auth_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import com.example.propertymanagement.R
import com.example.propertymanagement.ui.auth_screen.AuthError
import com.example.propertymanagement.ui.mapper.asString
import com.example.propertymanagement.ui.theme.PaddingSmall

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyConsentRow(
    isAccepted: Boolean,
    error: AuthError?,
    onAcceptedChange: (Boolean) -> Unit,
    onOpenPolicy: () -> Unit,
) {
    val prefix = stringResource(R.string.privacy_policy_consent_prefix)
    val link = stringResource(R.string.privacy_policy_link)
    val consentText = buildAnnotatedString {
        append(prefix.trimEnd())
        append(' ')
        withLink(
            LinkAnnotation.Clickable(
                tag = PRIVACY_POLICY_LINK_TAG,
                linkInteractionListener = { onOpenPolicy() },
            ),
        ) {
            withStyle(
                SpanStyle(
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline,
                ),
            ) {
                append(link)
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                Checkbox(
                    checked = isAccepted,
                    onCheckedChange = onAcceptedChange,
                    colors = CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary,
                        uncheckedColor = MaterialTheme.colorScheme.outline,
                        checkmarkColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                )
            }

            Text(
                text = consentText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f),
            )
        }

        error?.let {
            Text(
                text = it.asString(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = PaddingSmall),
            )
        }
    }
}

private const val PRIVACY_POLICY_LINK_TAG = "privacy_policy"
