package com.chinalwb.slidetoconfirm.compose

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.ui.platform.LocalContext
import kotlin.jvm.java
import kotlin.ranges.coerceAtLeast
import kotlin.ranges.coerceIn


/**
 * A composable function that creates a "Slide to Confirm" slider UI component.
 * This allows the user to slide a knob across the slider to confirm an action.
 * Useful for actions that require a deliberate user confirmation to prevent accidental triggers.
 *
 * @param modifier The Modifier to be applied to the slider container.
 * @param threshold The percentage of the slider that must be completed to trigger confirmation, range 0.0 to 1.0.
 * @param sliderContainerWidth The width of the slider knob container in Dp.
 * @param sliderIcon The drawable resource ID for the icon displayed on the slider knob.
 * @param sliderSize The size of the slider knob in Dp.
 * @param sliderTintColor The tint color applied to the slider knob icon.
 * @param sliderBackgroundColor The background color of the slider knob.
 * @param defaultBackgroundColor The background color of the slider when in the default unconfirmed state.
 * @param swipedBackgroundColor The background color of the slider once it has been swiped to confirmation.
 * @param borderRadius The corner radius of the slider container and slider knob background in Dp.
 * @param borderWidth The width of the border surrounding the slider in Dp.
 * @param borderColor The color of the border surrounding the slider.
 * @param engageText The text displayed inside the slider when in the default unconfirmed state.
 * @param engageTextStyle The text style applied to the engageText when in the unconfirmed state.
 * @param confirmedText The text displayed inside the slider when confirmation is complete.
 * @param confirmedTextStyle The text style applied to the confirmedText.
 * @param vibrationMilliseconds The duration of vibration in milliseconds upon successful confirmation.
 * @param defaultStatus The initial status of the slider, whether it starts in the confirmed or unconfirmed state.
 * @param onConfirmed A lambda function to be executed when the slider reaches the confirmation position.
 */
@Composable
fun SlideToConfirm(
    modifier: Modifier = Modifier,
    threshold: Float = 0.95f,
    sliderContainerWidth: Dp = 60.dp,
    sliderIcon: Int = R.drawable.slider,
    sliderSize: Dp = 32.dp,
    sliderTintColor: Color = Color.White,
    sliderBackgroundColor: Color = Color(0xFF484EAA),
    defaultBackgroundColor: Color = Color.White,
    swipedBackgroundColor: Color = sliderBackgroundColor,
    borderRadius: Dp = 12.dp,
    borderWidth: Dp = 1.dp,
    borderColor: Color = Color.Gray,
    engageText: String = "Slide to confirm",
    engageTextStyle: TextStyle = TextStyle(color = Color.Gray, fontSize = 12.sp),
    confirmedText: String = "Confirmed!",
    confirmedTextStyle: TextStyle = TextStyle(color = Color.White, fontSize = 12.sp),
    vibrationMilliseconds: Long = 100,
    defaultStatus: SlideToConfirmStatus = SlideToConfirmStatus.INIT,
    onConfirmed: () -> Unit = {}
) {
    val shape = RoundedCornerShape(borderRadius)

    val innerModifier = modifier
        .clip(shape)
        .background(color = defaultBackgroundColor, shape = shape)
        .border(width = borderWidth, color = borderColor, shape = shape)

    BoxWithConstraints(modifier = innerModifier) {
        // The size of the container (ie. the parent)
        val maxWidthPx = this.constraints.maxWidth.toFloat()
        // The width of the slider knob in dp
        val sliderWidthPx = with(LocalDensity.current) { sliderContainerWidth.toPx() }
        // How far the knob can travel
        val maxOffsetPx = (maxWidthPx - sliderWidthPx).coerceAtLeast(0f)

        // Where we consider "confirmed"
        val confirmOffsetPx = maxOffsetPx * threshold.coerceIn(0f, 1f)

        // Whether it is confirmed (i.e.: swiped to the end)
        var isConfirmed by rememberSaveable { mutableStateOf(defaultStatus == SlideToConfirmStatus.CONFIRMED) }

        val context = LocalContext.current
        // Callback when reaching the end
        val onReachEnd = {
            isConfirmed = true
            vibrateIfNeeded(context, vibrationMilliseconds)
            onConfirmed()
        }

        // The swiped distance
        val offsetX = remember { Animatable(0f) }

        // Draw foreground which is shown before swiping and the unswiped area
        InitStatusBackground(defaultBackgroundColor, isConfirmed, engageText, engageTextStyle)

        // Draw swiped area background
        SwipedAreaBackground( offsetX, borderRadius, isConfirmed, maxWidthPx, swipedBackgroundColor, confirmedText, confirmedTextStyle)

        // Draw the slider knob
        if (!isConfirmed) {
            SliderKnob(borderRadius, sliderContainerWidth, offsetX, sliderBackgroundColor, maxOffsetPx, confirmOffsetPx, onReachEnd, sliderIcon, sliderSize, sliderTintColor, engageText)
        }
    }
}

