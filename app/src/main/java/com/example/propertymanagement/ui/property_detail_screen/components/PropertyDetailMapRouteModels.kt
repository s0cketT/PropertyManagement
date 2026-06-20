package com.example.propertymanagement.ui.property_detail_screen.components

enum class PropertyDetailMapRouteStepKind {
    WALK,
    RIDE,
    TRANSFER,
    WAIT,
}

enum class PropertyDetailMapRouteWalkTarget {
    TO_STOP,
    FROM_STOP,
    TO_DESTINATION,
}

data class PropertyDetailMapRouteStep(
    val kind: PropertyDetailMapRouteStepKind,
    val title: String?,
    val durationText: String? = null,
    val boardStopName: String? = null,
    val exitStopName: String? = null,
    val targetStopName: String? = null,
    val walkTarget: PropertyDetailMapRouteWalkTarget? = null,
    val lineColorArgb: Int? = null,
    val waitUntilRideText: String? = null,
    val transportArrivesAtText: String? = null,
)

data class PropertyDetailMapRouteSummary(
    val durationText: String,
    val distanceText: String,
    val durationSeconds: Double = 0.0,
    val transfersCount: Int = 0,
    val transitSteps: List<PropertyDetailMapRouteStep> = emptyList(),
    val usesLiveTraffic: Boolean = false,
    val usesLiveSchedule: Boolean = false,
)
