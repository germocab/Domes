package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ripple
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.AddColorBottomSheet
import com.example.ui.components.BoardViewMode
import com.example.ui.components.CircularBackButton
import com.example.ui.components.ViewTogglePill
import com.example.ui.theme.InterFontFamily
import com.example.ui.viewmodel.DomesViewModel
import com.example.util.ColorUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DomeDetailScreen(
    domeId: Long,
    viewModel: DomesViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToColor: (colorId: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val domeWithColorsFlow = remember(domeId) { viewModel.getDomeWithColors(domeId) }
    val domeWithColors by domeWithColorsFlow.collectAsStateWithLifecycle(initialValue = null)
    val colorPositions by viewModel.colorPositions.collectAsStateWithLifecycle()
    val density = LocalDensity.current

    var currentViewMode by remember { mutableStateOf(BoardViewMode.MAP) }
    var showAddColorSheet by remember { mutableStateOf(false) }
    var showOptionsMenu by remember { mutableStateOf(false) }
    var showRenameDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (domeWithColors == null) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Text("Loading...", color = Color.Gray, fontFamily = InterFontFamily)
        }
        return
    }

    val dome = domeWithColors!!.dome
    val colors = domeWithColors!!.colors

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7F8FA))
            .statusBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header Row: Back + Dome Title + Options / Add
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    CircularBackButton(onBack = onNavigateBack)
                    Spacer(modifier = Modifier.width(14.dp))
                    Text(
                        text = dome.name,
                        fontSize = 28.sp,
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF121619),
                        modifier = Modifier.testTag("dome_detail_title")
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Circular "+" Button with drop shadow and gray stroke
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .shadow(
                                elevation = 10.dp,
                                shape = CircleShape,
                                ambientColor = Color(0x35000000),
                                spotColor = Color(0x45000000)
                            )
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(1.2.dp, Color(0xFFE2E4E8), CircleShape)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(bounded = true, radius = 23.dp),
                                onClick = { showAddColorSheet = true }
                            )
                            .testTag("dome_add_color_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Color",
                            tint = Color(0xFF121619),
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    // More Menu Circular Button with identical drop shadow and gray stroke
                    Box {
                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .shadow(
                                    elevation = 10.dp,
                                    shape = CircleShape,
                                    ambientColor = Color(0x35000000),
                                    spotColor = Color(0x45000000)
                                )
                                .clip(CircleShape)
                                .background(Color.White)
                                .border(1.2.dp, Color(0xFFE2E4E8), CircleShape)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = ripple(bounded = true, radius = 23.dp),
                                    onClick = { showOptionsMenu = true }
                                )
                                .testTag("dome_options_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More Options",
                                tint = Color(0xFF121619),
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = showOptionsMenu,
                            onDismissRequest = { showOptionsMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Rename Dome", fontFamily = InterFontFamily) },
                                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                                onClick = {
                                    showOptionsMenu = false
                                    showRenameDialog = true
                                },
                                modifier = Modifier.testTag("menu_rename_dome")
                            )
                            DropdownMenuItem(
                                text = { Text("Delete Dome", color = Color(0xFFE53935), fontFamily = InterFontFamily) },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = null,
                                        tint = Color(0xFFE53935)
                                    )
                                },
                                onClick = {
                                    showOptionsMenu = false
                                    showDeleteDialog = true
                                },
                                modifier = Modifier.testTag("menu_delete_dome")
                            )
                        }
                    }
                }
            }

            // Body: Bubble/Cluster View or List View
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                if (currentViewMode == BoardViewMode.LIST) {
                    // List View of Colors
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("dome_colors_list"),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(colors, key = { it.id }) { colorEntity ->
                            val parsed = ColorUtils.hexToColor(colorEntity.hex)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(
                                        elevation = 8.dp,
                                        shape = RoundedCornerShape(20.dp),
                                        ambientColor = Color(0x20000000),
                                        spotColor = Color(0x2D000000)
                                    )
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Color.White)
                                    .border(
                                        width = 1.2.dp,
                                        color = Color(0xFFE2E4E8),
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                    .clickable { onNavigateToColor(colorEntity.id) }
                                    .padding(16.dp)
                                    .testTag("color_item_${colorEntity.id}")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(54.dp)
                                            .clip(CircleShape)
                                            .background(parsed)
                                            .border(1.dp, Color(0x1A000000), CircleShape)
                                    )

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = colorEntity.name,
                                            fontSize = 18.sp,
                                            fontFamily = InterFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF121619)
                                        )
                                        Text(
                                            text = "HEX: #${colorEntity.hex}  •  RGB: ${colorEntity.r}, ${colorEntity.g}, ${colorEntity.b}",
                                            fontSize = 13.sp,
                                            fontFamily = InterFontFamily,
                                            color = Color(0xFF757575)
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // Bubble / Cluster View (Interactive Map with dragging and pinch-to-zoom!)
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
                            .testTag("dome_bubbles_cluster")
                    ) {
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
                            colors.forEachIndexed { index, colorEntity ->
                                val defaultPos = calculateBubblePosition(index)
                                val customOffset = colorPositions[colorEntity.id]
                                val currentX = customOffset?.first?.dp ?: defaultPos.first
                                val currentY = customOffset?.second?.dp ?: defaultPos.second
                                val bubbleSize = defaultPos.third

                                val parsed = ColorUtils.hexToColor(colorEntity.hex)
                                val isLight = ColorUtils.isLightColor(parsed)
                                val contentColor = if (isLight) Color(0xFF121619) else Color.White

                                var isDraggingThisColor by remember { mutableStateOf(false) }

                                Box(
                                    modifier = Modifier
                                        .offset(x = currentX, y = currentY)
                                        .size(bubbleSize)
                                        .graphicsLayer {
                                            if (isDraggingThisColor) {
                                                scaleX = 1.08f
                                                scaleY = 1.08f
                                                shadowElevation = 24.dp.toPx()
                                            }
                                        }
                                        .shadow(
                                            elevation = if (isDraggingThisColor) 24.dp else 12.dp,
                                            shape = CircleShape,
                                            ambientColor = Color(0x24000000),
                                            spotColor = Color(0x35000000)
                                        )
                                        .clip(CircleShape)
                                        .background(parsed)
                                        .border(1.2.dp, Color(0x22000000), CircleShape)
                                        .pointerInput(colorEntity.id, currentX, currentY, scale) {
                                            detectDragGesturesAfterLongPress(
                                                onDragStart = {
                                                    isDraggingThisColor = true
                                                },
                                                onDragEnd = {
                                                    isDraggingThisColor = false
                                                },
                                                onDragCancel = {
                                                    isDraggingThisColor = false
                                                },
                                                onDrag = { change, dragAmount ->
                                                    change.consume()
                                                    val deltaXDp = with(density) { (dragAmount.x / scale).toDp().value }
                                                    val deltaYDp = with(density) { (dragAmount.y / scale).toDp().value }
                                                    viewModel.updateColorPosition(
                                                        colorEntity.id,
                                                        currentX.value + deltaXDp,
                                                        currentY.value + deltaYDp
                                                    )
                                                }
                                            )
                                        }
                                        .clickable { onNavigateToColor(colorEntity.id) }
                                        .padding(20.dp)
                                        .testTag("bubble_${colorEntity.name.lowercase().replace(" ", "_")}"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = colorEntity.name,
                                            fontSize = 19.sp,
                                            fontFamily = InterFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            color = contentColor,
                                            textAlign = TextAlign.Center
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "HEX",
                                            fontSize = 11.sp,
                                            fontFamily = InterFontFamily,
                                            fontWeight = FontWeight.Bold,
                                            color = contentColor.copy(alpha = 0.85f),
                                            letterSpacing = 0.5.sp
                                        )
                                        Text(
                                            text = colorEntity.hex,
                                            fontSize = 12.sp,
                                            fontFamily = InterFontFamily,
                                            fontWeight = FontWeight.Medium,
                                            color = contentColor.copy(alpha = 0.85f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Bottom-left Switcher Pill
        ViewTogglePill(
            currentMode = currentViewMode,
            onModeSelected = { currentViewMode = it },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 20.dp, bottom = 28.dp)
        )

        // Add Color Sheet
        if (showAddColorSheet) {
            AddColorBottomSheet(
                onDismiss = { showAddColorSheet = false },
                onColorAdded = { name, hex, r, g, b ->
                    viewModel.addColorToDome(domeId, name, hex, r, g, b)
                    showAddColorSheet = false
                }
            )
        }

        // Rename Dome Dialog
        if (showRenameDialog) {
            var newName by remember { mutableStateOf(dome.name) }
            AlertDialog(
                onDismissRequest = { showRenameDialog = false },
                title = { Text("Rename Dome", fontWeight = FontWeight.Bold, fontFamily = InterFontFamily) },
                text = {
                    OutlinedTextField(
                        value = newName,
                        onValueChange = { newName = it },
                        label = { Text("Dome Name") },
                        singleLine = true
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.updateDomeName(domeId, newName)
                            showRenameDialog = false
                        }
                    ) {
                        Text("Save", color = Color(0xFF121619), fontWeight = FontWeight.Bold, fontFamily = InterFontFamily)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showRenameDialog = false }) {
                        Text("Cancel", color = Color.Gray, fontFamily = InterFontFamily)
                    }
                }
            )
        }

        // Delete Dome Dialog
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Delete Dome", fontWeight = FontWeight.Bold, fontFamily = InterFontFamily) },
                text = { Text("Are you sure you want to delete '${dome.name}'? This cannot be undone.", fontFamily = InterFontFamily) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDeleteDialog = false
                            viewModel.deleteDome(domeId) {
                                onNavigateBack()
                            }
                        }
                    ) {
                        Text("Delete", color = Color(0xFFE53935), fontWeight = FontWeight.Bold, fontFamily = InterFontFamily)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Cancel", color = Color.Gray, fontFamily = InterFontFamily)
                    }
                }
            )
        }
    }
}

