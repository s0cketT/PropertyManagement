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
import com.example.propertymanagement.domain.currency.convertAmountBetweenCurrencies
import com.example.propertymanagement.domain.model.CurrencyRate
import com.example.propertymanagement.domain.model.CurrencyType
import com.example.propertymanagement.domain.model.DealType
import com.example.propertymanagement.domain.model.Property
import com.example.propertymanagement.domain.model.UserLocation
import com.example.propertymanagement.domain.pricing.ManagerCommissionPricing
import com.example.propertymanagement.ui.common.formatPrice
import com.example.propertymanagement.ui.theme.MapSizesColors
import com.yandex.mapkit.Animation
import com.yandex.mapkit.geometry.Circle
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CameraUpdateReason
import com.yandex.mapkit.map.CircleMapObject
import com.yandex.mapkit.map.Map as YandexMap
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

        private const val SALE_CHEAP_USD = 30_000.0
        private const val SALE_EXPENSIVE_USD = 150_000.0
        private const val RENT_CHEAP_USD = 190.0
        private const val RENT_EXPENSIVE_USD = 1_000.0
    }

    private var userPlacemark: PlacemarkMapObject? = null
    private var userCircle: CircleMapObject? = null
    private var cameraMoved: Boolean = false

    /** Отдельный слой объявлений (без кластеризации), целиком снимается с карты при очистке. */
    private var propertyMarkersLayer: MapObjectCollection? = null

    private var detailPlacemark: PlacemarkMapObject? = null
    private val priceMarkerIconCache = mutableMapOf<String, ImageProvider>()

    private var mapViewForCamera: MapView? = null
    private var cameraListenerRegistered: Boolean = false
    private var lastRenderedPriceLabelMode: Boolean? = null
    private var cachedMarkerParams: MarkerParams? = null

    private data class MarkerParams(
        val markers: List<Property>,
        val currencyRates: Map<String, CurrencyRate>,
        val displayCurrency: CurrencyType,
        val managerCommissionPercent: Double,
        val onMarkerTap: (Property) -> Unit,
    )

    private val propertyMarkersCameraListener = object : CameraListener {
        override fun onCameraPositionChanged(
            map: YandexMap,
            cameraPosition: CameraPosition,
            cameraUpdateReason: CameraUpdateReason,
            finished: Boolean,
        ) {
            val mapView = mapViewForCamera ?: return
            val params = cachedMarkerParams ?: return
            val showPriceLabels = cameraPosition.zoom >= MapSizesColors.MAP_PRICE_LABEL_MIN_ZOOM
            if (showPriceLabels == lastRenderedPriceLabelMode) {
                return
            }
            rebuildPropertyMarkersLayer(
                mapView = mapView,
                params = params,
                showPriceLabels = showPriceLabels,
                clearIconCache = false,
            )
        }
    }

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
        currencyRates: Map<String, CurrencyRate>,
        displayCurrency: CurrencyType,
        managerCommissionPercent: Double = 0.0,
        onMarkerTap: (Property) -> Unit,
    ) {
        if (markers.isEmpty()) {
            cachedMarkerParams = null
            lastRenderedPriceLabelMode = null
            clearPropertyMarkersLayer(mapView, clearIconCache = true)
            return
        }

        val params = MarkerParams(
            markers = markers,
            currencyRates = currencyRates,
            displayCurrency = displayCurrency,
            managerCommissionPercent = managerCommissionPercent,
            onMarkerTap = onMarkerTap,
        )
        cachedMarkerParams = params
        ensurePropertyMarkersCameraListener(mapView)

        val showPriceLabels = mapView.map.cameraPosition.zoom >= MapSizesColors.MAP_PRICE_LABEL_MIN_ZOOM
        rebuildPropertyMarkersLayer(
            mapView = mapView,
            params = params,
            showPriceLabels = showPriceLabels,
            clearIconCache = true,
        )
    }

    fun zoomByDelta(mapView: MapView, delta: Float) {
        val pos = mapView.map.cameraPosition
        val newZoom = (pos.zoom + delta).coerceIn(
            MapSizesColors.MAP_ZOOM_MIN,
            MapSizesColors.MAP_ZOOM_MAX,
        )
        if (newZoom == pos.zoom) {
            return
        }

        mapView.map.move(
            CameraPosition(
                pos.target,
                newZoom,
                pos.azimuth,
                pos.tilt,
            ),
            Animation(Animation.Type.SMOOTH, MapSizesColors.MAP_ZOOM_BUTTON_ANIMATION_SEC),
            null,
        )
    }

    fun clearMarkers(mapView: MapView) {
        cachedMarkerParams = null
        lastRenderedPriceLabelMode = null
        clearPropertyMarkersLayer(mapView, clearIconCache = true)
    }

    private fun ensurePropertyMarkersCameraListener(mapView: MapView) {
        if (mapViewForCamera !== mapView) {
            mapViewForCamera?.let { previousMapView ->
                if (cameraListenerRegistered) {
                    previousMapView.map.removeCameraListener(propertyMarkersCameraListener)
                    cameraListenerRegistered = false
                }
            }
            mapViewForCamera = mapView
        }
        if (!cameraListenerRegistered) {
            mapView.map.addCameraListener(propertyMarkersCameraListener)
            cameraListenerRegistered = true
        }
    }

    private fun rebuildPropertyMarkersLayer(
        mapView: MapView,
        params: MarkerParams,
        showPriceLabels: Boolean,
        clearIconCache: Boolean,
    ) {
        clearPropertyMarkersLayer(mapView, clearIconCache = clearIconCache)
        lastRenderedPriceLabelMode = showPriceLabels

        val layer = mapView.map.mapObjects.addCollection()
        propertyMarkersLayer = layer
        layer.zIndex = PROPERTY_MARKERS_Z_INDEX

        val anchor = if (showPriceLabels) {
            PointF(0.5f, 1f)
        } else {
            PointF(0.5f, 0.5f)
        }

        params.markers.forEach { marker ->
            val markerStyle = marker.toPriceMarkerStyle(
                currencyRates = params.currencyRates,
                managerCommissionPercent = params.managerCommissionPercent,
            )
            val priceLabel = if (showPriceLabels) {
                marker.buildMarkerPriceLabel(
                    currencyRates = params.currencyRates,
                    displayCurrency = params.displayCurrency,
                    managerCommissionPercent = params.managerCommissionPercent,
                )
            } else {
                null
            }
            val iconCacheKey = if (showPriceLabels) {
                "price_${checkNotNull(priceLabel)}_${markerStyle.cacheKey}"
            } else {
                "dot_${markerStyle.cacheKey}"
            }
            val icon = priceMarkerIconCache.getOrPut(iconCacheKey) {
                if (showPriceLabels) {
                    createPriceMarkerIcon(
                        context = mapView.context,
                        text = checkNotNull(priceLabel),
                        backgroundColor = markerStyle.backgroundColor,
                        borderColor = markerStyle.borderColor,
                        textColor = markerStyle.textColor,
                    )
                } else {
                    createCompactDotMarkerIcon(
                        context = mapView.context,
                        fillColor = markerStyle.backgroundColor,
                        strokeColor = markerStyle.borderColor,
                    )
                }
            }

            val placemark = layer.addPlacemark(
                Point(marker.latitude, marker.longitude),
                icon,
                IconStyle().apply {
                    this.anchor = anchor
                },
            )
            placemark.zIndex = PROPERTY_MARKERS_Z_INDEX
            placemark.userData = marker
            placemark.addTapListener(
                object : MapObjectTapListener {
                    override fun onMapObjectTap(mapObject: MapObject, point: Point): Boolean {
                        mapView.post {
                            params.onMarkerTap(marker)
                        }
                        return true
                    }
                }
            )
        }
    }

    private fun clearPropertyMarkersLayer(mapView: MapView, clearIconCache: Boolean = true) {
        propertyMarkersLayer?.let { layer ->
            mapView.map.mapObjects.remove(layer)
            propertyMarkersLayer = null
        }
        if (clearIconCache) {
            priceMarkerIconCache.clear()
        }
    }

    /**
     * Одна метка объекта + плавное позиционирование камеры.
     * Для превью и полноэкранной карты в карточке объекта.
     */
    fun showSinglePropertyMarker(mapView: MapView, property: Property) {
        clearSinglePropertyMarker(mapView)
        cachedMarkerParams = null
        lastRenderedPriceLabelMode = null
        clearPropertyMarkersLayer(mapView, clearIconCache = true)

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
        if (cameraListenerRegistered) {
            mapView.map.removeCameraListener(propertyMarkersCameraListener)
            cameraListenerRegistered = false
        }
        mapViewForCamera = null
        cachedMarkerParams = null
        lastRenderedPriceLabelMode = null

        clearSinglePropertyMarker(mapView)
        clearPropertyMarkersLayer(mapView, clearIconCache = true)
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
        backgroundColor: Int,
        borderColor: Int,
        textColor: Int,
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
            color = textColor
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
            color = backgroundColor
            style = Paint.Style.FILL
        }
        val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = borderColor
            style = Paint.Style.STROKE
            this.strokeWidth = strokeWidth
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
        }

        val strokeInset = strokeWidth / 2f
        val bubbleStrokeRect = RectF(
            bubbleRect.left + strokeInset,
            bubbleRect.top + strokeInset,
            bubbleRect.right - strokeInset,
            bubbleRect.bottom - strokeInset,
        )

        canvas.drawRoundRect(
            bubbleRect,
            cornerRadius,
            cornerRadius,
            fillPaint,
        )
        canvas.drawRoundRect(
            bubbleStrokeRect,
            cornerRadius,
            cornerRadius,
            strokePaint,
        )

        val centerX = bubbleWidth / 2f
        val pointerTopY = bubbleHeight
        val pointerPath = Path().apply {
            moveTo(centerX - pointerHalfWidth, pointerTopY)
            lineTo(centerX + pointerHalfWidth, pointerTopY)
            lineTo(centerX, bubbleHeight + pointerHeight)
            close()
        }
        val pointerBorderPath = Path().apply {
            moveTo(centerX - pointerHalfWidth, pointerTopY)
            lineTo(centerX, bubbleHeight + pointerHeight - strokeInset)
            lineTo(centerX + pointerHalfWidth, pointerTopY)
        }
        canvas.drawPath(pointerPath, fillPaint)
        canvas.drawPath(pointerBorderPath, strokePaint)

        val textX = (bubbleWidth - textWidth) / 2f
        val textY = bubbleHeight / 2f - (fontMetrics.ascent + fontMetrics.descent) / 2f
        canvas.drawText(text, textX, textY, textPaint)

        return ImageProvider.fromBitmap(bitmap)
    }

    private fun createCompactDotMarkerIcon(
        context: Context,
        fillColor: Int,
        strokeColor: Int,
    ): ImageProvider {
        val density = context.resources.displayMetrics.density
        val sizePx = (12f * density).toInt().coerceAtLeast(8)
        val strokeWidth = 1f * density
        val bitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val cx = sizePx / 2f
        val cy = sizePx / 2f
        val radius = (sizePx / 2f - strokeWidth / 2f).coerceAtLeast(2f)
        val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = fillColor
            style = Paint.Style.FILL
        }
        val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = strokeColor
            style = Paint.Style.STROKE
            this.strokeWidth = strokeWidth
        }
        canvas.drawCircle(cx, cy, radius, fillPaint)
        canvas.drawCircle(cx, cy, radius, strokePaint)

        return ImageProvider.fromBitmap(bitmap)
    }

    private fun Property.buildMarkerPriceLabel(
        currencyRates: Map<String, CurrencyRate>,
        displayCurrency: CurrencyType,
        managerCommissionPercent: Double,
    ): String {
        val grossInListingCurrency = ManagerCommissionPricing.grossListingAmount(
            listedPrice = price,
            commissionPercent = managerCommissionPercent,
        )
        val displayPrice = if (currency == displayCurrency) {
            grossInListingCurrency
        } else {
            convertAmountBetweenCurrencies(
                amount = grossInListingCurrency,
                from = currency,
                to = displayCurrency,
                rates = currencyRates,
            )
        }
        return "${formatPrice(displayPrice)} ${displayCurrency.shortMarkerSymbol()}"
    }

    private fun CurrencyType.shortMarkerSymbol(): String {
        return when (this) {
            CurrencyType.USD -> "$"
            CurrencyType.EUR -> "EUR"
            CurrencyType.BYN -> "BYN"
        }
    }

    private fun Property.toPriceMarkerStyle(
        currencyRates: Map<String, CurrencyRate>,
        managerCommissionPercent: Double,
    ): PriceMarkerStyle {
        val priceUsd = toUsdPrice(
            currencyRates = currencyRates,
            managerCommissionPercent = managerCommissionPercent,
        )
        val normalized = when (dealType) {
            DealType.BUY -> normalizePrice(
                price = priceUsd,
                cheap = SALE_CHEAP_USD,
                expensive = SALE_EXPENSIVE_USD,
            )
            DealType.RENT -> normalizePrice(
                price = priceUsd,
                cheap = RENT_CHEAP_USD,
                expensive = RENT_EXPENSIVE_USD,
            )
        }

        val lightColor = when (dealType) {
            DealType.BUY -> AndroidColor.parseColor("#DBEAFE")
            DealType.RENT -> AndroidColor.parseColor("#FFEDD5")
        }
        val darkColor = when (dealType) {
            DealType.BUY -> AndroidColor.parseColor("#1D4ED8")
            DealType.RENT -> AndroidColor.parseColor("#C2410C")
        }
        val borderLightColor = when (dealType) {
            DealType.BUY -> AndroidColor.parseColor("#93C5FD")
            DealType.RENT -> AndroidColor.parseColor("#FDBA74")
        }
        val borderDarkColor = when (dealType) {
            DealType.BUY -> AndroidColor.parseColor("#1E40AF")
            DealType.RENT -> AndroidColor.parseColor("#9A3412")
        }

        return PriceMarkerStyle(
            backgroundColor = interpolateColor(
                startColor = lightColor,
                endColor = darkColor,
                fraction = normalized,
            ),
            borderColor = interpolateColor(
                startColor = borderLightColor,
                endColor = borderDarkColor,
                fraction = normalized,
            ),
            textColor = if (normalized > 0.55f) {
                AndroidColor.WHITE
            } else {
                AndroidColor.parseColor("#111827")
            },
            cacheKey = "${dealType.name}_${(normalized * 100).toInt()}",
        )
    }

    private fun Property.toUsdPrice(
        currencyRates: Map<String, CurrencyRate>,
        managerCommissionPercent: Double,
    ): Double {
        val grossInListingCurrency = ManagerCommissionPricing.grossListingAmount(
            listedPrice = price,
            commissionPercent = managerCommissionPercent,
        )
        if (currency == CurrencyType.USD) {
            return grossInListingCurrency
        }

        val hasUsdRate = currencyRates[CurrencyType.USD.name] != null
        val hasCurrentRate = currencyRates[currency.name] != null || currency == CurrencyType.BYN
        if (!hasUsdRate || !hasCurrentRate) {
            return grossInListingCurrency
        }

        return convertAmountBetweenCurrencies(
            amount = grossInListingCurrency,
            from = currency,
            to = CurrencyType.USD,
            rates = currencyRates,
        )
    }

    private fun normalizePrice(price: Double, cheap: Double, expensive: Double): Float {
        if (expensive <= cheap) {
            return 0f
        }

        return ((price - cheap) / (expensive - cheap))
            .coerceIn(0.0, 1.0)
            .toFloat()
    }

    private fun interpolateColor(
        startColor: Int,
        endColor: Int,
        fraction: Float,
    ): Int {
        val clamped = fraction.coerceIn(0f, 1f)
        val startA = AndroidColor.alpha(startColor)
        val startR = AndroidColor.red(startColor)
        val startG = AndroidColor.green(startColor)
        val startB = AndroidColor.blue(startColor)
        val endA = AndroidColor.alpha(endColor)
        val endR = AndroidColor.red(endColor)
        val endG = AndroidColor.green(endColor)
        val endB = AndroidColor.blue(endColor)

        val a = (startA + ((endA - startA) * clamped)).toInt()
        val r = (startR + ((endR - startR) * clamped)).toInt()
        val g = (startG + ((endG - startG) * clamped)).toInt()
        val b = (startB + ((endB - startB) * clamped)).toInt()
        return AndroidColor.argb(a, r, g, b)
    }

    private data class PriceMarkerStyle(
        val backgroundColor: Int,
        val borderColor: Int,
        val textColor: Int,
        val cacheKey: String,
    )

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
