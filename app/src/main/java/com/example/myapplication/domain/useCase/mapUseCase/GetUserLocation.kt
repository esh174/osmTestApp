package com.example.myapplication.domain.useCase.mapUseCase

import com.example.myapplication.constants.Constants
import com.example.myapplication.constants.MarkerTypes
import com.example.myapplication.domain.LocationManager
import com.example.myapplication.domain.entity.PointLocation
import com.example.myapplication.domain.entity.RequestResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetUserLocation(
    private val locationManager: LocationManager
) {
    operator fun invoke(): RequestResult<Flow<PointLocation>> {
        if(!locationManager.hasLocationPermission()) {
            return RequestResult.Error(
                message = "Location permission required"
            )
        }

        val locationFlow = locationManager.getLocationFlow()?.map {
            PointLocation(
                id = Constants.MARKER_USER_ID,
                type = MarkerTypes.MARKER_TYPE_USER_POSITION,
                latitude = it.latitude,
                longitude = it.longitude
            )
        }?: return RequestResult.Error(
                message = "Location manager error"
            )

        return RequestResult.Success(locationFlow)
    }
}