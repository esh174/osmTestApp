package com.example.myapplication.ui.pinPointMap

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.entity.RequestResult
import com.example.myapplication.domain.useCase.mapUseCase.MapUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PinPointMapViewModel @Inject constructor(
    private val mapUseCases: MapUseCases,
) : ViewModel() {
    private val _screenStateFlow = MutableStateFlow(PinPointMapState())
    var screenStateFlow = _screenStateFlow.asStateFlow().filterNotNull()

    init {
        viewModelScope.launch {
            launch { getLocation() }
            launch { getMapPointers() }
        }
    }

    fun onScreenEvent(event: PinPointMapEvent) {
        viewModelScope.launch {
            when (event) {
                is PinPointMapEvent.LocationSelected -> {
                    _screenStateFlow.update {
                        it.copy(
                            isInfoBoxVisible = when (event.userLocation) {
                                it.selectedLocation -> {
                                    !it.isInfoBoxVisible
                                }

                                else -> true
                            },
                            selectedLocation = event.userLocation
                        )
                    }
                }

                PinPointMapEvent.LocationPermissionGranted -> {
                    getLocation()
                }

                PinPointMapEvent.LocateUserClicked -> {
                    _screenStateFlow.update { it.copy(isInfoBoxVisible = false) }
                }
            }
        }
    }

    private suspend fun getMapPointers() {
        val locations = mapUseCases.getLocations()

        _screenStateFlow.update {
            it.copy(
                locations = locations,
                selectedLocation = locations.firstOrNull()
            )
        }
    }

    private suspend fun getLocation() {
        coroutineScope {
            launch {
                when (val response = mapUseCases.getUserLocation()) {
                    is RequestResult.Error -> {}
                    is RequestResult.Success -> {
                        response.result
                            .onEach { location ->
                                _screenStateFlow.update { it.copy(currentLocation = location) }
                            }.collect()
                    }
                }
            }
        }
    }
}