package com.example.myapplication.ui.components.mapView

import android.content.Context
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.example.myapplication.constants.Constants
import com.example.myapplication.domain.entity.PointLocation
import com.example.myapplication.domain.entity.UserLocation
import com.example.myapplication.ui.components.mapView.marker.UserMarker
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import kotlin.math.roundToInt

fun MapView.updateUserMarkers(
    context: Context,
    markers: List<UserLocation>,
    onUserLocationClick: (UserLocation) -> Unit = {},
) {
    val newMarkers: MutableList<UserMarker> = mutableListOf()
    val currentMarkers = this.overlays.filterIsInstance<UserMarker>()
    val removedMarkers = currentMarkers.filterNot { currentMarker ->
        markers.any { currentMarker.id == it.id }
    }

    removedMarkers.forEach { marker ->
        if (marker.id.contains(Constants.MARKER_USER_ID)) return@forEach
        this.overlays.remove(marker)
    }

    markers.forEach { updatedMarker ->
        val marker = currentMarkers.find { it.id == updatedMarker.id }
        if (marker == null) {
            newMarkers.add(
                this.createMapMarker(
                    id = updatedMarker.id,
                    icon = context.getDrawable(MarkerType.getByTypeId(updatedMarker.location.type.id).iconId),
                    imageUrl = updatedMarker.avatarUrl,
                    label = updatedMarker.name,
                    text = " ${updatedMarker.signalType}, ${updatedMarker.lastActive}",
                    position = updatedMarker.location.geoPoint,
                    context = context,
                    onUserLocationClick = { onUserLocationClick(updatedMarker) }
                )
            )

            return@forEach
        }

        marker.position = updatedMarker.location.geoPoint
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.setOnMarkerClickListener { _, _ ->
            onUserLocationClick(updatedMarker)
            true
        }
    }

    newMarkers.forEach { marker ->
        this.overlays.add(marker)
    }
}

fun MapView.updatePositionMarker(
    context: Context,
    marker: PointLocation
) {
    val currentMarkers = this.overlays.filterIsInstance<Marker>()
    val currentPositionMarker = currentMarkers.find { it.id == marker.id }

    if (currentPositionMarker == null) {
        val userMarker = this.createMapMarker(
            id = marker.id,
            icon = context.getDrawable(MarkerType.getByTypeId(marker.type.id).iconId),
            position = marker.geoPoint,
            context = context,
        )

        this.overlays.add(userMarker)
        return
    }

    currentPositionMarker.position = marker.geoPoint
    currentPositionMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
}

fun MapView.createMapMarker(
    id: String,
    icon: Drawable?,
    label: String? = null,
    text: String? = null,
    imageUrl: String? = null,
    position: GeoPoint,
    context: Context,
    onUserLocationClick: () -> Unit = {}
): UserMarker {
    val density = context.resources.displayMetrics.density

    val userMarker = UserMarker(
        this
    ).apply {
        this.id = id
        this.icon = icon
        this.position = position
        this.setOnMarkerClickListener { _, _ ->
            onUserLocationClick()
            true
        }

        this.setLabel(label)
        this.setAdditionalText(text)

        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
    }

    if (imageUrl != null) {
        Glide.with(context)
            .asDrawable()
            .load(imageUrl)
            .override((50 * density).roundToInt(), (50 * density).roundToInt())
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .listener(
                object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                }
            )
            .into(
                object : CustomTarget<Drawable>() {
                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable>?,
                    ) {
                        userMarker.setImageDrawable(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {}
                }
            )
    }

    return userMarker
}