package com.example.propertymanagement.ui.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.RectF
import android.util.TypedValue
import androidx.core.content.ContextCompat
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.CurrencyType
import com.example.propertymanagement.domain.model.Property
import com.example.propertymanagement.domain.model.UserLocation
import com.example.propertymanagement.ui.common.formatPrice
import com.example.propertymanagement.ui.theme.MapSizesColors
import com.yandex.mapkit.Animation
import com.yandex.mapkit.geometry.Circle
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CircleMapObject
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.MapObject
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import android.graphics.Color as AndroidColor

class MapHelper {

    companion object {
        /** Метки объявлений должны быть выше слоя геолокации (круг ~30 м перекрывает экран). */
        private const val USER_LAYER_Z_INDEX = 0f
        private const val PROPERTY_MARKERS_Z_INDEX = 5f
    }

    private var userPlacemark: PlacemarkMapObject? = null
    private var userCircle: CircleMapObject? = null
    private var cameraMoved: Boolean = false

    /** Отдельный слой объявлений (без кластеризации), целиком снимается с карты при очистке. */
    private var propertyMarkersLayer: MapObjectCollection? = null

    private var detailPlacemark: PlacemarkMapObject? = null
    private val priceMarkerIconCache = mutableMapOf<String, ImageProvider>()

    fun updateUserLocation(
        mapView: MapView,
        location: UserLocation,
        moveCameraOnFirstFix: Boolean = true
    ) {
        val point = Point(location.lat, location.lon)

        if (userPlacemark == null || userCircle == null) {
            val objects = createUserMapObjects(mapView, point)
            userPlacemark = objects.first.apply { zIndex = USER_LAYER_Z_INDEX }
            userCircle = objects.second.apply { zIndex = USER_LAYER_Z_INDEX }
        }

        cameraMoved = updateUserLocationOnMap(
            mapView,
            location,
            cameraMoved,
            userCircle,
            userPlacemark,
            moveCameraOnFirstFix
        )
    }

    fun showPropertyMarkers(
        mapView: MapView,
        markers: List<Property>,
        onMarkerTap: (Property) -> Unit
    ) {
        clearPropertyMarkersLayer(mapView)
        if (markers.isEmpty()) return

        val layer = mapView.map.mapObjects.addCollection()
        propertyMarkersLayer = layer
        // Выше круга/метки геолокации, иначе большой круг перехватывает все тапы по карте.
        layer.zIndex = PROPERTY_MARKERS_Z_INDEX

        markers.forEach { marker ->
            val priceLabel = marker.buildMarkerPriceLabel()
            val icon = priceMarkerIconCache.getOrPut(priceLabel) {
                createPriceMarkerIcon(
                    context = mapView.context,
                    text = priceLabel,
                )
            }

            val placemark = layer.addPlacemark(
                Point(marker.latitude, marker.longitude),
                icon,
                IconStyle().apply {
                    anchor = PointF(0.5f, 1f)
                },
            )
            placemark.zIndex = PROPERTY_MARKERS_Z_INDEX
            placemark.userData = marker
            placemark.addTapListener(
                object : MapObjectTapListener {
                    override fun onMapObjectTap(mapObject: MapObject, point: Point): Boolean {
                        mapView.post {
                            onMarkerTap(marker)
                        }
                        return true
                    }
                }
            )
        }
    }

    fun clearMarkers(mapView: MapView) {
        clearPropertyMarkersLayer(mapView)
    }

    private fun clearPropertyMarkersLayer(mapView: MapView) {
        propertyMarkersLayer?.let { layer ->
            mapView.map.mapObjects.remove(layer)
            propertyMarkersLayer = null
        }
        priceMarkerIconCache.clear()
    }

    /**
     * Одна метка объекта + плавное позиционирование камеры.
     * Для превью и полноэкранной карты в карточке объекта.
     */
    fun showSinglePropertyMarker(mapView: MapView, property: Property) {
        clearSinglePropertyMarker(mapView)
        clearPropertyMarkersLayer(mapView)

        val icon = createMarkerIcon(mapView.context)
        detailPlacemark = mapView.map.mapObjects.addPlacemark(
            Point(property.latitude, property.longitude),
            icon
        )

        mapView.map.move(
            CameraPosition(
                Point(property.latitude, property.longitude),
                MapSizesColors.PROPERTY_DETAIL_MAP_ZOOM,
                0f,
                0f
            ),
            Animation(Animation.Type.SMOOTH, MapSizesColors.CAMERA_MOVE_ANIMATION_DURATION_SEC),
            null
        )
    }

    fun moveCameraTo(
        mapView: MapView,
        latitude: Double,
        longitude: Double,
        zoom: Float = MapSizesColors.PROPERTY_DETAIL_MAP_ZOOM,
    ) {
        mapView.map.move(
            CameraPosition(
                Point(latitude, longitude),
                zoom,
                0f,
                0f
            ),
            Animation(Animation.Type.SMOOTH, MapSizesColors.CAMERA_MOVE_ANIMATION_DURATION_SEC),
            null
        )
    }