private fun calculateBubblePosition(index: Int): Triple<Dp, Dp, Dp> {
    // Spreads colors both sideways and downwards in an organic multi-column staggered canvas
    // Generously separated with clear breathing room and distinct presence across the pannable board
    val bubbleSize = 190.dp
    return when (index) {
        0 -> Triple(20.dp, 24.dp, bubbleSize)       // Column 0, Row 0
        1 -> Triple(230.dp, 60.dp, bubbleSize)      // Column 1, Row 0 (sideways right)
        2 -> Triple(440.dp, 24.dp, bubbleSize)      // Column 2, Row 0 (further sideways right)
        3 -> Triple(20.dp, 270.dp, bubbleSize)      // Column 0, Row 1
        4 -> Triple(230.dp, 305.dp, bubbleSize)     // Column 1, Row 1 (center)
        5 -> Triple(440.dp, 270.dp, bubbleSize)     // Column 2, Row 1
        6 -> Triple(20.dp, 515.dp, bubbleSize)      // Column 0, Row 2
        7 -> Triple(230.dp, 550.dp, bubbleSize)     // Column 1, Row 2
        8 -> Triple(440.dp, 515.dp, bubbleSize)     // Column 2, Row 2
        else -> {
            val col = index % 3
            val row = index / 3
            val x = (20 + col * 210).dp
            val staggerY = if (col == 1) 35.dp else 0.dp
            val y = (24 + row * 245).dp + staggerY
            Triple(x, y, bubbleSize)
        }
    }
}
