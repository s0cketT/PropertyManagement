package com.example.propertymanagement.ui.property_detail_screen.components

import com.example.propertymanagement.ui.theme.MapSizesColors
import com.yandex.mapkit.Animation
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.DrivingOptions
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.DrivingRouterType
import com.yandex.mapkit.directions.driving.DrivingSession
import com.yandex.mapkit.directions.driving.DrivingSession.DrivingRouteListener
import com.yandex.mapkit.directions.driving.VehicleOptions
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polyline
import com.yandex.mapkit.geometry.SubpolylineHelper
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.PolylineMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.transport.TransportFactory
import com.yandex.mapkit.transport.masstransit.BicycleRouterV2
import com.yandex.mapkit.transport.masstransit.FilterVehicleTypes
import com.yandex.mapkit.transport.masstransit.FitnessOptions
import com.yandex.mapkit.transport.masstransit.MasstransitRouter
import com.yandex.mapkit.transport.masstransit.PedestrianRouter
import com.yandex.mapkit.transport.masstransit.Route
import com.yandex.mapkit.transport.masstransit.RouteOptions
import com.yandex.mapkit.transport.masstransit.Session
import com.yandex.mapkit.transport.masstransit.TimeOptions
import com.yandex.mapkit.transport.masstransit.TransitOptions
import com.yandex.runtime.Error

class PropertyDetailMapRouteController {

    private val drivingRouter = DirectionsFactory.getInstance()
        .createDrivingRouter(DrivingRouterType.ONLINE)
    private val pedestrianRouter: PedestrianRouter =
        TransportFactory.getInstance().createPedestrianRouter()
    private val bicycleRouter: BicycleRouterV2 =
        TransportFactory.getInstance().createBicycleRouterV2()
    private val masstransitRouter: MasstransitRouter =
        TransportFactory.getInstance().createMasstransitRouter()

    private var activeMasstransitSession: Session? = null
    private var activeDrivingSession: DrivingSession? = null
    private var routesCollection: MapObjectCollection? = null
    private var isDarkTheme = false
    private var cachedMode: PropertyDetailMapRouteMode? = null
    private var cachedMasstransitRoute: Route? = null
    private var cachedPolylineGeometry: Polyline? = null

    fun requestRoute(
        mapView: MapView,
        mode: PropertyDetailMapRouteMode,
        isDarkTheme: Boolean,
        fromLat: Double,
        fromLon: Double,
        toLat: Double,
        toLon: Double,
        onSuccess: (PropertyDetailMapRouteSummary) -> Unit,
        onFailure: () -> Unit,
    ) {
        this.isDarkTheme = isDarkTheme
        clearRoute(mapView)

        if (mode == PropertyDetailMapRouteMode.DRIVING) {
            requestDrivingRoute(
                mapView = mapView,
                fromLat = fromLat,
                fromLon = fromLon,
                toLat = toLat,
                toLon = toLon,
                onSuccess = onSuccess,
                onFailure = onFailure,
            )
            return
        }

        val requestPoints = buildRequestPoints(fromLat, fromLon, toLat, toLon)
        val routeOptions = RouteOptions(FitnessOptions(false, false))
        val listener = object : Session.RouteListener {
            override fun onMasstransitRoutes(routes: MutableList<Route>) {
                val route = PropertyDetailMapRoutePicker.fastestMasstransitRoute(routes)
                if (route == null) {
                    onFailure()
                    return
                }
                displayMasstransitRoute(mapView, route, mode)
                runCatching {
                    PropertyDetailMapRouteMapper.masstransitSummary(route, mode)
                }.onSuccess(onSuccess).onFailure { onFailure() }
            }

            override fun onMasstransitRoutesError(error: Error) {
                onFailure()
            }
        }

        activeMasstransitSession = when (mode) {
            PropertyDetailMapRouteMode.PEDESTRIAN -> pedestrianRouter.requestRoutes(
                requestPoints,
                TimeOptions(),
                routeOptions,
                listener,
            )
            PropertyDetailMapRouteMode.BICYCLE -> bicycleRouter.requestRoutes(
                requestPoints,
                TimeOptions(),
                routeOptions,
                listener,
            )
            PropertyDetailMapRouteMode.TRANSIT -> masstransitRouter.requestRoutes(
                requestPoints,
                TransitOptions(
                    FilterVehicleTypes.NONE.value,
                    PropertyDetailMapRoutePicker.liveDepartureTimeOptions(),
                ),
                routeOptions,
                listener,
            )
            PropertyDetailMapRouteMode.DRIVING -> error("handled above")
        }
    }

