package com.example.propertymanagement.ui.property_detail_screen.components

import android.content.Context
import android.widget.Toast
import com.example.propertymanagement.domain.model.Property
import com.example.propertymanagement.domain.model.UserLocation
import com.example.propertymanagement.ui.components.MapHelper
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.mapview.MapView

internal enum class PropertyDetailMapRouteOriginSource {
    MY_LOCATION,
    CUSTOM,
}

internal fun requestPropertyDetailMapRoute(
    mapView: MapView,
    routeController: PropertyDetailMapRouteController,
    property: Property,
    selectedMode: PropertyDetailMapRouteMode,
    isDarkTheme: Boolean,
    origin: UserLocation,
    routeErrorMessage: String,
    onLoading: () -> Unit,
    onSuccess: (PropertyDetailMapRouteSummary) -> Unit,
    onFailure: () -> Unit,
) {
    onLoading()
    routeController.requestRoute(
        mapView = mapView,
        mode = selectedMode,
        isDarkTheme = isDarkTheme,
        fromLat = origin.lat,
        fromLon = origin.lon,
        toLat = property.latitude,
        toLon = property.longitude,
        onSuccess = { summary ->
            mapView.post { onSuccess(summary) }
        },
        onFailure = {
            mapView.post {
                onFailure()
                Toast.makeText(mapView.context, routeErrorMessage, Toast.LENGTH_SHORT).show()
            }
        },
    )
}

internal class PropertyDetailMapRouteOriginOverlay {

    private var placemark: PlacemarkMapObject? = null

    fun update(
        mapView: MapView,
        mapHelper: MapHelper,
        context: Context,
        origin: UserLocation?,
    ) {
        clear(mapView)
        if (origin == null) return
        placemark = mapView.map.mapObjects.addPlacemark(
            Point(origin.lat, origin.lon),
        ).apply {
            setIcon(mapHelper.propertyMarkerIcon(context))
            zIndex = ROUTE_ORIGIN_Z_INDEX
        }
    }

    fun clear(mapView: MapView) {
        placemark?.let { mapView.map.mapObjects.remove(it) }
        placemark = null
    }

    private companion object {
        private const val ROUTE_ORIGIN_Z_INDEX = 4f
    }
}

internal fun resolveRouteOrigin(
    source: PropertyDetailMapRouteOriginSource,
    myLocationOrigin: UserLocation?,
    customOrigin: UserLocation?,
    liveUserLocation: UserLocation?,
): UserLocation? = when (source) {
    PropertyDetailMapRouteOriginSource.MY_LOCATION ->
        myLocationOrigin ?: liveUserLocation
    PropertyDetailMapRouteOriginSource.CUSTOM -> customOrigin
}
