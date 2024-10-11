package com.example.myapplication.ui.components.mapView

import androidx.annotation.DrawableRes
import com.example.myapplication.R
import com.example.myapplication.constants.MarkerTypes

enum class MarkerType(
    val id: String,
    @DrawableRes val iconId: Int
) {
    UserPosition(
        id = MarkerTypes.MARKER_TYPE_USER_POSITION.id,
        iconId = R.drawable.ic_my_tracker_46dp
    ),
    OrdinaryPosition(
        id = MarkerTypes.MARKER_TYPE_ORDINARY_POSITION.id,
        iconId = R.drawable.ic_tracker_75dp
    );

    companion object {
        fun getByTypeId(typeId: String) = MarkerType.entries
            .find { it.id == typeId }
            ?: OrdinaryPosition
    }
}