    fun updateTheme(mapView: MapView, isDarkTheme: Boolean) {
        if (this.isDarkTheme == isDarkTheme) return
        this.isDarkTheme = isDarkTheme
        val mode = cachedMode ?: return
        cachedMasstransitRoute?.let { route ->
            removeRouteOverlay(mapView)
            displayMasstransitRoute(
                mapView = mapView,
                route = route,
                mode = mode,
                refitCamera = false,
            )
            return
        }
        cachedPolylineGeometry?.let { geometry ->
            removeRouteOverlay(mapView)
            displayPolylineRoute(
                mapView = mapView,
                geometry = geometry,
                mode = mode,
                refitCamera = false,
            )
        }
    }

    fun clearRoute(mapView: MapView) {
        cancelActiveSessions()
        removeRouteOverlay(mapView)
        cachedMode = null
        cachedMasstransitRoute = null
        cachedPolylineGeometry = null
    }

    fun release(mapView: MapView) {
        clearRoute(mapView)
    }

    private fun requestDrivingRoute(
        mapView: MapView,
        fromLat: Double,
        fromLon: Double,
        toLat: Double,
        toLon: Double,
        onSuccess: (PropertyDetailMapRouteSummary) -> Unit,
        onFailure: () -> Unit,
    ) {
        val requestPoints = buildRequestPoints(fromLat, fromLon, toLat, toLon)
        val drivingOptions = DrivingOptions().apply {
            routesCount = DRIVING_ROUTE_ALTERNATIVES
            departureTime = System.currentTimeMillis()
        }
        activeDrivingSession = drivingRouter.requestRoutes(
            requestPoints,
            drivingOptions,
            VehicleOptions(),
            object : DrivingRouteListener {
                override fun onDrivingRoutes(routes: MutableList<DrivingRoute>) {
                    val route = PropertyDetailMapRoutePicker.fastestDrivingRoute(routes)
                    if (route == null) {
                        onFailure()
                        return
                    }
                    displayPolylineRoute(
                        mapView = mapView,
                        geometry = route.geometry,
                        mode = PropertyDetailMapRouteMode.DRIVING,
                    )
                    onSuccess(PropertyDetailMapRouteMapper.drivingSummary(route))
                }

                override fun onDrivingRoutesError(error: Error) {
                    onFailure()
                }
            },
        )
    }

    private fun buildRequestPoints(
        fromLat: Double,
        fromLon: Double,
        toLat: Double,
        toLon: Double,
    ): List<RequestPoint> = listOf(
        RequestPoint(Point(fromLat, fromLon), RequestPointType.WAYPOINT, null, null, null),
        RequestPoint(Point(toLat, toLon), RequestPointType.WAYPOINT, null, null, null),
    )

    private fun displayMasstransitRoute(
        mapView: MapView,
        route: Route,
        mode: PropertyDetailMapRouteMode,
        refitCamera: Boolean = true,
    ) {
        val collection = mapView.map.mapObjects.addCollection()
        routesCollection = collection
        collection.zIndex = ROUTE_Z_INDEX
        cachedMode = mode
        cachedMasstransitRoute = route
        cachedPolylineGeometry = null

        if (mode == PropertyDetailMapRouteMode.TRANSIT) {
            route.sections.forEach { section ->
                val sectionGeometry = SubpolylineHelper.subpolyline(route.geometry, section.geometry)
                val polyline = collection.addPolyline(sectionGeometry)
                val isRide = !section.metadata.data.transports.isNullOrEmpty()
                if (isRide) {
                    val lineColor = section.metadata.data.transports
                        ?.firstOrNull()
                        ?.line
                        ?.style
                        ?.color
                        ?.let { color ->
                            if (color and 0xFF000000.toInt() != 0) color else color or 0xFF000000.toInt()
                        }
                    val fallback = PropertyDetailMapRouteColors.transitDefaultStroke(isDarkTheme)
                    polyline.applySolidStyle(
                        strokeColor = PropertyDetailMapRouteColors.lineColor(
                            isDarkTheme = isDarkTheme,
                            overrideColor = lineColor,
                            fallbackResColor = fallback,
                        ),
                    )
                } else {
                    polyline.applyDashedStyle(
                        strokeColor = PropertyDetailMapRouteColors.walkSegmentStroke(isDarkTheme),
                    )
                }
            }
        } else {
            val polyline = collection.addPolyline(route.geometry)
            polyline.applyStyleForMode(mode)
        }

        if (refitCamera) {
            fitCameraToRoute(mapView, route.geometry)
        }
    }

