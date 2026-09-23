package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ColorCategoriesData
import com.example.ui.theme.InterFontFamily
import com.example.util.ColorUtils

enum class ColorSheetTab {
    CATEGORIES,
    CREATE
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddColorBottomSheet(
    onDismiss: () -> Unit,
    onColorAdded: (name: String, hex: String, r: Int, g: Int, b: Int) -> Unit,
    initialHex: String = "D10F0F",
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
) {
    var currentTab by remember { mutableStateOf(ColorSheetTab.CATEGORIES) }

    // Color states for CREATE tab
    val initialRgb = remember(initialHex) { ColorUtils.hexToRgb(initialHex) }
    var currentR by remember { mutableIntStateOf(initialRgb.first) }
    var currentG by remember { mutableIntStateOf(initialRgb.second) }
    var currentB by remember { mutableIntStateOf(initialRgb.third) }
    var currentHex by remember { mutableStateOf(initialHex.uppercase()) }
    var currentColorName by remember { mutableStateOf(ColorUtils.findClosestColorName(currentR, currentG, currentB)) }

    val initialHsv = remember(initialHex) {
        val c = ColorUtils.hexToColor(initialHex)
        ColorUtils.colorToHsv(c)
    }
    var hue by remember { mutableFloatStateOf(initialHsv.first) }
    var saturation by remember { mutableFloatStateOf(initialHsv.second) }
    var value by remember { mutableFloatStateOf(initialHsv.third) }

    fun updateFromHsv(newHue: Float, newSat: Float, newVal: Float) {
        hue = newHue
        saturation = newSat
        value = newVal
        val color = ColorUtils.hsvToColor(newHue, newSat, newVal)
        val hex = ColorUtils.colorToHex(color)
        val rgb = ColorUtils.hexToRgb(hex)
        currentHex = hex
        currentR = rgb.first
        currentG = rgb.second
        currentB = rgb.third
        currentColorName = ColorUtils.findClosestColorName(currentR, currentG, currentB)
    }

    fun updateFromHexRgb(hex: String, r: Int, g: Int, b: Int) {
        currentHex = hex
        currentR = r
        currentG = g
        currentB = b
        currentColorName = ColorUtils.findClosestColorName(r, g, b)
        val color = Color(r, g, b)
        val hsv = ColorUtils.colorToHsv(color)
        hue = hsv.first
        saturation = hsv.second
        value = hsv.third
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        containerColor = Color.White,
        dragHandle = null,
        modifier = Modifier.testTag("add_color_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header Row: X on left, "New color" in center
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                // Rounded X button with drop shadow and gray stroke
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .align(Alignment.CenterStart)
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
                            indication = ripple(bounded = true, radius = 21.dp),
                            onClick = onDismiss
                        )
                        .testTag("close_sheet_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close",
                        tint = Color(0xFF121619),
                        modifier = Modifier.size(20.dp)
                    )
                }

                Text(
                    text = "New color",
                    fontSize = 22.sp,
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF121619),
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // Segmented Control Pill: Categories | Create
            Box(
                modifier = Modifier
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(24.dp),
                        ambientColor = Color(0x14000000),
                        spotColor = Color(0x1E000000)
                    )
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White)
                    .border(1.2.dp, Color(0xFFE2E4E8), RoundedCornerShape(24.dp))
                    .padding(3.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    // Categories Tab
                    val isCategories = currentTab == ColorSheetTab.CATEGORIES
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isCategories) Color(0xFF121619) else Color.Transparent)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(bounded = true, radius = 40.dp),
                                onClick = { currentTab = ColorSheetTab.CATEGORIES }
                            )
                            .padding(horizontal = 20.dp, vertical = 8.dp)
                            .testTag("tab_categories")
                    ) {
                        Text(
                            text = "Categories",
                            fontSize = 14.sp,
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = if (isCategories) Color.White else Color(0xFF121619)
                        )
                    }

                    // Create Tab
                    val isCreate = currentTab == ColorSheetTab.CREATE
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isCreate) Color(0xFF121619) else Color.Transparent)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(bounded = true, radius = 40.dp),
                                onClick = { currentTab = ColorSheetTab.CREATE }
                            )
                            .padding(horizontal = 22.dp, vertical = 8.dp)
                            .testTag("tab_create")
                    ) {
                        Text(
                            text = "Create",
                            fontSize = 14.sp,
                            fontFamily = InterFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = if (isCreate) Color.White else Color(0xFF121619)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Body depending on tab
            when (currentTab) {
                ColorSheetTab.CATEGORIES -> {
                    // Categories List
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(440.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        ColorCategoriesData.categories.forEach { category ->
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = category.title,
                                    fontSize = 17.sp,
                                    fontFamily = InterFontFamily,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF1E293B),
                                    modifier = Modifier.padding(bottom = 10.dp)
                                )

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .horizontalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    category.colors.forEach { pred ->
                                        val c = ColorUtils.hexToColor(pred.hex)
                                        Box(
                                            modifier = Modifier
                                                .size(46.dp)
                                                .shadow(3.dp, CircleShape)
                                                .clip(CircleShape)
                                                .background(c)
                                                .border(
                                                    width = 1.dp,
                                                    color = Color(0x1F000000),
                                                    shape = CircleShape
                                                )
                                                .clickable {
                                                    val rgb = ColorUtils.hexToRgb(pred.hex)
                                                    onColorAdded(pred.name, pred.hex, rgb.first, rgb.second, rgb.third)
                                                }
                                                .testTag("color_chip_${pred.hex}")
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                ColorSheetTab.CREATE -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Preview circle + Color name
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(bottom = 20.dp)
                        ) {
                            val activeColor = ColorUtils.hsvToColor(hue, saturation, value)
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .shadow(4.dp, CircleShape)
                                    .clip(CircleShape)
                                    .background(activeColor)
                                    .border(1.dp, Color(0x1F000000), CircleShape)
                            )
                            Spacer(modifier = Modifier.width(14.dp))
                            Text(
                                text = currentColorName,
                                fontSize = 22.sp,
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.Black,
                                color = activeColor
                            )
                        }

                        // 2D Gradient picker and Hue slider
                        ColorPicker2D(
                            hue = hue,
                            saturation = saturation,
                            value = value,
                            onColorChanged = { newHue, newSat, newVal ->
                                updateFromHsv(newHue, newSat, newVal)
                            }
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // HEX, R, G, B Pill Badges (Editable)
                        ColorPillsRow(
                            hex = currentHex,
                            r = currentR,
                            g = currentG,
                            b = currentB,
                            isEditable = true,
                            onColorChanged = { newHex, newR, newG, newB ->
                                updateFromHexRgb(newHex, newR, newG, newB)
                            }
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // "Add color" Button (always reachable at bottom)
                        Button(
                            onClick = {
                                onColorAdded(currentColorName, currentHex, currentR, currentG, currentB)
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
                                .testTag("add_color_button"),
                            shape = RoundedCornerShape(28.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF121619),
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                text = "Add color",
                                fontSize = 16.sp,
                                fontFamily = InterFontFamily,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}
