package com.chinalwb.compose.slidetoconfirm.demo

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chinalwb.slidetoconfirm.compose.SlideToConfirm
import com.chinalwb.slidetoconfirm.compose.SlideToConfirmStatus

@Composable
fun SlideToConfirmDemo(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        SliderDemo1()
        Spacer(modifier = Modifier.height(16.dp))
        SliderDemo2()
        Spacer(modifier = Modifier.height(16.dp))
        SliderDemo3()
    }
}

@Composable
private fun SliderDemo1() {
    Box(modifier = Modifier.fillMaxWidth()) {
        SlideToConfirm(
            modifier = Modifier
                .height(60.dp)
                .background(color = Color.White),
            borderColor = Color(0xFF484EAA),
            defaultBackgroundColor = Color.White,
            swipedBackgroundColor = Color(0xFFD81B60),
            defaultStatus = SlideToConfirmStatus.INIT
        ) {
            Log.d("xx", "SlideToConfirmTest: confirmed!")
        }
    }
}


@Composable
private fun SliderDemo2() {
    Box(modifier = Modifier.fillMaxWidth()) {
        SlideToConfirm(
            modifier = Modifier
                .height(60.dp)
                .background(color = Color.White),
            sliderIcon = com.chinalwb.compose.slidetoconfirm.R.drawable.arrow,
            sliderBackgroundColor = Color(0xFF484EAA),
            sliderTintColor = Color(0xFFD81B60),
            sliderSize = 24.dp,
            sliderContainerWidth = 80.dp,
            borderColor = Color(0xFF484EAA),
            borderWidth = 2.dp,
            borderRadius = 30.dp,
            defaultBackgroundColor = Color.White,
            swipedBackgroundColor = Color(0xFFD81B60),
            engageText = "Slide to stop billing",
            engageTextStyle = TextStyle(fontSize = 17.sp, color = Color(0xFFD81B60)),
            defaultStatus = SlideToConfirmStatus.INIT
        ) {
            Log.d("xx", "SlideToConfirmTest: confirmed!")
        }
    }
}


@Composable
private fun SliderDemo3() {
    Box(modifier = Modifier.fillMaxWidth()) {
        SlideToConfirm(
            modifier = Modifier
                .height(60.dp)
                .background(color = Color.White),
            sliderIcon = com.chinalwb.compose.slidetoconfirm.R.drawable.arrow_hand,
            sliderBackgroundColor = Color(0xFF484EAA),
            sliderSize = 24.dp,
            sliderContainerWidth = 40.dp,
            borderColor = Color(0xFF484EAA),
            defaultBackgroundColor = Color(0xFFD81B60),
            swipedBackgroundColor = Color(0xFF484EAA),
            engageText = "Swipe right to unlock",
            engageTextStyle = TextStyle(fontSize = 12.sp, color = Color.White),
            confirmedText = "Unlocked!",
            defaultStatus = SlideToConfirmStatus.INIT
        ) {
            Log.d("xx", "SlideToConfirmTest: confirmed!")
        }
    }
}

@Preview
@Composable
fun SlideToConfirmDemoPreview() {
    SlideToConfirmDemo()
}