package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun RainbowAddButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 52.dp
) {
    val rainbowColors = listOf(
        Color(0xFFFF8A80),
        Color(0xFFFFD180),
        Color(0xFFFFFF8D),
        Color(0xFFCCFF90),
        Color(0xFFA7FFEB),
        Color(0xFF80D8FF),
        Color(0xFF82B1FF),
        Color(0xFFB388FF),
        Color(0xFFFF80AB),
        Color(0xFFFF8A80)
    )

    Box(
        modifier = modifier
            .size(size)
            .shadow(
                elevation = 10.dp,
                shape = CircleShape,
                ambientColor = Color(0x35000000),
                spotColor = Color(0x45000000)
            )
            .border(
                width = 2.5.dp,
                brush = Brush.sweepGradient(rainbowColors),
                shape = CircleShape
            )
            .padding(2.5.dp)
            .clip(CircleShape)
            .background(Color.White)
            .border(1.dp, Color(0xFFE2E4E8), CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = true, radius = size / 2),
                onClick = onClick
            )
            .testTag("add_dome_button"),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add New Dome",
            tint = Color(0xFF121619),
            modifier = Modifier.size(24.dp)
        )
    }
}
