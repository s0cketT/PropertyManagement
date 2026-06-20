package com.example.propertymanagement.ui.property_detail_screen.components

import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.transport.masstransit.Route
import com.yandex.mapkit.transport.masstransit.Section

internal object PropertyDetailMapRouteMapper {

    fun drivingSummary(route: DrivingRoute): PropertyDetailMapRouteSummary {
        val weight = route.metadata.weight
        return PropertyDetailMapRouteSummary(
            durationText = weight.timeWithTraffic.text,
            distanceText = weight.distance.text,
            durationSeconds = weight.timeWithTraffic.value,
            usesLiveTraffic = true,
        )
    }

    fun masstransitSummary(route: Route, mode: PropertyDetailMapRouteMode): PropertyDetailMapRouteSummary {
        val weight = route.metadata.weight
        return PropertyDetailMapRouteSummary(
            durationText = weight.time.text,
            distanceText = route.masstransitDistanceText(mode),
            durationSeconds = weight.time.value,
            transfersCount = if (mode == PropertyDetailMapRouteMode.TRANSIT) weight.transfersCount else 0,
            transitSteps = if (mode == PropertyDetailMapRouteMode.TRANSIT) route.toTransitSteps() else emptyList(),
            usesLiveSchedule = mode == PropertyDetailMapRouteMode.TRANSIT,
        )
    }

    private fun Route.toTransitSteps(): List<PropertyDetailMapRouteStep> = runCatching {
        buildTransitSteps()
    }.getOrDefault(emptyList())

    private fun Route.buildTransitSteps(): List<PropertyDetailMapRouteStep> {
        val routeSections = sections
        var pendingWaitDuration: String? = null
        var pendingTransportArrivesAt: String? = null
        val steps = mutableListOf<PropertyDetailMapRouteStep>()

        routeSections.forEachIndexed { index, section ->
            val data = section.metadata.data
            val duration = section.metadata.weight.time.text
            val transports = data.transports
            when {
                data.wait != null -> {
                    pendingWaitDuration = duration
                    pendingTransportArrivesAt = section.metadata.estimation?.arrivalTime?.displayClockTime()
                        ?: section.metadata.estimation?.departureTime?.displayClockTime()
                }

                !transports.isNullOrEmpty() -> {
                    val transport = transports.firstOrNull() ?: return@forEachIndexed
                    val line = transport.line
                    val boardStop = section.boardStopName()
                    val exitStop = section.exitStopName()
                    val rideDepartureTime = section.metadata.estimation?.departureTime?.displayClockTime()
                    steps.add(
                        PropertyDetailMapRouteStep(
                            kind = PropertyDetailMapRouteStepKind.RIDE,
                            title = line.name?.takeIf { it.isNotBlank() },
                            durationText = duration,
                            boardStopName = boardStop,
                            exitStopName = exitStop?.takeIf { it != boardStop },
                            lineColorArgb = line.style?.color?.let { color ->
                                if (color and 0xFF000000.toInt() != 0) color else color or 0xFF000000.toInt()
                            },
                            waitUntilRideText = pendingWaitDuration,
                            transportArrivesAtText = pendingTransportArrivesAt ?: rideDepartureTime,
                        ),
                    )
                    pendingWaitDuration = null
                    pendingTransportArrivesAt = null
                }

                data.fitness != null -> {
                    pendingWaitDuration = null
                    pendingTransportArrivesAt = null
                    val nextBoardStop = routeSections.findNextBoardStop(afterIndex = index)
                    val previousExitStop = routeSections.findPreviousExitStop(beforeIndex = index)
                    val hasUpcomingRide = nextBoardStop != null
                    val walkTarget = when {
                        hasUpcomingRide -> PropertyDetailMapRouteWalkTarget.TO_STOP
                        previousExitStop != null -> PropertyDetailMapRouteWalkTarget.FROM_STOP
                        else -> PropertyDetailMapRouteWalkTarget.TO_DESTINATION
                    }
                    steps.add(
                        PropertyDetailMapRouteStep(
                            kind = PropertyDetailMapRouteStepKind.WALK,
                            title = null,
                            durationText = duration,
                            targetStopName = nextBoardStop ?: previousExitStop,
                            walkTarget = walkTarget,
                        ),
                    )
                }

                data.transfer != null -> {
                    pendingWaitDuration = null
                    pendingTransportArrivesAt = null
                    val nextBoardStop = routeSections.findNextBoardStop(afterIndex = index)
                    steps.add(
                        PropertyDetailMapRouteStep(
                            kind = PropertyDetailMapRouteStepKind.TRANSFER,
                            title = null,
                            durationText = duration,
                            targetStopName = nextBoardStop,
                            walkTarget = nextBoardStop?.let { PropertyDetailMapRouteWalkTarget.TO_STOP },
                        ),
                    )
                }
            }
        }

        return steps
    }

    private fun Section.boardStopName(): String? =
        stops.firstOrNull()?.metadata?.stop?.name?.trim()?.takeIf { it.isNotEmpty() }

    private fun Section.exitStopName(): String? =
        stops.lastOrNull()?.metadata?.stop?.name?.trim()?.takeIf { it.isNotEmpty() }

    private fun List<Section>.findNextBoardStop(afterIndex: Int): String? {
        for (index in (afterIndex + 1) until size) {
            val boardStop = this[index].boardStopName()
            if (!this[index].metadata.data.transports.isNullOrEmpty() && boardStop != null) {
                return boardStop
            }
        }
        return null
    }

    private fun List<Section>.findPreviousExitStop(beforeIndex: Int): String? {
        for (index in beforeIndex - 1 downTo 0) {
            val exitStop = this[index].exitStopName()
            if (!this[index].metadata.data.transports.isNullOrEmpty() && exitStop != null) {
                return exitStop
            }
        }
        return null
    }
}
