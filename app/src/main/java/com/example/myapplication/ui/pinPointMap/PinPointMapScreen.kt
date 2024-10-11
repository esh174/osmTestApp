package com.example.myapplication.ui.pinPointMap

import android.Manifest
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myapplication.ui.components.loadingIndicators.LoadingView
import com.example.myapplication.ui.components.mapView.MapView
import com.example.myapplication.ui.components.mapView.rememberMapViewWithLifecycle
import com.example.myapplication.ui.pinPointMap.components.MapControls
import com.example.myapplication.ui.pinPointMap.components.UserInfoBox
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.launch


@Composable
fun PinPointMapScreen(
    viewModel: PinPointMapViewModel,
    navController: NavController,
) {
    val screenState by viewModel.screenStateFlow.collectAsState(null)
    PinPointMapScreen(
        screenState = screenState,
        onScreenEvent = viewModel::onScreenEvent
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun PinPointMapScreen(
    screenState: PinPointMapState?,
    onScreenEvent: (PinPointMapEvent) -> Unit = {}
) {
    val context = LocalContext.current
    val locationPermissionState = rememberPermissionState(
        permission = Manifest.permission.ACCESS_FINE_LOCATION,
        onPermissionResult = { isGranted ->
            if (isGranted) onScreenEvent(PinPointMapEvent.LocationPermissionGranted)
        }
    )

    val mapViewState = rememberMapViewWithLifecycle()
    var selectedLocationIndex by remember {
        mutableIntStateOf(0)
    }

    var startPositioned by rememberSaveable { mutableStateOf(false) }

    if (!startPositioned) {
        LaunchedEffect(screenState?.currentLocation) {
            if (screenState?.currentLocation != null) {
                startPositioned = true
                mapViewState.controller.setCenter(screenState.currentLocation.geoPoint)
            }
        }
    }

    LaunchedEffect(Unit) {
        when {
            locationPermissionState.status.isGranted -> {
                onScreenEvent(PinPointMapEvent.LocationPermissionGranted)
            }

            locationPermissionState.status.shouldShowRationale && !locationPermissionState.status.isGranted -> {
                Toast.makeText(
                    context,
                    "Необходимо разрешить отслеживание положения в настройках",
                    Toast.LENGTH_LONG
                ).show()
            }

            else -> {
                locationPermissionState.launchPermissionRequest()
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        if (screenState != null) {
            MapView(
                modifier = Modifier.navigationBarsPadding(),
                mapViewState = mapViewState,
                userPositionMarker = screenState.currentLocation,
                markers = screenState.locations,
                onUserLocationClick = {
                    mapViewState.controller.animateTo(
                        it.location.geoPoint,
                        mapViewState.zoomLevelDouble,
                        200
                    )

                    onScreenEvent(PinPointMapEvent.LocationSelected(it))
                }
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding(),
            ) {
                MapControls(
                    modifier = Modifier.align(Alignment.TopEnd),
                    isUserLocationVisible = screenState.currentLocation != null,
                    isNextLocationVisible = screenState.locations.isNotEmpty(),
                    onZoomInClick = {
                        mapViewState.controller.zoomTo(
                            mapViewState.zoomLevelDouble + 1,
                            300
                        )
                    },
                    onZoomOutClick = {
                        mapViewState.controller.zoomTo(
                            mapViewState.zoomLevelDouble - 1,
                            300
                        )
                    },
                    onLocateUserClick = {
                        if (screenState.currentLocation != null) {
                            onScreenEvent(PinPointMapEvent.LocateUserClicked)
                            mapViewState.controller.stopPanning()
                            mapViewState.controller.animateTo(
                                screenState.currentLocation.geoPoint,
                                mapViewState.zoomLevelDouble,
                                300
                            )
                        }
                    },
                    onNextLocationClick = {
                        when {
                            screenState.locations.size > 2 -> {
                                if (selectedLocationIndex + 1 > screenState.locations.lastIndex) {
                                    selectedLocationIndex = 0

                                } else {
                                    selectedLocationIndex++
                                }

                                val selectedLocation = screenState.locations[selectedLocationIndex]

                                onScreenEvent(PinPointMapEvent.LocationSelected(selectedLocation))
                                mapViewState.controller.stopPanning()
                                mapViewState.controller.animateTo(
                                    selectedLocation.location.geoPoint,
                                    mapViewState.zoomLevelDouble,
                                    300
                                )
                            }

                            screenState.locations.size == 1 -> {
                                mapViewState.controller.stopPanning()
                                mapViewState.controller.animateTo(
                                    screenState.locations.firstOrNull()?.location?.geoPoint,
                                    mapViewState.zoomLevelDouble,
                                    300
                                )
                            }

                            else -> {}
                        }
                    }
                )

                if (screenState.selectedLocation != null) {
                    AnimatedVisibility(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter),
                        visible = screenState.isInfoBoxVisible,
                        enter = slideInVertically(
                            animationSpec = tween(300),
                            initialOffsetY = { it }
                        ),
                        exit = slideOutVertically(
                            animationSpec = tween(300),
                            targetOffsetY = { it }
                        ),
                    ) {
                        UserInfoBox(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.White)
                                .padding(16.dp),
                            userLocation = screenState.selectedLocation
                        )
                    }
                }
            }
        }

        LoadingView(
            modifier = Modifier.fillMaxSize(),
            visible = screenState == null || screenState.isLoading
        )
    }
}

