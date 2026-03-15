package com.example.propertymanagement.ui.components

val areaValues = listOf(200, -1, 0, 25, 30, 35, 40, 50, 60, 70, 80, 90, 100, 125, 150, 175)
val floorValues = listOf(30, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29)
val separateRoomsValues = listOf(20, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19)

val floorDisplayMapper: (Int) -> String = {
    when(it) {
        -1 -> "Любой"
        30 -> "Больше 30"
        else -> it.toString()
    }
}

val areaDisplayMapper: (Int) -> String = {
    if (it == -1) "Любой" else it.toString()
}

val separateRoomsDisplayMapper: (Int) -> String = {
    if (it == -1) "Любой" else it.toString()
}