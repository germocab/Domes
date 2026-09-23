package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DomeColorEntity
import com.example.ui.components.AddColorBottomSheet
import com.example.ui.components.CircularBackButton
import com.example.ui.components.ColorPillsRow
import com.example.ui.theme.InterFontFamily
import com.example.ui.viewmodel.DomesViewModel
import com.example.util.ColorUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewDomeScreen(
    viewModel: DomesViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var domeName by remember { mutableStateOf("") }
    val addedColors = remember { mutableStateListOf<DomeColorEntity>() }
    var selectedColorIndex by remember { mutableIntStateOf(0) }
    var showAddColorSheet by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7F8FA)),
        containerColor = Color(0xFFF7F8FA),
        bottomBar = {
            // Save -> Button ALWAYS pinned at the bottom of the screen for one-hand reach
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF7F8FA))
                    .navigationBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Button(
                    onClick = {
                        val nameToSave = if (domeName.isBlank()) "My first dome" else domeName
                        viewModel.createDome(nameToSave, addedColors) {
                            onNavigateBack()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(28.dp),
                            ambientColor = Color(0x20000000),
                            spotColor = Color(0x30000000)
                        )
                        .testTag("save_dome_button"),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF121619),
                        contentColor = Color.White
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Save",
                            fontSize = 17.sp,
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Save",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .statusBarsPadding()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Header: Back Button + "New Dome"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularBackButton(onBack = onNavigateBack)
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "New Dome",
                    fontSize = 28.sp,
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF121619)
                )
            }

            // Dome Name Input: Pill shaped outlined text field with subtle shadow and gray stroke
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(27.dp),
                        ambientColor = Color(0x12000000),
                        spotColor = Color(0x18000000)
                    )
                    .clip(RoundedCornerShape(27.dp))
                    .background(Color.White)
                    .border(1.2.dp, Color(0xFFE2E4E8), RoundedCornerShape(27.dp))
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (domeName.isEmpty()) {
                    Text(
                        text = "My first dome",
                        color = Color(0xFF6B7280),
                        fontSize = 16.sp,
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                BasicTextField(
                    value = domeName,
                    onValueChange = { domeName = it },
                    textStyle = TextStyle(
                        color = Color(0xFF121619),
                        fontSize = 16.sp,
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.SemiBold
                    ),
                    singleLine = true,
                    cursorBrush = SolidColor(Color(0xFF121619)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_dome_name")
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Large White Middle Card with Drop Shadow and Gray Stroke
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 12.dp,
                        shape = RoundedCornerShape(36.dp),
                        ambientColor = Color(0x20000000),
                        spotColor = Color(0x2D000000)
                    )
                    .clip(RoundedCornerShape(36.dp))
                    .background(Color.White)
                    .border(1.2.dp, Color(0xFFE2E4E8), RoundedCornerShape(36.dp))
                    .padding(vertical = 36.dp, horizontal = 20.dp)
            ) {
                if (addedColors.isEmpty()) {
                    // Empty State: Gray circle with "+" and "Add a color"
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = { showAddColorSheet = true }
                            )
                            .testTag("empty_add_color_card"),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(210.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFD6D8DC)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add a color",
                                tint = Color(0xFF333333),
                                modifier = Modifier.size(52.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(28.dp))

                        Text(
                            text = "Add a color",
                            fontSize = 20.sp,
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF374151)
                        )
                    }
                } else {
                    // Color Selected State
                    val safeIndex = selectedColorIndex.coerceIn(0, addedColors.lastIndex)
                    val activeColor = addedColors[safeIndex]
                    val parsedColor = ColorUtils.hexToColor(activeColor.hex)

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Large Color Circle with soft shadow
                        Box(
                            modifier = Modifier
                                .size(210.dp)
                                .shadow(
                                    elevation = 10.dp,
                                    shape = CircleShape,
                                    ambientColor = Color(0x20000000),
                                    spotColor = Color(0x2D000000)
                                )
                                .clip(CircleShape)
                                .background(parsedColor)
                                .border(1.dp, Color(0x1F000000), CircleShape)
                                .clickable { showAddColorSheet = true }
                                .testTag("selected_color_circle")
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Color Name (in that color)
                        Text(
                            text = activeColor.name,
                            fontSize = 24.sp,
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.Black,
                            color = parsedColor,
                            modifier = Modifier.testTag("color_name_label")
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // HEX, R, G, B Pills
                        ColorPillsRow(
                            hex = activeColor.hex,
                            r = activeColor.r,
                            g = activeColor.g,
                            b = activeColor.b
                        )

                        // If multiple colors, show pagination chips
                        if (addedColors.size > 1) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                addedColors.forEachIndexed { idx, col ->
                                    val isCur = idx == safeIndex
                                    Box(
                                        modifier = Modifier
                                            .size(if (isCur) 14.dp else 10.dp)
                                            .clip(CircleShape)
                                            .background(ColorUtils.hexToColor(col.hex))
                                            .border(
                                                width = if (isCur) 2.dp else 0.dp,
                                                color = Color(0xFF121619),
                                                shape = CircleShape
                                            )
                                            .clickable { selectedColorIndex = idx }
                                    )
                                }
                            }
                        }
                    }

                    // Floating "+" Button on Right Side of Card with drop shadow and gray stroke
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .offset(x = 6.dp)
                            .size(50.dp)
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
                                indication = ripple(bounded = true, radius = 25.dp),
                                onClick = { showAddColorSheet = true }
                            )
                            .testTag("add_another_color_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Color",
                            tint = Color(0xFF121619),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // Add Color Sheet
        if (showAddColorSheet) {
            AddColorBottomSheet(
                onDismiss = { showAddColorSheet = false },
                onColorAdded = { name, hex, r, g, b ->
                    val newCol = DomeColorEntity(
                        domeId = 0,
                        name = name,
                        hex = hex,
                        r = r,
                        g = g,
                        b = b,
                        sortOrder = addedColors.size
                    )
                    addedColors.add(newCol)
                    selectedColorIndex = addedColors.lastIndex
                    showAddColorSheet = false
                }
            )
        }
    }
}
