package com.example.propertymanagement.ui.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import androidx.core.content.ContextCompat
import com.example.propertymanagement.R
import com.example.propertymanagement.domain.model.NearbyMapPoi
import com.example.propertymanagement.domain.model.NearbyPoiCategory
import com.example.propertymanagement.ui.theme.MapPoiLayerColors
import com.example.propertymanagement.ui.theme.MapSizesColors
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.MapObject
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import android.graphics.Color as AndroidColor

/**
 * POI (OSM) на карте объявления: при отдалении — точки, при приближении — badge-иконки.
 */
class OsmMapPoiMarkersLayer {

    private var collection: MapObjectCollection? = null

    private val iconCache = mutableMapOf<IconCacheKey, ImageProvider>()

    private var lastPois: List<NearbyMapPoi> = emptyList()
    private var lastOnTap: ((NearbyMapPoi) -> Unit)? = null
    private var lastDetailedStyle: Boolean? = null
    private var nightModeEnabled: Boolean = false

    fun clear(mapView: MapView) {
        collection?.let { layer ->
            mapView.map.mapObjects.remove(layer)
            collection = null
        }
    }

    fun applyNightMode(mapView: MapView, enabled: Boolean) {
        if (nightModeEnabled == enabled) {
            return
        }
        nightModeEnabled = enabled
        iconCache.clear()
        if (lastPois.isNotEmpty()) {
            rebuild(mapView)
        }
    }

    fun update(
        mapView: MapView,
        pois: List<NearbyMapPoi>,
        onPoiTap: (NearbyMapPoi) -> Unit,
    ) {
        lastPois = pois
        lastOnTap = onPoiTap
        rebuild(mapView)
    }

    fun onZoomLevelChanged(mapView: MapView, zoom: Float) {
        val detailed = zoom >= MapSizesColors.POI_MARKER_DETAILED_ICON_MIN_ZOOM
        if (detailed == lastDetailedStyle) {
            return
        }
        if (lastPois.isEmpty()) {
            return
        }
        rebuild(mapView)
    }

    private fun rebuild(mapView: MapView) {
        clear(mapView)
        if (lastPois.isEmpty()) {
            lastDetailedStyle = null
            return
        }
        val zoom = mapView.map.cameraPosition.zoom
        val detailed = zoom >= MapSizesColors.POI_MARKER_DETAILED_ICON_MIN_ZOOM
        lastDetailedStyle = detailed

        val ctx = mapView.context
        val layer = mapView.map.mapObjects.addCollection()
        collection = layer
        layer.zIndex = POI_LAYER_Z_INDEX

        val onTap = lastOnTap ?: return

        lastPois.forEach { poi ->
            val icon = iconForCategory(
                context = ctx,
                category = poi.category,
                detailed = detailed,
            )
            val placemark = layer.addPlacemark(
                Point(poi.latitude, poi.longitude),
                icon,
                IconStyle().apply { anchor = PointF(0.5f, 0.5f) },
            )
            placemark.userData = poi
            placemark.addTapListener(
                object : MapObjectTapListener {
                    override fun onMapObjectTap(mapObject: MapObject, point: Point): Boolean {
                        mapView.post {
                            onTap(poi)
                        }
                        return true
                    }
                },
            )
        }
    }

    private fun iconForCategory(
        context: Context,
        category: NearbyPoiCategory,
        detailed: Boolean,
    ): ImageProvider {
        val key = IconCacheKey(
            category = category,
            detailed = detailed,
            nightMode = nightModeEnabled,
        )
        return iconCache.getOrPut(key) {
            if (detailed) {
                createDetailedBadgeIcon(
                    context = context,
                    category = category,
                    nightMode = nightModeEnabled,
                )
            } else {
                createDotIcon(
                    context = context,
                    fillArgb = MapPoiLayerColors.fillArgb(category),
                    nightMode = nightModeEnabled,
                )
            }
        }
    }

