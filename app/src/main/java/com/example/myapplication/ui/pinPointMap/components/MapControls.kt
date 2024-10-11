package com.example.myapplication.ui.pinPointMap.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.R

@Composable
fun MapControls(
    modifier: Modifier = Modifier,
    isUserLocationVisible: Boolean = false,
    isNextLocationVisible: Boolean = false,
    onZoomInClick: () -> Unit,
    onZoomOutClick: () -> Unit,
    onLocateUserClick: () -> Unit,
    onNextLocationClick: () -> Unit,
) {
    Column(
        modifier = modifier.padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Image(
            modifier = Modifier
                .align(Alignment.End)
                .size(55.dp)
                .clip(CircleShape)
                .clickable(onClick = onZoomInClick),
            painter = painterResource(id = R.drawable.ic_zoom_plus_55dp),
            contentDescription = "locate_button"
        )

        Image(
            modifier = Modifier
                .align(Alignment.End)
                .size(55.dp)
                .clip(CircleShape)
                .clickable(onClick = onZoomOutClick),
            painter = painterResource(id = R.drawable.ic_zoom_minus_55dp),
            contentDescription = "locate_button"
        )

        AnimatedVisibility(
            modifier = Modifier.align(Alignment.End),
            visible = isUserLocationVisible,
            enter = fadeIn(tween(300)),
            exit = fadeOut(tween(300)),
        ) {
            Image(
                modifier = Modifier
                    .size(55.dp)
                    .clip(CircleShape)
                    .clickable(
                        enabled = isUserLocationVisible,
                        onClick = onLocateUserClick
                    ),
                painter = painterResource(id = R.drawable.ic_mylocation_55dp),
                contentDescription = "locate_button"
            )
        }

        AnimatedVisibility(
            modifier = Modifier.align(Alignment.End),
            visible = isNextLocationVisible,
            enter = fadeIn(tween(300)),
            exit = fadeOut(tween(300)),
        ) {
            Image(
                modifier = Modifier
                    .size(55.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onNextLocationClick),
                painter = painterResource(id = R.drawable.ic_next_tracker_55dp),
                contentDescription = "locate_button"
            )
        }
    }
}