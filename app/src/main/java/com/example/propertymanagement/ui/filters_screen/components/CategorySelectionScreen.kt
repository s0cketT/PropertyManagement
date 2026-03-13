package com.example.propertymanagement.ui.filters_screen.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.PropertyType
import com.example.propertymanagement.ui.theme.DividerColor
import com.example.propertymanagement.ui.theme.HorizontalPadding
import com.example.propertymanagement.ui.theme.SmallHorizontalPadding
import com.example.propertymanagement.ui.theme.SpacerBetweenElements
import com.example.propertymanagement.ui.theme.SpacerTiny
import com.example.propertymanagement.ui.theme.TopBarBackground
import com.example.propertymanagement.ui.theme.TopBarHeight
import com.example.propertymanagement.ui.theme.VerticalPaddingItem

@Composable
fun CategorySelectionScreen(navController: NavController) {

    BackHandler {
        navController.popBackStack()
    }

    UI(navController = navController)
}

@Composable
private fun UI(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        CustomCategoryTopBar(
            onBackClick = { navController.popBackStack() }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(PropertyType.entries.toList()) { type ->
                CategoryItem(
                    type = type,
                    onClick = {
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("selected_property_type", type.name)

                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

@Composable
private fun CustomCategoryTopBar(
    onBackClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .background(TopBarBackground)
                .fillMaxWidth()
                .height(TopBarHeight)
                .padding(horizontal = SmallHorizontalPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.width(SpacerBetweenElements))

            Text(
                text = stringResource(R.string.real_estate),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun CategoryItem(
    type: PropertyType,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = HorizontalPadding, vertical = VerticalPaddingItem),

            ) {
            Text(
                text = stringResource(
                    when (type) {
                        PropertyType.APARTMENT -> R.string.category_apartments
                        PropertyType.HOUSE -> R.string.category_houses
                        PropertyType.LAND -> R.string.category_land
                        PropertyType.COMMERCIAL -> R.string.category_commercial
                        PropertyType.GARAGE -> R.string.category_garages
                        PropertyType.ROOM -> R.string.category_rooms
                    }
                ),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(SpacerTiny))

            Text(
                text = stringResource(
                    when (type) {
                        PropertyType.APARTMENT -> R.string.description_apartments
                        PropertyType.HOUSE -> R.string.description_houses
                        PropertyType.LAND -> R.string.description_land
                        PropertyType.COMMERCIAL -> R.string.description_commercial
                        PropertyType.GARAGE -> R.string.description_garages
                        PropertyType.ROOM -> R.string.description_rooms
                    }
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth(),
            color = DividerColor
        )
    }
}