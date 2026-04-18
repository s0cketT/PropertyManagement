package com.example.propertymanagement.ui.list_property_screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.propertymanagement.domain.model.CurrencyRate
import com.example.propertymanagement.domain.model.Property
import com.example.propertymanagement.ui.theme.ButtonCornerRadius
import com.example.propertymanagement.ui.theme.PaddingLarge
import com.example.propertymanagement.ui.theme.PaddingMedium
import com.example.propertymanagement.ui.theme.PaddingSmall

@Composable
fun PropertyList(
    list: List<Property>,
    currencyRates: Map<String, CurrencyRate>,
    onFavoriteClick: (Int) -> Unit,
    onItemClick: (Property) -> Unit,
    bottomTrailing: (@Composable (Property) -> Unit)? = null
) {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = PaddingMedium)
    ) {
        items(list, key = { it.id }) { property ->
            PropertyListCard(
                property = property,
                currencyRates = currencyRates,
                onFavoriteClick = onFavoriteClick,
                onItemClick = onItemClick,
                bottomTrailing = bottomTrailing
            )
        }
    }
}

@Composable
fun PropertyListCard(
    property: Property,
    currencyRates: Map<String, CurrencyRate>,
    onFavoriteClick: (Int) -> Unit,
    onItemClick: (Property) -> Unit,
    bottomTrailing: (@Composable (Property) -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = PaddingLarge, vertical = PaddingSmall),
        shape = RoundedCornerShape(ButtonCornerRadius),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            PropertyImageSection(
                photos = property.photos,
                isFavorite = property.isFavorite,
                onFavoriteClick = { onFavoriteClick(property.id) }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onItemClick(property) }
            ) {
                PropertyContent(property = property, currencyRates = currencyRates)

                if (bottomTrailing != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = PaddingLarge,
                                end = PaddingLarge,
                                top = PaddingSmall,
                                bottom = PaddingMedium
                            )
                    ) {
                        Box(modifier = Modifier.align(Alignment.CenterEnd)) {
                            bottomTrailing(property)
                        }
                    }
                }
            }
        }
    }
}
