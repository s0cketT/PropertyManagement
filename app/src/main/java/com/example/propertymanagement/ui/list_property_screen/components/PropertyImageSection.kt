package com.example.propertymanagement.ui.list_property_screen.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.propertymanagement.ui.theme.FavoriteIconSize
import com.example.propertymanagement.ui.theme.ImagePickerHeight
import com.example.propertymanagement.ui.theme.IndicatorSize
import com.example.propertymanagement.ui.theme.IndicatorSizeActive
import com.example.propertymanagement.ui.theme.PaddingMedium
import com.example.propertymanagement.ui.theme.PaddingSmall

@Composable
fun PropertyImageSection(
    photos: List<String>,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    showFavoriteButton: Boolean = true
) {
    Box {

        PropertyImages(photos)

        if (showFavoriteButton) {
            FavoriteButton(
                isFavorite = isFavorite,
                onClick = onFavoriteClick,
                prominent = true,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(PaddingSmall)
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PropertyImages(photos: List<String>) {
    PropertyImagePager(
        photos = photos,
        modifier = Modifier
            .fillMaxWidth()
            .height(ImagePickerHeight)
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PropertyImagePager(
    photos: List<String>,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(ImagePickerHeight),
    onPhotoClick: ((pageIndex: Int) -> Unit)? = null
) {

    if (photos.isEmpty()) {
        Box(
            modifier = modifier
                .background(MaterialTheme.colorScheme.outlineVariant)
        )
        return
    }

    if (photos.size == 1) {
        AsyncImage(
            model = photos.first(),
            contentDescription = null,
            modifier = modifier
                .then(
                    if (onPhotoClick != null) {
                        Modifier.clickable { onPhotoClick(0) }
                    } else {
                        Modifier
                    }
                ),
            contentScale = ContentScale.Crop
        )
        return
    }

    val pagerState = rememberPagerState(pageCount = { photos.size })

    Box(modifier = modifier) {

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            AsyncImage(
                model = photos[page],
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .then(
                        if (onPhotoClick != null) {
                            Modifier.clickable { onPhotoClick(page) }
                        } else {
                            Modifier
                        }
                    ),
                contentScale = ContentScale.Crop
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = PaddingMedium),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(photos.size) { index ->

                val isSelected = pagerState.currentPage == index

                Box(
                    modifier = Modifier
                        .padding(horizontal = PaddingSmall)
                        .size(if (isSelected) IndicatorSizeActive else IndicatorSize)
                        .clip(CircleShape)
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                )
            }
        }
    }
}

private val FavoriteProminentSize = 44.dp
private val FavoriteProminentIconSize = 26.dp

@Composable
fun FavoriteButton(
    isFavorite: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    prominent: Boolean = false,
    contentDescription: String? = null
) {
    val scheme = MaterialTheme.colorScheme

    if (prominent) {
        Surface(
            onClick = onClick,
            shape = CircleShape,
            color = scheme.surface.copy(alpha = 0.52f),
            border = BorderStroke(
                width = 0.5.dp,
                color = scheme.outline.copy(alpha = 0.16f)
            ),
            shadowElevation = 0.dp,
            modifier = modifier.size(FavoriteProminentSize)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                FavoriteAnimatedIcon(
                    isFavorite = isFavorite,
                    contentDescription = contentDescription,
                    modifier = Modifier.size(FavoriteProminentIconSize)
                )
            }
        }
    } else {
        IconButton(
            onClick = onClick,
            modifier = modifier.size(FavoriteIconSize)
        ) {
            FavoriteAnimatedIcon(
                isFavorite = isFavorite,
                contentDescription = contentDescription,
                modifier = Modifier.size(26.dp)
            )
        }
    }
}

@Composable
private fun FavoriteAnimatedIcon(
    isFavorite: Boolean,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    val scheme = MaterialTheme.colorScheme
    val infiniteTransition = rememberInfiniteTransition(label = "favorite")

    val animatedScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 900,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val animatedColor by infiniteTransition.animateColor(
        initialValue = scheme.primary,
        targetValue = scheme.tertiary,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse,
            initialStartOffset = StartOffset(500)
        ),
        label = "color"
    )

    val outlineTint = scheme.primary
    val iconVector =
        if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder
    val tint = when {
        !isFavorite -> outlineTint
        else -> animatedColor
    }
    val scale = if (isFavorite) animatedScale else 1f

    Icon(
        imageVector = iconVector,
        contentDescription = contentDescription,
        tint = tint,
        modifier = modifier.scale(scale)
    )
}
