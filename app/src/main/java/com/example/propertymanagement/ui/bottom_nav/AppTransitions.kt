package com.example.propertymanagement.ui.bottom_nav

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavBackStackEntry

/**
 * Переходы навигации: новый экран выезжает справа, уходящий уезжает влево; при pop — наоборот.
 */
object AppTransitions {

    private const val DurationMs = 300

    private val slideSpec: FiniteAnimationSpec<IntOffset> = tween(
        durationMillis = DurationMs,
        easing = FastOutSlowInEasing
    )

    /** Вперёд: новый экран въезжает справа. */
    private val enterForward: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Left,
            animationSpec = slideSpec
        )
    }

    /** Вперёд: текущий экран уезжает влево. */
    private val exitForward: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Left,
            animationSpec = slideSpec
        )
    }

    /** Назад: нижний экран въезжает слева. */
    private val popEnter: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Right,
            animationSpec = slideSpec
        )
    }

    /** Назад: верхний экран уезжает вправо. */
    private val popExit: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Right,
            animationSpec = slideSpec
        )
    }

    val slideFromRight = NavTransition(
        enter = enterForward,
        exit = exitForward,
        popEnter = popEnter,
        popExit = popExit
    )
}

data class NavTransition(
    val enter: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition,
    val exit: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition,
    val popEnter: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition,
    val popExit: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition
)
