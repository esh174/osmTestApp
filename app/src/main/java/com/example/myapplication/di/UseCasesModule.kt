package com.example.myapplication.di

import com.example.myapplication.domain.LocationManager
import com.example.myapplication.domain.useCase.mapUseCase.GetLocations
import com.example.myapplication.domain.useCase.mapUseCase.GetUserLocation
import com.example.myapplication.domain.useCase.mapUseCase.MapUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCasesModule {
    @Provides
    @Singleton
    fun provideMapUseCases(
        locationManager: LocationManager
    ): MapUseCases {
        return MapUseCases(
            getUserLocation = GetUserLocation(locationManager),
            getLocations = GetLocations()
        )
    }
}