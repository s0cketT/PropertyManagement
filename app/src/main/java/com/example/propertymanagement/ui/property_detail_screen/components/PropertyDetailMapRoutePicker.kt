package com.example.propertymanagement.ui.property_detail_screen.components

import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.transport.masstransit.Route
import com.yandex.mapkit.transport.masstransit.TimeOptions

internal object PropertyDetailMapRoutePicker {

    fun liveDepartureTimeOptions(): TimeOptions =
        TimeOptions().setDepartureTime(System.currentTimeMillis())

    fun fastestDrivingRoute(routes: List<DrivingRoute>): DrivingRoute? =
        routes.minByOrNull { it.metadata.weight.timeWithTraffic.value }

    fun fastestMasstransitRoute(routes: List<Route>): Route? =
        routes.minByOrNull { it.metadata.weight.time.value }
}
