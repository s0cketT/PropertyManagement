package com.example.propertymanagement.ui.bottom_nav

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.propertymanagement.ui.theme.BottomBarIconSize
import com.example.propertymanagement.ui.theme.BottomBarSurfaceDark
import com.example.propertymanagement.ui.theme.BottomBarSurfaceLight
import com.example.propertymanagement.ui.theme.BottomBarTextLineHeight
import com.example.propertymanagement.ui.theme.BottomBarTextSize
import com.example.propertymanagement.ui.theme.BottomBarVerticalPadding
import com.example.propertymanagement.ui.theme.LocalAppDarkTheme

@Composable
fun BottomBar(navController: NavController) {

    val items = listOf(
        Screens.Advertisements,
        Screens.Favorites,
        Screens.Publish,
        Screens.Map,
        Screens.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomBarBackground = if (LocalAppDarkTheme.current) {
        BottomBarSurfaceDark
    } else {
        BottomBarSurfaceLight
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(bottomBarBackground)
            .navigationBarsPadding()
            .padding(vertical = BottomBarVerticalPadding),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {

        items.forEach { screen ->

            BottomBarItem(
                screens = screen,
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}

@Composable
private fun BottomBarItem(
    screens: Screens,
    selected: Boolean,
    onClick: () -> Unit
) {

    val scale by animateFloatAsState(
        targetValue = if (selected) 1.1f else 1f,
        label = "scale"
    )

    val color by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        },
        label = "color"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            )
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
    ) {

        Icon(
            imageVector = screens.icon!!,
            contentDescription = screens.title(),
            tint = color,
            modifier = Modifier.size(BottomBarIconSize)
        )

        Text(
            text = screens.title(),
            color = color,
            fontSize = BottomBarTextSize,
            lineHeight = BottomBarTextLineHeight
        )
    }
}
