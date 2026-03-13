package com.example.propertymanagement.ui.filters_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.propertymanagement.ui.theme.DividerColor
import com.example.propertymanagement.ui.theme.OnSurfaceVariant
import com.example.propertymanagement.ui.theme.WheelPickerItemHeight
import com.example.propertymanagement.ui.theme.WheelPickerLetterSpacing
import com.example.propertymanagement.ui.theme.WheelPickerLineThickness
import com.example.propertymanagement.ui.theme.WheelPickerLineWidth
import com.example.propertymanagement.ui.theme.WheelPickerVisibleItems
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

@Composable
fun WheelPicker(
    values: List<Int>,
    initialValue: Int = values.first(),
    modifier: Modifier = Modifier,
    resetTrigger: Int = 0,
    displayMapper: (Int) -> String = { it.toString() },
    onValueSelected: (Int) -> Unit
) {

    val centerIndex = WheelPickerVisibleItems / 2
    val itemHeight = WheelPickerItemHeight

    val anchorIndex = Int.MAX_VALUE / 2
    val baseIndex = anchorIndex - anchorIndex % values.size

    val initialIndex = values.indexOf(initialValue).coerceAtLeast(0)
    val initialListIndex = baseIndex + initialIndex - centerIndex

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = baseIndex + initialIndex - centerIndex
    )

    LaunchedEffect(resetTrigger) {
        listState.animateScrollToItem(initialListIndex)
    }

    val flingBehavior = rememberSnapFlingBehavior(listState)


    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }
            .distinctUntilChanged()
            .filter { !it } // когда скролл остановился
            .map {
                val center = listState.firstVisibleItemIndex + centerIndex
                values[Math.floorMod(center - baseIndex, values.size)]
            }
            .collect {
                onValueSelected(it) }
    }

    Box(
        modifier = modifier
            .height(itemHeight * WheelPickerVisibleItems)
            .fillMaxWidth()
    ) {

        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            modifier = Modifier.fillMaxSize()
        ) {

            items(Int.MAX_VALUE) { index ->

                val value = values[Math.floorMod(index - baseIndex, values.size)]

                val firstIndex = listState.firstVisibleItemIndex
                val offset = listState.firstVisibleItemScrollOffset.toFloat() / itemHeight.value

                val itemPosition = index - firstIndex - offset
                val distanceFromCenter = itemPosition - centerIndex

                var rotationX = distanceFromCenter * 18f
                val scale = 1f - (0.12f * kotlin.math.abs(distanceFromCenter))
                val alpha = 1f - (0.25f * kotlin.math.abs(distanceFromCenter))

                Box(
                    modifier = Modifier
                        .height(itemHeight)
                        .fillMaxWidth()
                        .graphicsLayer {

                            cameraDistance = 8 * density

                            rotationX = rotationX

                            scaleX = scale.coerceAtLeast(0.7f)
                            scaleY = scale.coerceAtLeast(0.7f)

                            this.alpha = alpha.coerceAtLeast(0.2f)
                        },
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = displayMapper(value),
                        style = TextStyle(
                            fontWeight = FontWeight.Thin,
                            letterSpacing = WheelPickerLetterSpacing,
                            color = OnSurfaceVariant
                        )
                    )
                }
            }
        }

        val half = itemHeight / 2

        listOf(-half, half).forEach { offset ->

            Box(
                Modifier
                    .align(Alignment.Center)
                    .width(WheelPickerLineWidth)
                    .height(WheelPickerLineThickness)
                    .offset(y = offset)
                    .background(DividerColor)
            )
        }
    }
}