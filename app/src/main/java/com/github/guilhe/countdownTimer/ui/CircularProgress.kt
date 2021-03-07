/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.guilhe.countdownTimer.ui

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.guilhe.countdownTimer.ui.theme.AppTheme

@Composable
fun CircularProgress(
    modifier: Modifier = Modifier,
    color: Color,
    backgroundColor: Color = color,
    startingAngle: Float = 270f,
    progress: Float,
    animate: Boolean = true,
    animationSpec: AnimationSpec<Float> = tween(durationMillis = 250, easing = LinearOutSlowInEasing)
) {
    val darkMode = isSystemInDarkTheme()
    val animatedProgress: Float by animateFloatAsState(targetValue = progress, animationSpec = animationSpec)
    Canvas(modifier) {
        val sweepAngle = (360 * if (animate) animatedProgress else progress) / 100
        val ringRadius = size.minDimension * 0.15f
        val size = Size(size.width, size.height)
        drawArc(backgroundColor, startingAngle, 360f, false, size = size, alpha = 0.2f, style = Stroke(ringRadius))
        drawArc(
            color = if (darkMode) Color.White else Color.Black,
            startingAngle, sweepAngle, false, Offset(0f, 20f), size, 0.2f, Stroke(ringRadius, cap = StrokeCap.Round)
        )
        drawArc(color, startingAngle, sweepAngle, false, size = size, style = Stroke(ringRadius, cap = StrokeCap.Round))
    }
}

@ExperimentalFoundationApi
@Composable
@Preview
private fun MockCountdownTimer() {
    AppTheme {
        val rows = listOf(
            Pair(0f, MaterialTheme.colors.primary),
            Pair(5f, MaterialTheme.colors.primary),
            Pair(10f, MaterialTheme.colors.primary),
            Pair(15f, MaterialTheme.colors.primary),
            Pair(20f, MaterialTheme.colors.primary),
            Pair(25f, MaterialTheme.colors.primary),
            Pair(30f, MaterialTheme.colors.primary),
            Pair(35f, MaterialTheme.colors.primary),
            Pair(40f, MaterialTheme.colors.primary),
            Pair(45f, MaterialTheme.colors.primary),
            Pair(50f, MaterialTheme.colors.primary),
            Pair(55f, MaterialTheme.colors.primary),
            Pair(60f, MaterialTheme.colors.primary),
            Pair(65f, MaterialTheme.colors.primary),
            Pair(70f, MaterialTheme.colors.primary),
            Pair(75f, MaterialTheme.colors.primary),
            Pair(80f, MaterialTheme.colors.primary),
            Pair(85f, MaterialTheme.colors.primary),
            Pair(90f, MaterialTheme.colors.primary),
            Pair(95f, MaterialTheme.colors.primary),
            Pair(100f, MaterialTheme.colors.primary)
        )
        LazyVerticalGrid(modifier = Modifier.fillMaxSize(), cells = GridCells.Adaptive(minSize = 130.dp)) {
            items(rows) {
                Box(Modifier.padding(20.dp), contentAlignment = Alignment.Center) {
                    CircularProgress(
                        Modifier
                            .width(80.dp)
                            .height(80.dp),
                        progress = it.first, color = it.second
                    )
                }
            }
        }
    }
}
