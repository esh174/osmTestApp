package com.example.myapplication.ui.pinPointMap

import android.location.Location
import com.example.myapplication.domain.entity.PointLocation
import com.example.myapplication.domain.entity.UserLocation

data class PinPointMapState(
    val isLoading: Boolean = false,
    val currentLocation: PointLocation? = null,
    val locations: List<UserLocation> = emptyList(),
    val selectedLocation: UserLocation? = null,
    val isInfoBoxVisible: Boolean = false
)