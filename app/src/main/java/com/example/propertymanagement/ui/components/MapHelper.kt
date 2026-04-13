package com.example.propertymanagement.ui.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.util.TypedValue
import androidx.core.content.ContextCompat
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.Property
import com.example.propertymanagement.domain.model.UserLocation
import com.example.propertymanagement.ui.theme.MapSizesColors
import com.yandex.mapkit.Animation
import com.yandex.mapkit.geometry.Circle
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CircleMapObject
import com.yandex.mapkit.map.ClusterizedPlacemarkCollection
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import android.graphics.Color as AndroidColor

class MapHelper {

    private var userPlacemark: PlacemarkMapObject? = null
    private var userCircle: CircleMapObject? = null
    private var cameraMoved: Boolean = false
    private val propertyPlacemarks = mutableListOf<PlacemarkMapObject>()

    private var clusterCollection: ClusterizedPlacemarkCollection? = null
    private var zoomListener: CameraListener? = null

    fun updateUserLocation(
        mapView: MapView,
        location: UserLocation
    ) {
        val point = Point(location.lat, location.lon)

        if (userPlacemark == null || userCircle == null) {
            val objects = createUserMapObjects(mapView, point)
            userPlacemark = objects.first
            userCircle = objects.second
        }

        cameraMoved = updateUserLocationOnMap(
            mapView,
            location,
            cameraMoved,
            userCircle,
            userPlacemark
        )
    }

    fun showPropertyMarkers(
        mapView: MapView,
        markers: List<Property>
    ) {
        val context = mapView.context
        val mapObjects = mapView.mapWindow.map.mapObjects

        initClusterCollection(mapObjects)

        val icon = createMarkerIcon(context)

        markers.forEach { marker ->
            val placemark = clusterCollection!!.addPlacemark(
                Point(marker.latitude, marker.longitude),
                icon
            )
            propertyPlacemarks.add(placemark)
        }


        updateVisibilityByZoom(mapView)

        zoomListener?.let { mapView.map.removeCameraListener(it) }

        zoomListener = CameraListener { _, _, _, _ ->
            updateVisibilityByZoom(mapView)
        }
        mapView.map.addCameraListener(zoomListener!!)

        clusterCollection!!.clusterPlacemarks(
            MapSizesColors.CLUSTER_RADIUS_METERS,
            MapSizesColors.CLUSTER_MIN_ZOOM
        )
    }

    //Очист
    fun clearMarkers() {
        clusterCollection?.clear()
        propertyPlacemarks.clear()
    }

    fun release(mapView: MapView) {
        zoomListener?.let {
            mapView.map.removeCameraListener(it)
            zoomListener = null
        }
    }

    private fun updateVisibilityByZoom(mapView: MapView) {
        val zoom = mapView.mapWindow.map.cameraPosition.zoom
        val shouldBeVisible = zoom > MapSizesColors.VISIBILITY_THRESHOLD_ZOOM

        clusterCollection?.setVisible(shouldBeVisible)
    }