    private fun displayPolylineRoute(
        mapView: MapView,
        geometry: Polyline,
        mode: PropertyDetailMapRouteMode,
        refitCamera: Boolean = true,
    ) {
        val collection = mapView.map.mapObjects.addCollection()
        routesCollection = collection
        collection.zIndex = ROUTE_Z_INDEX
        cachedMode = mode
        cachedPolylineGeometry = geometry
        cachedMasstransitRoute = null

        val polyline = collection.addPolyline(geometry)
        polyline.applyStyleForMode(mode)
        if (refitCamera) {
            fitCameraToRoute(mapView, geometry)
        }
    }

    private fun PolylineMapObject.applyStyleForMode(mode: PropertyDetailMapRouteMode) {
        when (mode) {
            PropertyDetailMapRouteMode.PEDESTRIAN -> applyDashedStyle(
                strokeColor = PropertyDetailMapRouteColors.pedestrianStroke(isDarkTheme),
            )
            PropertyDetailMapRouteMode.BICYCLE -> applySolidStyle(
                strokeColor = PropertyDetailMapRouteColors.bicycleStroke(isDarkTheme),
            )
            PropertyDetailMapRouteMode.DRIVING -> applySolidStyle(
                strokeColor = PropertyDetailMapRouteColors.drivingStroke(isDarkTheme),
            )
            PropertyDetailMapRouteMode.TRANSIT -> applySolidStyle(
                strokeColor = PropertyDetailMapRouteColors.transitDefaultStroke(isDarkTheme),
            )
        }
    }

    private fun PolylineMapObject.applySolidStyle(strokeColor: Int) {
        zIndex = ROUTE_Z_INDEX
        setStrokeColor(strokeColor)
        style = style.apply {
            strokeWidth = PropertyDetailMapRouteColors.strokeWidth(isDarkTheme)
            outlineColor = PropertyDetailMapRouteColors.outline(isDarkTheme)
            outlineWidth = PropertyDetailMapRouteColors.outlineWidth(isDarkTheme)
            dashLength = 0f
            gapLength = 0f
        }
    }

    private fun PolylineMapObject.applyDashedStyle(strokeColor: Int) {
        zIndex = ROUTE_Z_INDEX
        setStrokeColor(strokeColor)
        style = style.apply {
            strokeWidth = PropertyDetailMapRouteColors.strokeWidth(isDarkTheme)
            outlineColor = PropertyDetailMapRouteColors.outline(isDarkTheme)
            outlineWidth = PropertyDetailMapRouteColors.outlineWidth(isDarkTheme)
            dashLength = ROUTE_DASH_LENGTH
            gapLength = ROUTE_GAP_LENGTH
        }
    }

    private fun removeRouteOverlay(mapView: MapView) {
        routesCollection?.let { collection ->
            mapView.map.mapObjects.remove(collection)
        }
        routesCollection = null
    }

    private fun fitCameraToRoute(mapView: MapView, geometry: Polyline) {
        val position = mapView.map.cameraPosition(
            Geometry.fromPolyline(geometry),
            null,
            null,
            null,
        )
        val adjustedZoom = (position.zoom - ROUTE_CAMERA_ZOOM_OUT_DELTA)
            .coerceIn(MapSizesColors.MAP_ZOOM_MIN, MapSizesColors.MAP_ZOOM_MAX)
        mapView.map.move(
            CameraPosition(
                position.target,
                adjustedZoom,
                position.azimuth,
                position.tilt,
            ),
            Animation(Animation.Type.SMOOTH, MapSizesColors.CAMERA_MOVE_ANIMATION_DURATION_SEC),
            null,
        )
    }

    private fun cancelActiveSessions() {
        activeMasstransitSession?.cancel()
        activeDrivingSession?.cancel()
        activeMasstransitSession = null
        activeDrivingSession = null
    }

    private companion object {
        private const val ROUTE_Z_INDEX = 2f
        private const val DRIVING_ROUTE_ALTERNATIVES = 3
        private const val ROUTE_DASH_LENGTH = 8f
        private const val ROUTE_GAP_LENGTH = 6f
        private const val ROUTE_CAMERA_ZOOM_OUT_DELTA = 0.6f
    }
}