    private fun createDetailedBadgeIcon(
        context: Context,
        category: NearbyPoiCategory,
        nightMode: Boolean,
    ): ImageProvider {
        val density = context.resources.displayMetrics.density
        val sizePx = (BADGE_DP * density).toInt().coerceAtLeast(28)
        val strokeWidth = 1.5f * density
        val iconPx = (BADGE_ICON_DP * density).toInt().coerceAtLeast(16)

        val bitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val cx = sizePx / 2f
        val cy = sizePx / 2f
        val radius = (sizePx / 2f - strokeWidth).coerceAtLeast(8f)

        val accent = MapPoiLayerColors.fillArgb(category)
        val fillColor = if (nightMode) {
            MapPoiLayerColors.markerBadgeBackgroundNightArgb(category)
        } else {
            MapPoiLayerColors.markerBadgeBackgroundDayArgb()
        }
        val iconTint = if (nightMode) {
            AndroidColor.WHITE
        } else {
            accent
        }

        val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = fillColor
            style = Paint.Style.FILL
        }
        val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = accent
            style = Paint.Style.STROKE
            this.strokeWidth = strokeWidth
        }
        canvas.drawCircle(cx, cy, radius, fillPaint)
        canvas.drawCircle(cx, cy, radius, strokePaint)

        drawCategoryIcon(
            context = context,
            category = category,
            canvas = canvas,
            centerX = cx,
            centerY = cy,
            targetPx = iconPx,
            tint = iconTint,
        )

        return ImageProvider.fromBitmap(bitmap)
    }

    private fun drawCategoryIcon(
        context: Context,
        category: NearbyPoiCategory,
        canvas: Canvas,
        centerX: Float,
        centerY: Float,
        targetPx: Int,
        tint: Int,
    ) {
        val resId = when (category) {
            NearbyPoiCategory.SCHOOL -> R.drawable.school_icon
            NearbyPoiCategory.POLYCLINIC -> R.drawable.clinick_icon
            NearbyPoiCategory.GROCERY -> R.drawable.shop_icon
        }
        val drawable = ContextCompat.getDrawable(context, resId)?.mutate() ?: return

        val sourceW = drawable.intrinsicWidth.takeIf { it > 0 } ?: targetPx
        val sourceH = drawable.intrinsicHeight.takeIf { it > 0 } ?: targetPx
        val sourceBitmap = Bitmap.createBitmap(sourceW, sourceH, Bitmap.Config.ARGB_8888)
        val sourceCanvas = Canvas(sourceBitmap)
        drawable.setBounds(0, 0, sourceW, sourceH)
        drawable.draw(sourceCanvas)

        val scaled = Bitmap.createScaledBitmap(sourceBitmap, targetPx, targetPx, true)
        if (scaled != sourceBitmap) {
            sourceBitmap.recycle()
        }

        val tinted = Bitmap.createBitmap(targetPx, targetPx, Bitmap.Config.ARGB_8888)
        val tintCanvas = Canvas(tinted)
        val tintPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            colorFilter = PorterDuffColorFilter(tint, PorterDuff.Mode.SRC_IN)
        }
        tintCanvas.drawBitmap(scaled, 0f, 0f, tintPaint)
        scaled.recycle()

        val left = centerX - targetPx / 2f
        val top = centerY - targetPx / 2f
        canvas.drawBitmap(tinted, left, top, null)
        tinted.recycle()
    }

    private fun createDotIcon(
        context: Context,
        fillArgb: Int,
        nightMode: Boolean,
    ): ImageProvider {
        val density = context.resources.displayMetrics.density
        val sizePx = (DOT_DP * density).toInt().coerceAtLeast(8)
        val strokeWidth = 1f * density
        val bitmap = Bitmap.createBitmap(sizePx, sizePx, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val cx = sizePx / 2f
        val cy = sizePx / 2f
        val radius = (sizePx / 2f - strokeWidth / 2f).coerceAtLeast(2.5f)
        val strokeColor = if (nightMode) {
            MapPoiLayerColors.markerDotStrokeNightArgb()
        } else {
            AndroidColor.WHITE
        }
        val fill = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = fillArgb
            style = Paint.Style.FILL
        }
        val stroke = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = strokeColor
            style = Paint.Style.STROKE
            this.strokeWidth = strokeWidth
        }
        canvas.drawCircle(cx, cy, radius, fill)
        canvas.drawCircle(cx, cy, radius, stroke)
        return ImageProvider.fromBitmap(bitmap)
    }

    private data class IconCacheKey(
        val category: NearbyPoiCategory,
        val detailed: Boolean,
        val nightMode: Boolean,
    )

    companion object {
        private const val POI_LAYER_Z_INDEX = 2f
        private const val DOT_DP = 10.5f
        private const val BADGE_DP = 34f
        private const val BADGE_ICON_DP = 18f
    }
}
