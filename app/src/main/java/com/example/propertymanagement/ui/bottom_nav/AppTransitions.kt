package com.example.propertymanagement.ui.bottom_nav

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavBackStackEntry

object AppTransitions {

    val defaultEnter: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        slideInHorizontally(
            initialOffsetX = { fullWidth -> fullWidth },
            animationSpec = tween(
                durationMillis = 420,
                easing = FastOutSlowInEasing
            )
        ) + fadeIn(animationSpec = tween(300))
    }

    val defaultExit: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        slideOutHorizontally(
            targetOffsetX = { fullWidth -> -fullWidth / 2 },
            animationSpec = tween(
                durationMillis = 380,
                easing = FastOutSlowInEasing
            )
        ) + fadeOut(animationSpec = tween(280))
    }

    val defaultPopEnter: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        slideInHorizontally(
            initialOffsetX = { fullWidth -> -fullWidth },
            animationSpec = tween(420, easing = FastOutSlowInEasing)
        ) + fadeIn(animationSpec = tween(300))
    }

    val defaultPopExit: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        slideOutHorizontally(
            targetOffsetX = { fullWidth -> fullWidth },
            animationSpec = tween(380, easing = LinearOutSlowInEasing)
        ) + fadeOut(animationSpec = tween(280))
    }

    // Готовый набор для типичного свайпа вправо → влево
    val slideFromRight = NavTransition(
        enter = defaultEnter,
        exit = defaultExit,
        popEnter = defaultPopEnter,
        popExit = defaultPopExit
    )
}

// Вспомогательный data class для удобства (опционально)
data class NavTransition(
    val enter: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition,
    val exit: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition,
    val popEnter: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition,
    val popExit: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition
)