package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DomeColorEntity
import com.example.ui.theme.InterFontFamily
import com.example.util.ColorUtils
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun DomeCircleView(
    name: String,
    colors: List<DomeColorEntity>,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    size: Dp = 230.dp,
    dotSize: Dp = 32.dp
) {
    val clickModifier = if (onClick != null) {
        Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = ripple(bounded = false, radius = size / 2),
            onClick = onClick
        )
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .size(size)
            .then(clickModifier)
            .testTag("dome_circle_${name.lowercase().replace(" ", "_")}"),
        contentAlignment = Alignment.Center
    ) {
        // Main white circle with distinct drop shadow to contrast with background
        Box(
            modifier = Modifier
                .size(size)
                .shadow(
                    elevation = 20.dp,
                    shape = CircleShape,
                    ambientColor = Color(0x35000000),
                    spotColor = Color(0x45000000)
                )
                .clip(CircleShape)
                .background(Color.White)
                .border(
                    width = 1.2.dp,
                    color = Color(0xFFE2E4E8),
                    shape = CircleShape
                )
        ) {
            // Layout dots inside the dome circle with cohesive, balanced spacing (not touching the nametag)
            val dotCount = colors.size
            if (dotCount == 0) {
                // Empty circle placeholder
                Box(
                    modifier = Modifier
                        .size(dotSize)
                        .clip(CircleShape)
                        .background(Color(0xFFE5E7EB))
                        .align(Alignment.Center)
                )
            } else if (dotCount == 1) {
                // 1 color: exactly at center, nicely sized
                val color = ColorUtils.hexToColor(colors[0].hex)
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(color)
                        .align(Alignment.Center)
                )
            } else if (dotCount == 2) {
                // 2 colors: side by side horizontally, united with clean separation
                val c1 = ColorUtils.hexToColor(colors[0].hex)
                val c2 = ColorUtils.hexToColor(colors[1].hex)
                val spacing = size * 0.20f
                Box(
                    modifier = Modifier
                        .size(dotSize)
                        .offset(x = -spacing)
                        .clip(CircleShape)
                        .background(c1)
                        .align(Alignment.Center)
                )
                Box(
                    modifier = Modifier
                        .size(dotSize)
                        .offset(x = spacing)
                        .clip(CircleShape)
                        .background(c2)
                        .align(Alignment.Center)
                )
            } else if (dotCount == 3) {
                // 3 colors: balanced triangle, cohesive spacing
                val radius = size * 0.24f
                colors.take(3).forEachIndexed { index, colorEntity ->
                    val angle = Math.toRadians((index * 120.0) - 90.0)
                    val offsetX = (radius.value * cos(angle)).dp
                    val offsetY = (radius.value * sin(angle)).dp
                    Box(
                        modifier = Modifier
                            .size(dotSize)
                            .offset(x = offsetX, y = offsetY)
                            .clip(CircleShape)
                            .background(ColorUtils.hexToColor(colorEntity.hex))
                            .align(Alignment.Center)
                    )
                }
            } else if (dotCount == 4) {
                // 4 colors: diamond (top, right, bottom, left) comfortably inward from border and tag
                val radius = size * 0.25f
                colors.take(4).forEachIndexed { index, colorEntity ->
                    val angle = Math.toRadians(index * 90.0 - 90.0)
                    val offsetX = (radius.value * cos(angle)).dp
                    val offsetY = (radius.value * sin(angle)).dp
                    Box(
                        modifier = Modifier
                            .size(dotSize)
                            .offset(x = offsetX, y = offsetY)
                            .clip(CircleShape)
                            .background(ColorUtils.hexToColor(colorEntity.hex))
                            .align(Alignment.Center)
                    )
                }
            } else if (dotCount == 5) {
                // 5 colors: 1 center + 4 surrounding dots (matching prototype)
                // Kept inward at 26% radius so bottom dot is comfortably above the nametag!
                val centerColor = colors[1]
                Box(
                    modifier = Modifier
                        .size(dotSize)
                        .clip(CircleShape)
                        .background(ColorUtils.hexToColor(centerColor.hex))
                        .align(Alignment.Center)
                )

                // 4 outer colors (top, right, bottom, left)
                val outerColors = listOf(colors[0], colors[3], colors[4], colors[2])
                val radius = size * 0.26f
                outerColors.forEachIndexed { index, colorEntity ->
                    val angle = Math.toRadians(index * 90.0 - 90.0)
                    val offsetX = (radius.value * cos(angle)).dp
                    val offsetY = (radius.value * sin(angle)).dp
                    Box(
                        modifier = Modifier
                            .size(dotSize)
                            .offset(x = offsetX, y = offsetY)
                            .clip(CircleShape)
                            .background(ColorUtils.hexToColor(colorEntity.hex))
                            .align(Alignment.Center)
                    )
                }
            } else {
                // 6+ colors: 1 center + outer ring with cohesive radius
                val centerColor = colors[0]
                val dotSmall = 28.dp
                Box(
                    modifier = Modifier
                        .size(dotSmall)
                        .clip(CircleShape)
                        .background(ColorUtils.hexToColor(centerColor.hex))
                        .align(Alignment.Center)
                )

                val outerCount = dotCount - 1
                val radius = size * 0.27f
                for (i in 1 until dotCount) {
                    val angle = Math.toRadians(((i - 1) * (360.0 / outerCount)) - 90.0)
                    val offsetX = (radius.value * cos(angle)).dp
                    val offsetY = (radius.value * sin(angle)).dp
                    Box(
                        modifier = Modifier
                            .size(dotSmall)
                            .offset(x = offsetX, y = offsetY)
                            .clip(CircleShape)
                            .background(ColorUtils.hexToColor(colors[i].hex))
                            .align(Alignment.Center)
                    )
                }
            }
        }

        // Pill badge overlapping bottom edge with drop shadow and gray stroke
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = 12.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(18.dp),
                    ambientColor = Color(0x28000000),
                    spotColor = Color(0x38000000)
                )
                .clip(RoundedCornerShape(18.dp))
                .background(Color.White)
                .border(1.2.dp, Color(0xFFE2E4E8), RoundedCornerShape(18.dp))
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Text(
                text = name.uppercase(),
                color = Color(0xFF6B7280),
                fontSize = 11.sp,
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp
            )
        }
    }
}
