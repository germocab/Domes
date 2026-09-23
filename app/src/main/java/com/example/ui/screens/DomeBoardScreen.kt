package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.DomeWithColors
import com.example.ui.components.BoardViewMode
import com.example.ui.components.DomeCircleView
import com.example.ui.components.RainbowAddButton
import com.example.ui.components.ViewTogglePill
import com.example.ui.theme.InterFontFamily
import com.example.ui.viewmodel.DomesViewModel
import com.example.util.ColorUtils

@Composable
fun DomeBoardScreen(
    viewModel: DomesViewModel,
    onNavigateToNewDome: () -> Unit,
    onNavigateToDome: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val domes by viewModel.domes.collectAsStateWithLifecycle()
    val viewMode by viewModel.viewMode.collectAsStateWithLifecycle()
    val domePositions by viewModel.domePositions.collectAsStateWithLifecycle()
    val density = LocalDensity.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7F8FA))
            .statusBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header: "Your Domes" + Rainbow Add Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 20.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Your Domes",
                    fontSize = 32.sp,
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF121619),
                    modifier = Modifier.testTag("board_title")
                )

                RainbowAddButton(onClick = onNavigateToNewDome)
            }

            // Main Content: Map view or List view
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                if (viewMode == BoardViewMode.LIST) {
                    // List View
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("domes_list_view"),
                        contentPadding = PaddingValues(
                            start = 20.dp,
                            end = 20.dp,
                            top = 8.dp,
                            bottom = 96.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(domes, key = { it.dome.id }) { domeWithColors ->
                            DomeListCard(
                                domeWithColors = domeWithColors,
                                onClick = { onNavigateToDome(domeWithColors.dome.id) }
                            )
                        }

                        if (domes.isEmpty()) {
                            item {
                                EmptyBoardNotice(onAddClick = onNavigateToNewDome)
                            }
                        }
                    }
                } else {
                    // Map / Dome View with Dragging & Pinch to Zoom
                    var scale by remember { mutableFloatStateOf(1f) }
                    var panOffset by remember { mutableStateOf(Offset.Zero) }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {
                                detectTransformGestures { _, pan, zoom, _ ->
                                    scale = (scale * zoom).coerceIn(0.5f, 2.2f)
                                    panOffset += pan
                                }
                            }
                            .testTag("domes_map_view")
                    ) {
                        // Transformable Map Surface
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer {
                                    scaleX = scale
                                    scaleY = scale
                                    translationX = panOffset.x
                                    translationY = panOffset.y
                                }
                        ) {
                            domes.forEachIndexed { index, domeWithColors ->
                                val defaultOffset = calculateDomeOffset(index)
                                val customOffset = domePositions[domeWithColors.dome.id]
                                val currentX = customOffset?.first?.dp ?: defaultOffset.first
                                val currentY = customOffset?.second?.dp ?: defaultOffset.second

                                var isDraggingThisDome by remember { mutableStateOf(false) }

                                Box(
                                    modifier = Modifier
                                        .offset(x = currentX, y = currentY)
                                        .graphicsLayer {
                                            if (isDraggingThisDome) {
                                                scaleX = 1.06f
                                                scaleY = 1.06f
                                                shadowElevation = 24.dp.toPx()
                                            }
                                        }
                                        .pointerInput(domeWithColors.dome.id, currentX, currentY, scale) {
                                            detectDragGesturesAfterLongPress(
                                                onDragStart = {
                                                    isDraggingThisDome = true
                                                },
                                                onDragEnd = {
                                                    isDraggingThisDome = false
                                                },
                                                onDragCancel = {
                                                    isDraggingThisDome = false
                                                },
                                                onDrag = { change, dragAmount ->
                                                    change.consume()
                                                    val deltaXDp = with(density) { (dragAmount.x / scale).toDp().value }
                                                    val deltaYDp = with(density) { (dragAmount.y / scale).toDp().value }
                                                    viewModel.updateDomePosition(
                                                        domeWithColors.dome.id,
                                                        currentX.value + deltaXDp,
                                                        currentY.value + deltaYDp
                                                    )
                                                }
                                            )
                                        }
                                ) {
                                    DomeCircleView(
                                        name = domeWithColors.dome.name,
                                        colors = domeWithColors.colors,
                                        onClick = { onNavigateToDome(domeWithColors.dome.id) },
                                        size = 230.dp
                                    )
                                }
                            }

                            if (domes.isEmpty()) {
                                EmptyBoardNotice(
                                    onAddClick = onNavigateToNewDome,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Floating Switcher Pill in Bottom-Left
        ViewTogglePill(
            currentMode = viewMode,
            onModeSelected = { viewModel.setViewMode(it) },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 20.dp, bottom = 28.dp)
        )
    }
}

private fun calculateDomeOffset(index: Int): Pair<androidx.compose.ui.unit.Dp, androidx.compose.ui.unit.Dp> {
    // Staggered layout matching the prototype images
    return when (index) {
        0 -> Pair(20.dp, 20.dp)       // Top-left
        1 -> Pair(260.dp, 45.dp)      // Top-right
        2 -> Pair(120.dp, 280.dp)     // Middle-center (e.g. Nature Brand)
        3 -> Pair(12.dp, 530.dp)      // Lower-left
        4 -> Pair(265.dp, 510.dp)     // Lower-right
        5 -> Pair(130.dp, 760.dp)     // Centered next row
        else -> {
            val row = index / 2
            val col = index % 2
            val x = if (col == 0) 30.dp else 260.dp
            val y = (row * 240 + 20).dp
            Pair(x, y)
        }
    }
}

@Composable
fun DomeListCard(
    domeWithColors: DomeWithColors,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(28.dp),
                ambientColor = Color(0x20000000),
                spotColor = Color(0x2D000000)
            )
            .clip(RoundedCornerShape(28.dp))
            .background(Color.White)
            .border(
                width = 1.2.dp,
                color = Color(0xFFE2E4E8),
                shape = RoundedCornerShape(28.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 22.dp, vertical = 20.dp)
            .testTag("dome_card_${domeWithColors.dome.name.lowercase().replace(" ", "_")}")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = domeWithColors.dome.name,
                fontSize = 20.sp,
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF121619)
            )
            Text(
                text = "${domeWithColors.colors.size} Colors",
                fontSize = 14.sp,
                fontFamily = InterFontFamily,
                color = Color(0xFF8E8E93),
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(top = 2.dp, bottom = 14.dp)
            )

            // Horizontal row of color dots with generous spacing
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                domeWithColors.colors.forEach { colorEntity ->
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .shadow(3.dp, CircleShape)
                            .clip(CircleShape)
                            .background(ColorUtils.hexToColor(colorEntity.hex))
                            .border(1.dp, Color(0x18000000), CircleShape)
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyBoardNotice(
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "No Domes yet",
            fontSize = 20.sp,
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF121619)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Tap the + button to create your first Dome palette",
            fontSize = 14.sp,
            fontFamily = InterFontFamily,
            color = Color(0xFF8E8E93)
        )
    }
}
