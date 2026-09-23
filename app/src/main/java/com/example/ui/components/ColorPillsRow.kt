package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.InterFontFamily
import com.example.util.ColorUtils

@Composable
fun ColorPillsRow(
    hex: String,
    r: Int,
    g: Int,
    b: Int,
    isEditable: Boolean = false,
    onColorChanged: ((hex: String, r: Int, g: Int, b: Int) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var showHexDialog by remember { mutableStateOf(false) }
    var showRgbDialog by remember { mutableStateOf(false) }
    var activeRgbChannel by remember { mutableStateOf("R") }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // HEX pill (larger width)
        PillItem(
            label = "HEX",
            value = hex.uppercase(),
            modifier = Modifier
                .weight(1.3f)
                .testTag("pill_hex"),
            onClick = {
                if (isEditable) showHexDialog = true
            }
        )

        // R pill
        PillItem(
            label = "R",
            value = r.toString(),
            modifier = Modifier
                .weight(1f)
                .testTag("pill_r"),
            onClick = {
                if (isEditable) {
                    activeRgbChannel = "R"
                    showRgbDialog = true
                }
            }
        )

        // G pill
        PillItem(
            label = "G",
            value = g.toString(),
            modifier = Modifier
                .weight(1f)
                .testTag("pill_g"),
            onClick = {
                if (isEditable) {
                    activeRgbChannel = "G"
                    showRgbDialog = true
                }
            }
        )

        // B pill
        PillItem(
            label = "B",
            value = b.toString(),
            modifier = Modifier
                .weight(1f)
                .testTag("pill_b"),
            onClick = {
                if (isEditable) {
                    activeRgbChannel = "B"
                    showRgbDialog = true
                }
            }
        )
    }

    // Hex Input Dialog
    if (showHexDialog && onColorChanged != null) {
        var inputHex by remember { mutableStateOf(hex) }
        AlertDialog(
            onDismissRequest = { showHexDialog = false },
            title = { Text("Enter HEX Code", fontWeight = FontWeight.Bold, fontFamily = InterFontFamily) },
            text = {
                OutlinedTextField(
                    value = inputHex,
                    onValueChange = { inputHex = it.take(6) },
                    label = { Text("Hex (e.g. D10F0F)", fontFamily = InterFontFamily) },
                    singleLine = true,
                    modifier = Modifier.testTag("input_hex_field")
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val clean = inputHex.removePrefix("#").trim()
                        if (clean.length == 6 && clean.all { it in "0123456789ABCDEFabcdef" }) {
                            val rgb = ColorUtils.hexToRgb(clean)
                            onColorChanged(clean.uppercase(), rgb.first, rgb.second, rgb.third)
                        }
                        showHexDialog = false
                    }
                ) {
                    Text("Apply", color = Color(0xFF121619), fontWeight = FontWeight.Bold, fontFamily = InterFontFamily)
                }
            },
            dismissButton = {
                TextButton(onClick = { showHexDialog = false }) {
                    Text("Cancel", color = Color.Gray, fontFamily = InterFontFamily)
                }
            }
        )
    }

    // RGB Input Dialog
    if (showRgbDialog && onColorChanged != null) {
        val initialVal = when (activeRgbChannel) {
            "R" -> r.toString()
            "G" -> g.toString()
            else -> b.toString()
        }
        var inputVal by remember { mutableStateOf(initialVal) }

        AlertDialog(
            onDismissRequest = { showRgbDialog = false },
            title = { Text("Enter $activeRgbChannel (0 - 255)", fontWeight = FontWeight.Bold, fontFamily = InterFontFamily) },
            text = {
                OutlinedTextField(
                    value = inputVal,
                    onValueChange = { inputVal = it.filter { ch -> ch.isDigit() }.take(3) },
                    label = { Text("$activeRgbChannel Value", fontFamily = InterFontFamily) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.testTag("input_rgb_field")
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val num = (inputVal.toIntOrNull() ?: 0).coerceIn(0, 255)
                        var newR = r
                        var newG = g
                        var newB = b
                        when (activeRgbChannel) {
                            "R" -> newR = num
                            "G" -> newG = num
                            "B" -> newB = num
                        }
                        val newHex = ColorUtils.rgbToHex(newR, newG, newB)
                        onColorChanged(newHex, newR, newG, newB)
                        showRgbDialog = false
                    }
                ) {
                    Text("Apply", color = Color(0xFF121619), fontWeight = FontWeight.Bold, fontFamily = InterFontFamily)
                }
            },
            dismissButton = {
                TextButton(onClick = { showRgbDialog = false }) {
                    Text("Cancel", color = Color.Gray, fontFamily = InterFontFamily)
                }
            }
        )
    }
}

@Composable
private fun PillItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = Color(0x18000000),
                spotColor = Color(0x22000000)
            )
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .border(
                width = 1.2.dp,
                color = Color(0xFFE2E4E8),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp, horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontFamily = InterFontFamily,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF121619)
        )
        Text(
            text = value,
            fontSize = 11.sp,
            fontFamily = InterFontFamily,
            color = Color(0xFF757575),
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}