@Composable
private fun SwipedAreaBackground(
    offsetX: Animatable<Float, AnimationVector1D>,
    borderRadius: Dp,
    isConfirmed: Boolean,
    maxWidthPx: Float,
    swipedBackgroundColor: Color,
    confirmedText: String,
    confirmedTextStyle: TextStyle
) {
    var offsetXDp = with(LocalDensity.current) {
        offsetX.value.toDp()
    } + if (offsetX.value > 0) borderRadius else 0.dp

    if (isConfirmed) {
        offsetXDp = with(LocalDensity.current) { maxWidthPx.toDp() }
    }

    val swipedShape = RoundedCornerShape(
        topStart = borderRadius,
        topEnd = if (isConfirmed) borderRadius else 0.dp,
        bottomEnd = if (isConfirmed) borderRadius else 0.dp,
        bottomStart = borderRadius
    )

    Box(
        Modifier
            .fillMaxHeight()
            .width(offsetXDp)
            .background(color = swipedBackgroundColor, shape = swipedShape)
    ) {
        if (isConfirmed) {
            Text(
                text = confirmedText,
                modifier = Modifier.align(Alignment.Center),
                style = confirmedTextStyle
            )
        }
    }
}

@Composable
private fun InitStatusBackground(
    defaultBackgroundColor: Color,
    isConfirmed: Boolean,
    engageText: String,
    engageTextStyle: TextStyle
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = defaultBackgroundColor)
    ) {
        if (!isConfirmed) {
            Text(
                text = engageText,
                modifier = Modifier.align(Alignment.Center),
                style = engageTextStyle
            )
        }
    }
}

@Composable
private fun SliderKnob(
    borderRadius: Dp,
    sliderContainerWidth: Dp,
    offsetX: Animatable<Float, AnimationVector1D>,
    sliderBackgroundColor: Color,
    maxOffsetPx: Float,
    confirmOffsetPx: Float,
    onReachEnd: () -> Unit,
    sliderIcon: Int,
    sliderSize: Dp,
    sliderTintColor: Color,
    engageText: String
) {
    val sliderShape = RoundedCornerShape(
        topStart = borderRadius,
        topEnd = borderRadius,
        bottomEnd = borderRadius,
        bottomStart = borderRadius
    )
    val scope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(sliderContainerWidth)
            .offset { IntOffset(offsetX.value.toInt(), 0) }
            .clip(sliderShape)
            .background(color = sliderBackgroundColor, shape = sliderShape)
            .draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta: Float ->
                    val new = (offsetX.value + delta).coerceIn(0f, maxOffsetPx)
                    scope.launch { offsetX.snapTo(new) }
                },
                onDragStopped = {
                    scope.launch {
                        if (offsetX.value >= confirmOffsetPx) {
                            // Animate to the end and notify
                            offsetX.animateTo(maxOffsetPx)
                            delay(100)
                            onReachEnd()
                        } else {
                            // Animate back to start
                            offsetX.animateTo(0f)
                        }
                    }
                }
            )
    ) {
        Icon(
            painter = painterResource(id = sliderIcon),
            modifier = Modifier
                .size(sliderSize)
                .align(Alignment.Center),
            contentDescription = engageText,
            tint = sliderTintColor,
        )
    }
}


fun vibrateIfNeeded(
    context: Context,
    vibrationMilliseconds: Long
) {
    try {
        if (vibrationMilliseconds > 0) {
            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vm = context.getSystemService(VibratorManager::class.java)
                vm?.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                context.getSystemService(Vibrator::class.java)
            }

            if (vibrator != null && vibrator.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val effect = VibrationEffect.createOneShot(
                        vibrationMilliseconds,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                    vibrator.vibrate(effect)
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(vibrationMilliseconds)
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * Represents the status of a slide-to-confirm interaction.
 *
 * Enum values:
 * - `INIT`: Indicates the initial state of the slider before confirmation.
 * - `CONFIRMED`: Indicates that the slider has reached the confirm position.
 *
 * Used to track and manage the current state of the slider in the slide-to-confirm feature.
 */
enum class SlideToConfirmStatus {
    INIT,
    CONFIRMED
}


/*
----------------------------
----------------------------
*/

@Preview(widthDp = 392)
@Composable
fun SlideToConfirmPreview() {
    SlideToConfirm(
        modifier = Modifier
            .height(60.dp)
            .background(color = Color.White),
        borderColor = Color(0xFF484EAA),
        defaultBackgroundColor = Color.White,
        swipedBackgroundColor = Color(0xFFD81B60),
        defaultStatus = SlideToConfirmStatus.INIT
    )
}