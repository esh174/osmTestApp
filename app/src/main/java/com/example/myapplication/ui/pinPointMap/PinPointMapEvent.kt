package com.example.myapplication.ui.pinPointMap

import com.example.myapplication.domain.entity.UserLocation

sealed class PinPointMapEvent  {
    data object LocateUserClicked: PinPointMapEvent()
    data class LocationSelected(val userLocation: UserLocation): PinPointMapEvent()
    data object LocationPermissionGranted: PinPointMapEvent()
}