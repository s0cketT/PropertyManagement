package com.example.propertymanagement.ui.bottom_nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.example.propertymanagement.R
sealed class Screens(
    val route: String,
    val titleResId: Int? = null,
    val icon: ImageVector? = null
) {
    @Composable
    fun title(): String = stringResource(titleResId!!)

    object Advertisements : Screens(
        route = "advertisements",
        titleResId = R.string.screen_advertisements,
        icon = Icons.Default.Home
    )

    object Favorites : Screens(
        route = "favorites",
        titleResId = R.string.screen_favorites,
        icon = Icons.Default.Favorite
    )

    object Map : Screens(
        route = "map",
        titleResId = R.string.screen_map,
        icon = Icons.Default.Place
    )

    object Publish : Screens(
        route = "publish",
        titleResId = R.string.screen_publish,
        icon = Icons.Default.AddCircle
    )

    object Profile : Screens(
        route = "profile",
        titleResId = R.string.screen_profile,
        icon = Icons.Default.Person
    )

    object Filters : Screens("filters")

    object CategorySelection : Screens("category_selection")
}