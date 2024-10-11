package com.example.myapplication.domain.entity

import javax.annotation.concurrent.Immutable

//TODO: Create user model

@Immutable
data class UserLocation(
    val id: String,
    val name: String,
    val avatarUrl: String,
    val birthdate: String,
    val lastActive: String,
    val signalType: String,
    val location: PointLocation
)
