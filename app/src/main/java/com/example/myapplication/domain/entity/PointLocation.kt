package com.example.myapplication.domain.entity

import androidx.compose.runtime.Immutable
import com.example.myapplication.constants.MarkerTypes
import org.osmdroid.util.GeoPoint

@Immutable
data class PointLocation(
    val id: String,
    val type: MarkerTypes,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String? = null
) {
    val geoPoint: GeoPoint
        get() = GeoPoint(latitude, longitude)
}