    fun clearSinglePropertyMarker(mapView: MapView) {
        detailPlacemark?.let { placemark ->
            mapView.map.mapObjects.remove(placemark)
            detailPlacemark = null
        }
    }

    fun release(mapView: MapView) {
        clearSinglePropertyMarker(mapView)
        clearPropertyMarkersLayer(mapView)
    }

    /** Иконка метки `point` для одиночных пинов (карта выбора адреса и т.п.). */
    fun propertyMarkerIcon(context: Context): ImageProvider = createMarkerIcon(context)

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

    private fun createPriceMarkerIcon(
        context: Context,
        text: String,
    ): ImageProvider {
        val density = context.resources.displayMetrics.density
        val textSizePx = 13f * density
        val horizontalPadding = 10f * density
        val verticalPadding = 6f * density
        val pointerHeight = 8f * density
        val pointerHalfWidth = 7f * density
        val cornerRadius = 12f * density
        val strokeWidth = 1f * density
        val minBubbleWidth = 58f * density

        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = AndroidColor.parseColor("#111827")
            textSize = textSizePx
            typeface = android.graphics.Typeface.DEFAULT_BOLD
        }
        val fontMetrics = textPaint.fontMetrics
        val textWidth = textPaint.measureText(text)
        val textHeight = fontMetrics.bottom - fontMetrics.top

        val bubbleWidth = maxOf(minBubbleWidth, textWidth + horizontalPadding * 2f)
        val bubbleHeight = textHeight + verticalPadding * 2f
        val bitmapWidth = kotlin.math.ceil(bubbleWidth).toInt()
        val bitmapHeight = kotlin.math.ceil(bubbleHeight + pointerHeight).toInt()

        val bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val bubbleRect = RectF(
            0f,
            0f,
            bubbleWidth,
            bubbleHeight,
        )
        val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = AndroidColor.WHITE
            style = Paint.Style.FILL
        }
        val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = AndroidColor.parseColor("#D1D5DB")
            style = Paint.Style.STROKE
            this.strokeWidth = strokeWidth
        }

        canvas.drawRoundRect(
            bubbleRect,
            cornerRadius,
            cornerRadius,
            fillPaint,
        )
        canvas.drawRoundRect(
            bubbleRect,
            cornerRadius,
            cornerRadius,
            strokePaint,
        )

        val centerX = bubbleWidth / 2f
        val pointerTopY = bubbleHeight - 1f
        val pointerPath = Path().apply {
            moveTo(centerX - pointerHalfWidth, pointerTopY)
            lineTo(centerX + pointerHalfWidth, pointerTopY)
            lineTo(centerX, bubbleHeight + pointerHeight)
            close()
        }
        canvas.drawPath(pointerPath, fillPaint)
        canvas.drawPath(pointerPath, strokePaint)

        val textX = (bubbleWidth - textWidth) / 2f
        val textY = bubbleHeight / 2f - (fontMetrics.ascent + fontMetrics.descent) / 2f
        canvas.drawText(text, textX, textY, textPaint)

        return ImageProvider.fromBitmap(bitmap)
    }

    private fun Property.buildMarkerPriceLabel(): String {
        return "${formatPrice(price)} ${currency.shortMarkerSymbol()}"
    }

    private fun CurrencyType.shortMarkerSymbol(): String {
        return when (this) {
            CurrencyType.USD -> "$"
            CurrencyType.EUR -> "EUR"
            CurrencyType.BYN -> "BYN"
        }
    }

    private fun updateUserLocationOnMap(
        mapView: MapView,
        location: UserLocation,
        cameraMoved: Boolean,
        userCircle: CircleMapObject?,
        userPlacemark: PlacemarkMapObject?,
        moveCameraOnFirstFix: Boolean = true
    ): Boolean {

        val point = Point(location.lat, location.lon)

        var cameraWasMoved = cameraMoved

        if (!cameraWasMoved) {
            if (moveCameraOnFirstFix) {
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
            }
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

        canvas.drawCircle(cx, cy, MapSizesColors.USER_MARKER_WHITE_CIRCLE_RADIUS, whitePaint)

        canvas.drawCircle(cx, cy, MapSizesColors.USER_MARKER_BLUE_CIRCLE_RADIUS, bluePaint)

        val path = Path()
        path.moveTo(cx, cy - MapSizesColors.USER_MARKER_ARROW_TOP_OFFSET)
        path.lineTo(cx - MapSizesColors.USER_MARKER_ARROW_SIDE_OFFSET, cy - 12f)
        path.lineTo(cx + MapSizesColors.USER_MARKER_ARROW_SIDE_OFFSET, cy - 12f)
        path.close()

        val matrix = Matrix()
        matrix.postRotate(bearing, cx, cy)
        path.transform(matrix)

        canvas.drawPath(path, bluePaint)

        return bitmap
    }
}
