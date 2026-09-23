package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.util.ColorUtils

@Composable
fun ColorPicker2D(
    hue: Float,
    saturation: Float,
    value: Float,
    onColorChanged: (hue: Float, saturation: Float, value: Float) -> Unit,
    modifier: Modifier = Modifier
) {
    var pickerSize by remember { mutableStateOf(IntSize.Zero) }
    var hueSliderSize by remember { mutableStateOf(IntSize.Zero) }

    Column(modifier = modifier.fillMaxWidth()) {
        // 2D Saturation-Value Canvas Picker
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .shadow(4.dp, RoundedCornerShape(24.dp))
                .clip(RoundedCornerShape(24.dp))
                .onSizeChanged { pickerSize = it }
                .pointerInput(hue) {
                    detectDragGestures { change, _ ->
                        change.consume()
                        if (pickerSize.width > 0 && pickerSize.height > 0) {
                            val newSat = (change.position.x / pickerSize.width).coerceIn(0f, 1f)
                            val newVal = (1f - (change.position.y / pickerSize.height)).coerceIn(0f, 1f)
                            onColorChanged(hue, newSat, newVal)
                        }
                    }
                }
                .pointerInput(hue) {
                    detectTapGestures { offset ->
                        if (pickerSize.width > 0 && pickerSize.height > 0) {
                            val newSat = (offset.x / pickerSize.width).coerceIn(0f, 1f)
                            val newVal = (1f - (offset.y / pickerSize.height)).coerceIn(0f, 1f)
                            onColorChanged(hue, newSat, newVal)
                        }
                    }
                }
                .testTag("color_picker_canvas")
        ) {
            val pureHueColor = ColorUtils.hsvToColor(hue, 1f, 1f)

            // Canvas drawing both horizontal (white to hue) and vertical (transparent to black) gradients
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Horizontal gradient: white to pure hue
                drawRect(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color.White, pureHueColor)
                    )
                )
                // Vertical gradient: transparent to black
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black)
                    )
                )
            }

            // Draggable circular thumb
            if (pickerSize.width > 0 && pickerSize.height > 0) {
                val thumbX = (saturation * pickerSize.width).coerceIn(0f, pickerSize.width.toFloat())
                val thumbY = ((1f - value) * pickerSize.height).coerceIn(0f, pickerSize.height.toFloat())
                val thumbSizePx = 32.dp.value * (pickerSize.width / 320f).coerceIn(1f, 1.5f)

                Box(
                    modifier = Modifier
                        .offset {
                            IntOffset(
                                (thumbX - 16.dp.roundToPx()).toInt(),
                                (thumbY - 16.dp.roundToPx()).toInt()
                            )
                        }
                        .size(32.dp)
                        .shadow(6.dp, CircleShape)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(3.dp, Color.White, CircleShape)
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Rainbow Hue Slider Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .clip(RoundedCornerShape(10.dp))
                .onSizeChanged { hueSliderSize = it }
                .pointerInput(saturation, value) {
                    detectDragGestures { change, _ ->
                        change.consume()
                        if (hueSliderSize.width > 0) {
                            val ratio = (change.position.x / hueSliderSize.width).coerceIn(0f, 1f)
                            val newHue = ratio * 360f
                            onColorChanged(newHue, saturation, value)
                        }
                    }
                }
                .pointerInput(saturation, value) {
                    detectTapGestures { offset ->
                        if (hueSliderSize.width > 0) {
                            val ratio = (offset.x / hueSliderSize.width).coerceIn(0f, 1f)
                            val newHue = ratio * 360f
                            onColorChanged(newHue, saturation, value)
                        }
                    }
                }
                .testTag("hue_slider")
        ) {
            val rainbowBrush = Brush.horizontalGradient(
                listOf(
                    Color.Red,
                    Color.Yellow,
                    Color.Green,
                    Color.Cyan,
                    Color.Blue,
                    Color.Magenta,
                    Color.Red
                )
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(rainbowBrush)
            )

            // Hue Slider Thumb
            if (hueSliderSize.width > 0) {
                val thumbX = ((hue / 360f) * hueSliderSize.width).coerceIn(0f, hueSliderSize.width.toFloat())
                Box(
                    modifier = Modifier
                        .offset {
                            IntOffset(
                                (thumbX - 16.dp.roundToPx()).toInt(),
                                (-6.dp.roundToPx())
                            )
                        }
                        .size(32.dp)
                        .shadow(6.dp, CircleShape)
                        .clip(CircleShape)
                        .background(Color.White)
                        .border(3.dp, Color.White, CircleShape)
                )
            }
        }
    }
}