    //Работа с кластером
    private fun initClusterCollection(mapObjects: MapObjectCollection) {

        clusterCollection = mapObjects.addClusterizedPlacemarkCollection { cluster ->

            val size = MapSizesColors.CLUSTER_BITMAP_SIZE_PX
            val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)

            val paint = Paint().apply {
                color = Color.parseColor(MapSizesColors.CLUSTER_BACKGROUND_COLOR)
                isAntiAlias = true
            }

            val textPaint = Paint().apply {
                color = Color.parseColor(MapSizesColors.CLUSTER_TEXT_COLOR)
                textSize = MapSizesColors.CLUSTER_TEXT_SIZE_SP.toFloat()
                textAlign = Paint.Align.CENTER
                isAntiAlias = true
                typeface = MapSizesColors.CLUSTER_TEXT_TYPEFACE
            }

            val radius = size * MapSizesColors.CLUSTER_CIRCLE_RADIUS_FACTOR
            canvas.drawCircle(radius, radius, radius, paint)

            val text = cluster.size.toString()
            val yPos = radius - (textPaint.descent() + textPaint.ascent()) / 2
            canvas.drawText(text, radius, yPos, textPaint)

            cluster.appearance.setIcon(ImageProvider.fromBitmap(bitmap))
        }
    }

    private fun createMarkerIcon(context: Context): ImageProvider {
        val sizeInSp = MapSizesColors.MARKER_ICON_SIZE_SP
        val scaledSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            sizeInSp.toFloat(),
            context.resources.displayMetrics
        ).toInt()

        val drawable = ContextCompat.getDrawable(context, R.drawable.point)!!
        val bitmap = Bitmap.createBitmap(scaledSize, scaledSize, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        drawable.setBounds(0, 0, scaledSize, scaledSize)
        drawable.draw(canvas)

        return ImageProvider.fromBitmap(bitmap)
    }

    private fun updateUserLocationOnMap(
        mapView: MapView,
        location: UserLocation,
        cameraMoved: Boolean,
        userCircle: CircleMapObject?,
        userPlacemark: PlacemarkMapObject?
    ): Boolean {

        val point = Point(location.lat, location.lon)

        var cameraWasMoved = cameraMoved

        if (!cameraWasMoved) {
            mapView.map.move(
                CameraPosition(
                    point,
                    MapSizesColors.INITIAL_USER_LOCATION_ZOOM,
                    0f,
                    0f
                ),
                Animation(Animation.Type.SMOOTH, MapSizesColors.CAMERA_MOVE_ANIMATION_DURATION_SEC),
                null
            )
            cameraWasMoved = true
        }

        userCircle?.geometry = Circle(point, MapSizesColors.USER_LOCATION_CIRCLE_RADIUS_METERS)

        userPlacemark?.apply {
            geometry = point
            setIcon(
                ImageProvider.fromBitmap(
                    createUserDirectionMarker(location.bearing)
                )
            )
        }

        return cameraWasMoved
    }

    private fun createUserMapObjects(
        mapView: MapView,
        point: Point
    ): Pair<PlacemarkMapObject, CircleMapObject> {

        val circle = mapView.map.mapObjects.addCircle(
            Circle(point, MapSizesColors.USER_LOCATION_CIRCLE_RADIUS_METERS)
        ).apply {
            fillColor = AndroidColor.argb(
                MapSizesColors.USER_CIRCLE_FILL_ALPHA,
                MapSizesColors.USER_CIRCLE_R,
                MapSizesColors.USER_CIRCLE_G,
                MapSizesColors.USER_CIRCLE_B
            )
            strokeColor = AndroidColor.argb(
                MapSizesColors.USER_CIRCLE_STROKE_ALPHA,
                MapSizesColors.USER_CIRCLE_R,
                MapSizesColors.USER_CIRCLE_G,
                MapSizesColors.USER_CIRCLE_B
            )
            strokeWidth = MapSizesColors.USER_CIRCLE_STROKE_WIDTH
        }

        val placemark = mapView.map.mapObjects.addPlacemark(point)

        return placemark to circle
    }

    private fun createUserDirectionMarker(bearing: Float): Bitmap {
        val size = MapSizesColors.USER_DIRECTION_BITMAP_SIZE_PX
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val cx = size / 2f
        val cy = size / 2f

        val whitePaint = Paint().apply {
            color = AndroidColor.WHITE
            isAntiAlias = true
        }

        val bluePaint = Paint().apply {
            color = AndroidColor.parseColor("#1E88E5")
            isAntiAlias = true
        }

        // Белая обводка
        canvas.drawCircle(cx, cy, MapSizesColors.USER_MARKER_WHITE_CIRCLE_RADIUS, whitePaint)

        // Синий круг
        canvas.drawCircle(cx, cy, MapSizesColors.USER_MARKER_BLUE_CIRCLE_RADIUS, bluePaint)

        // Треугольный «клюв»
        val path = Path()
        path.moveTo(cx, cy - MapSizesColors.USER_MARKER_ARROW_TOP_OFFSET)
        path.lineTo(cx - MapSizesColors.USER_MARKER_ARROW_SIDE_OFFSET, cy - 12f)
        path.lineTo(cx + MapSizesColors.USER_MARKER_ARROW_SIDE_OFFSET, cy - 12f)
        path.close()

        // Поворачиваем по bearing
        val matrix = Matrix()
        matrix.postRotate(bearing, cx, cy)
        path.transform(matrix)

        canvas.drawPath(path, bluePaint)

        return bitmap
    }
}