package com.example.myapplication.ui.components.mapView

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.myapplication.domain.entity.PointLocation
import com.example.myapplication.domain.entity.UserLocation
import org.osmdroid.views.MapView


@Composable
fun MapView(
    modifier: Modifier = Modifier,
    mapViewState: MapView = rememberMapViewWithLifecycle(),
    userPositionMarker: PointLocation?,
    markers: List<UserLocation>,
    onLoad: ((map: MapView) -> Unit)? = null,
    onUserLocationClick: (UserLocation) -> Unit = {},
) {
    val context = LocalContext.current
    LaunchedEffect(markers, onUserLocationClick) {
        mapViewState.updateUserMarkers(
            context = context,
            markers = markers,
            onUserLocationClick = onUserLocationClick
        )
    }

    if (userPositionMarker != null) {
        LaunchedEffect(userPositionMarker) {
            mapViewState.updatePositionMarker(
                context = context.applicationContext,
                marker = userPositionMarker
            )
        }
    }

    AndroidView(
        factory = { mapViewState },
        modifier = modifier
    ) { mapView ->
        onLoad?.invoke(mapView)
    }
}