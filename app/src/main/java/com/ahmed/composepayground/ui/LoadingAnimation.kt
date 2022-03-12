package com.ahmed.composepayground.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun LoadingAnimation(
    modifier: Modifier = Modifier,
    circleSize: Dp = 25.dp,
    circleColor: Color = MaterialTheme.colors.primary,
    spaceBetween: Dp = 10.dp,
    travelDistance: Dp = 20.dp
) {
    val circles = listOf(
        remember {
            Animatable(0f)
        }, remember {
            Animatable(0f)
        }, remember {
            Animatable(0f)
        })

    circles.forEachIndexed { index, it ->
        LaunchedEffect(key1 = it){
            delay(index * 100L)
            it.animateTo(
                targetValue = 0f,
                animationSpec = infiniteRepeatable(
                    animation = keyframes {
                        durationMillis = 1200
                        0.0f at 0 with LinearOutSlowInEasing
                        1f at 300 with LinearOutSlowInEasing
                        0f at 600 with LinearOutSlowInEasing
                        0f at 1200 with LinearOutSlowInEasing
                    },
                    repeatMode = RepeatMode.Restart
                )
            )
        }
    }

    val circlesValues = circles.map { it.value }
    val distance = with(LocalDensity.current) {
        travelDistance.toPx()
    }

    Row(modifier = modifier) {
        circlesValues.forEachIndexed { index, value ->
            Box(modifier = Modifier
                .size(circleSize)
                .graphicsLayer {
                    translationY= -value * distance

                }
                .background(color = circleColor, shape = CircleShape))
            if (index !=circles.size-1){
                Spacer(modifier = Modifier.width(spaceBetween))
            }
        }


    }


}