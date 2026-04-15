package com.example.propertymanagement.ui.theme

import android.graphics.Typeface

object MapSizesColors {
    // Кластер (bitmap для иконки кластера)
    const val CLUSTER_BITMAP_SIZE_PX = 180          // размер bitmap в пикселях (не dp/sp!)
    const val CLUSTER_TEXT_SIZE_SP = 40             // размер текста в кластере
    const val CLUSTER_CIRCLE_RADIUS_FACTOR = 0.5f   // радиус = size / 2

    // Обычная метка (placemark icon)
    const val MARKER_ICON_SIZE_SP = 40              // исходный размер в sp

    // Кластеризация
    const val CLUSTER_RADIUS_METERS = 60.0          // радиус кластеризации в метрах
    const val CLUSTER_MIN_ZOOM = 12                 // минимальный зум для появления кластеров
    const val VISIBILITY_THRESHOLD_ZOOM = 11.0      // зум > этого → показываем метки

    const val CLUSTER_BACKGROUND_COLOR = "#4A90E2"
    const val CLUSTER_TEXT_COLOR = "#FFFFFF"


    // Размер bitmap для стрелки-направления пользователя (в пикселях)
    const val USER_DIRECTION_BITMAP_SIZE_PX = 100

    // Радиус круга вокруг пользователя (в метрах, для Yandex MapKit)
    const val USER_LOCATION_CIRCLE_RADIUS_METERS = 30f

    // Цвета круга (alpha, r, g, b)
    const val USER_CIRCLE_FILL_ALPHA = 30
    const val USER_CIRCLE_STROKE_ALPHA = 60
    const val USER_CIRCLE_R = 30
    const val USER_CIRCLE_G = 136
    const val USER_CIRCLE_B = 229

    // Толщина обводки круга (в пикселях на экране)
    const val USER_CIRCLE_STROKE_WIDTH = 2f

    // Начальный зум при первом позиционировании пользователя
    const val INITIAL_USER_LOCATION_ZOOM = 16f

    /** Зум при показе одного объекта на карте (превью и детальный экран) */
    const val PROPERTY_DETAIL_MAP_ZOOM = 16f

    // Длительность анимации перемещения камеры (в секундах)
    const val CAMERA_MOVE_ANIMATION_DURATION_SEC = 1f

    // ----- Элементы стрелки-направления (в пикселях на bitmap) -----

    // Радиус белой обводки (фон)
    const val USER_MARKER_WHITE_CIRCLE_RADIUS = 28f

    // Радиус синего круга
    const val USER_MARKER_BLUE_CIRCLE_RADIUS = 20f

    // Размеры треугольника-"клюва" (относительно центра)
    const val USER_MARKER_ARROW_TOP_OFFSET = 34f      // насколько вверх от центра
    const val USER_MARKER_ARROW_SIDE_OFFSET = 12f     // ширина основания треугольника / 2

    val CLUSTER_TEXT_TYPEFACE: Typeface = Typeface.create(Typeface.SERIF, Typeface.ITALIC)
}