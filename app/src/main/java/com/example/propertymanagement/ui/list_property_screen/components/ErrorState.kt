package com.example.propertymanagement.ui.list_property_screen.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.R

@Composable
fun ErrorState(error: String?) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = error ?: stringResource(R.string.error_default),
            modifier = Modifier.align(Alignment.Center),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}