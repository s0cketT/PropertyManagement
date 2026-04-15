package com.example.propertymanagement.ui.theme

object MapSizesColors {

    // Обычная метка (placemark icon)
    const val MARKER_ICON_SIZE_SP = 40              // исходный размер в sp

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

    /** Зум при показе одного объекта на карте (превью и полноэкранная карта) */
    const val PROPERTY_DETAIL_MAP_ZOOM = 16f

    // Длительность анимации перемещения камеры (в секундах)
    const val CAMERA_MOVE_ANIMATION_DURATION_SEC = 1f

    // ----- Элементы стрелки-направления (в пикселях на bitmap) -----

    const val USER_MARKER_WHITE_CIRCLE_RADIUS = 28f

    const val USER_MARKER_BLUE_CIRCLE_RADIUS = 20f

    const val USER_MARKER_ARROW_TOP_OFFSET = 34f
    const val USER_MARKER_ARROW_SIDE_OFFSET = 12f
}
