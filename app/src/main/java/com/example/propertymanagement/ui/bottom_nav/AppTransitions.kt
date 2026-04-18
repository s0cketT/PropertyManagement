package com.example.propertymanagement.ui.bottom_nav

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavBackStackEntry

/**
 * Переходы в духе Material motion: не полноэкранный «сдвиг», а короткая дистанция (~20% ширины),
 * лёгкий fade и чуть scale на входе — выглядит современно, без перегруза.
 */
object AppTransitions {

    /** Emphasized decelerate — близко к спецификации Material 3. */
    private val EnterEase = CubicBezierEasing(0.2f, 0f, 0f, 1f)
    private val ExitEase = CubicBezierEasing(0.4f, 0f, 0.2f, 1f)

    private const val EnterMs = 340
    private const val ExitMs = 280
    private const val FadeEnterMs = 300
    private const val FadeExitMs = 240

    /** Доля ширины экрана для горизонтального сдвига (не на всю ширину). */
    private fun offsetFraction(fraction: Float): (Int) -> Int = { (it * fraction).toInt() }

    val defaultEnter: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        slideInHorizontally(
            initialOffsetX = offsetFraction(0.22f),
            animationSpec = tween(EnterMs, easing = EnterEase)
        ) + fadeIn(
            animationSpec = tween(FadeEnterMs, easing = EnterEase)
        ) + scaleIn(
            initialScale = 0.96f,
            animationSpec = tween(EnterMs, easing = EnterEase)
        )
    }

    val defaultExit: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        slideOutHorizontally(
            targetOffsetX = offsetFraction(-0.12f),
            animationSpec = tween(ExitMs, easing = ExitEase)
        ) + fadeOut(
            animationSpec = tween(FadeExitMs, easing = ExitEase)
        ) + scaleOut(
            targetScale = 0.96f,
            animationSpec = tween(ExitMs, easing = ExitEase)
        )
    }

    val defaultPopEnter: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
        slideInHorizontally(
            initialOffsetX = offsetFraction(-0.22f),
            animationSpec = tween(EnterMs, easing = EnterEase)
        ) + fadeIn(
            animationSpec = tween(FadeEnterMs, easing = EnterEase)
        ) + scaleIn(
            initialScale = 0.96f,
            animationSpec = tween(EnterMs, easing = EnterEase)
        )
    }

    val defaultPopExit: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
        slideOutHorizontally(
            targetOffsetX = offsetFraction(0.22f),
            animationSpec = tween(ExitMs, easing = ExitEase)
        ) + fadeOut(
            animationSpec = tween(FadeExitMs, easing = ExitEase)
        ) + scaleOut(
            targetScale = 0.96f,
            animationSpec = tween(ExitMs, easing = ExitEase)
        )
    }

    val slideFromRight = NavTransition(
        enter = defaultEnter,
        exit = defaultExit,
        popEnter = defaultPopEnter,
        popExit = defaultPopExit
    )
}

data class NavTransition(
    val enter: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition,
    val exit: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition,
    val popEnter: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition,
    val popExit: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition
)
