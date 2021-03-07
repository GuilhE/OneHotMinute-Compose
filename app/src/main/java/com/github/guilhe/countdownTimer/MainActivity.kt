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
@file:Suppress("SameParameterValue")

package com.github.guilhe.countdownTimer

import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androiddevchallenge.R
import com.github.guilhe.countdownTimer.ui.CircularProgress
import com.github.guilhe.countdownTimer.ui.theme.AppTheme

class MainActivity : AppCompatActivity() {

   private val step = 1000
   private val max = 60
   private val timeLiveData = MutableLiveData(max)
   private val time: LiveData<Int> = timeLiveData
   private val pausedLiveData = MutableLiveData(true)
   private val paused: LiveData<Boolean> = pausedLiveData
   private val timer = object : CountDownTimer(max * 1000L, step.toLong()) {
        override fun onTick(millisUntilFinished: Long) {
            val current = timeLiveData.value ?: 0
            if(current <= 0) {
                onFinish()
            } else {
                timeLiveData.value = current - 1
            }
        }

        override fun onFinish() {
            pausedLiveData.value = true
            timeLiveData.value = max
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                val timeValue by time.observeAsState(max)
                val isPaused  by paused.observeAsState(true)
                CountdownScreen(timeValue, max, isPaused) {
                    when (it) {
                        Action.Play -> {
                            pausedLiveData.value = false
                            timer.start()
                        }
                        Action.Pause -> {
                            pausedLiveData.value = true
                            timer.cancel()
                        }
                        Action.Stop -> {
                            timer.cancel()
                            timer.onFinish()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CountdownScreen(time: Int, max :Int, paused: Boolean, onAction: (Action) -> Unit) {
    ConstraintLayout(Modifier.fillMaxSize()) {
        val (progress, label, btnStart, btnStop) = createRefs()
        CircularProgress(
            Modifier
                .width(150.dp)
                .height(150.dp)
                .constrainAs(progress) { centerTo(parent) },
            progress = time.toFloat() * 100 / max,
            color = MaterialTheme.colors.primary,
            animationSpec = tween(durationMillis = if(time == max) 2000 else 200, easing = LinearEasing)
        )
        Text(
            text = "$time",
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.primary,
            fontSize = 50.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(8.dp)
                .constrainAs(label) { centerTo(progress) },
        )
        FloatingActionButton(
            onClick = { onAction.invoke(if (paused) Action.Play else Action.Pause) },
            modifier = Modifier
                .size(50.dp)
                .constrainAs(btnStart) {
                    top.linkTo(progress.top, 100.dp)
                    start.linkTo(progress.end, 10.dp)
                }) {
            Image(
                painterResource(if (paused) R.drawable.ic_play else R.drawable.ic_pause),
                stringResource(if (paused) R.string.btn_play else R.string.btn_pause)
            )
        }
        FloatingActionButton(
            onClick = { onAction.invoke(Action.Stop) },
            modifier = Modifier
                .size(50.dp)
                .constrainAs(btnStop) {
                    top.linkTo(btnStart.bottom, 2.dp)
                    end.linkTo(btnStart.end, 40.dp)
                }) {
            Image(painterResource(R.drawable.ic_stop), stringResource(R.string.btn_stop))
        }
    }
}

@Composable
@Preview
fun Mock() {
    AppTheme { CountdownScreen(70, 60,  true, onAction = { }) }
}

sealed class Action {
    object Play : Action()
    object Pause : Action()
    object Stop : Action()
}