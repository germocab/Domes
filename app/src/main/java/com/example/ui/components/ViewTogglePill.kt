package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material3.Icon
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

enum class BoardViewMode {
    MAP,
    LIST
}

@Composable
fun ViewTogglePill(
    currentMode: BoardViewMode,
    onModeSelected: (BoardViewMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(28.dp),
                ambientColor = Color(0x24000000),
                spotColor = Color(0x35000000)
            )
            .clip(RoundedCornerShape(28.dp))
            .background(Color.White)
            .border(1.2.dp, Color(0xFFE2E4E8), RoundedCornerShape(28.dp))
            .padding(4.dp)
            .testTag("view_toggle_pill")
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Map / Grid option
            val isMapSelected = currentMode == BoardViewMode.MAP
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(if (isMapSelected) Color(0xFF121619) else Color.Transparent)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(bounded = true, radius = 20.dp),
                        onClick = { onModeSelected(BoardViewMode.MAP) }
                    )
                    .testTag("toggle_map_view"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.GridView,
                    contentDescription = "Map View",
                    tint = if (isMapSelected) Color.White else Color(0xFF121619),
                    modifier = Modifier.size(20.dp)
                )
            }

            // List option
            val isListSelected = currentMode == BoardViewMode.LIST
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(if (isListSelected) Color(0xFF121619) else Color.Transparent)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(bounded = true, radius = 20.dp),
                        onClick = { onModeSelected(BoardViewMode.LIST) }
                    )
                    .testTag("toggle_list_view"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.ViewAgenda,
                    contentDescription = "List View",
                    tint = if (isListSelected) Color.White else Color(0xFF121619),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
