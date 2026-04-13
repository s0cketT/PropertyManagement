package com.example.propertymanagement.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.R
import com.example.propertymanagement.ui.theme.PaddingMedium
import com.example.propertymanagement.ui.theme.PaddingSmall

@Composable
fun AppTopBar(
    title: Int,
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = PaddingSmall)
    ) {

        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = PaddingMedium)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.cd_back),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        Text(
            text = stringResource(title),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}