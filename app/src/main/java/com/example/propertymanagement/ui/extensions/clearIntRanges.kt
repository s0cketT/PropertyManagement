package com.example.propertymanagement.ui.extensions

import com.example.propertymanagement.ui.filters_screen.FiltersState

fun FiltersState.clearIntRanges() = copy(
    area = area.clear(),
    floor = floor.clear(),
    floorHouse = floorHouse.clear(),
    separateRooms = separateRooms.clear()
)