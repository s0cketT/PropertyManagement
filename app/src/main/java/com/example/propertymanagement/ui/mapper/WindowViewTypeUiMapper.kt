package com.example.propertymanagement.ui.mapper

import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.WindowViewType

fun WindowViewType.titleRes(): Int = when (this) {
    WindowViewType.RIVER -> R.string.window_view_river
    WindowViewType.COURTYARD -> R.string.window_view_courtyard
    WindowViewType.PARK -> R.string.window_view_park
    WindowViewType.STREET -> R.string.window_view_street
    WindowViewType.SOUTH -> R.string.window_view_south
    WindowViewType.NORTH -> R.string.window_view_north
    WindowViewType.EAST -> R.string.window_view_east
    WindowViewType.WEST -> R.string.window_view_west
}

