package com.example.myapplication.domain.useCase.mapUseCase

import com.example.myapplication.constants.Constants
import com.example.myapplication.constants.MarkerTypes
import com.example.myapplication.domain.entity.PointLocation
import com.example.myapplication.domain.entity.UserLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.osmdroid.util.GeoPoint

class GetLocations {
    suspend operator fun invoke(): List<UserLocation> = withContext(Dispatchers.IO) {
        delay(300)
        return@withContext listOf(
            UserLocation(
                id = "mocked_id1",
                name = "Фаркуад",
                lastActive = "13:00",
                signalType = "GPS",
                birthdate = "01.01.2003",
                avatarUrl = "https://static.wikia.nocookie.net/shrek/images/f/fe/Some_of_you_may_die_farquaad.png",
                location =  PointLocation(
                    "mocked_id1",
                    MarkerTypes.MARKER_TYPE_ORDINARY_POSITION,
                    latitude = 10.0,
                    longitude = 40.0
                ),
            ),
            UserLocation(
                id = "mocked_id2",
                name = "Шрек",
                lastActive = "12:00",
                signalType = "GPS",
                birthdate = "01.01.2002",
                avatarUrl = "https://static.wikia.nocookie.net/shrek/images/4/4f/Shrek_donkey_onions.jpg",
                location =    PointLocation(
                    "mocked_id2",
                    MarkerTypes.MARKER_TYPE_ORDINARY_POSITION,
                    latitude = 20.0,
                    longitude = 40.0
                ),
            ),
            UserLocation(
                id = "mocked_id3",
                name = "Кот в сапогах",
                lastActive = "11:00",
                birthdate = "01.01.2000",
                signalType = "GPS",
                avatarUrl = "https://static.wikia.nocookie.net/shrek/images/f/fc/ImagesCAF18L2Y.jpg",
                location =     PointLocation(
                    "mocked_id3",
                    MarkerTypes.MARKER_TYPE_ORDINARY_POSITION,
                    latitude = 30.0,
                    longitude = 40.0
                ),
            )
        )
    }
}