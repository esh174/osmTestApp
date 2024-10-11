package com.example.myapplication.ui.components.loadingIndicators

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun LoadingView(
    modifier: Modifier = Modifier,
    visible: Boolean = true,
    color: Color = Color.LightGray,
    backgroundColor: Color = Color.White,
    enterAnimationTimeMillis: Int = 200,
    exitAnimationTimeMillis: Int = 200,
) {
    AnimatedVisibility(
        modifier = modifier.pointerInput(Unit) {
            detectTapGestures(onTap = {})
        },
        visible = visible,
        enter = fadeIn(animationSpec = tween(enterAnimationTimeMillis)),
        exit = fadeOut(animationSpec = tween(exitAnimationTimeMillis))
    ) {
        Column(
            modifier = Modifier.background(backgroundColor),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(color = color)
        }
    }
}