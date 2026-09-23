package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.AddColorBottomSheet
import com.example.ui.components.CircularBackButton
import com.example.ui.components.ColorPillsRow
import com.example.ui.theme.InterFontFamily
import com.example.ui.viewmodel.DomesViewModel
import com.example.util.ColorUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorDetailScreen(
    colorId: Long,
    viewModel: DomesViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorFlow = remember(colorId) { viewModel.getColorById(colorId) }
    val colorEntity by colorFlow.collectAsStateWithLifecycle(initialValue = null)

    var showEditSheet by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    if (colorEntity == null) {
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

    val color = colorEntity!!
    val parsedColor = ColorUtils.hexToColor(color.hex)

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7F8FA)),
        containerColor = Color(0xFFF7F8FA),
        bottomBar = {
            // Edit and Delete buttons ALWAYS pinned at the bottom of the screen for one-hand reach
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF7F8FA))
                    .navigationBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Edit Button (Solid Black Pill)
                Button(
                    onClick = { showEditSheet = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(28.dp),
                            ambientColor = Color(0x20000000),
                            spotColor = Color(0x30000000)
                        )
                        .testTag("edit_color_button"),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF121619),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Edit",
                        fontSize = 17.sp,
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Delete Button (Light Grey Pill with Gray Stroke and Red Text)
                Button(
                    onClick = { showDeleteConfirm = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .shadow(
                            elevation = 4.dp,
                            shape = RoundedCornerShape(28.dp),
                            ambientColor = Color(0x14000000),
                            spotColor = Color(0x1E000000)
                        )
                        .border(
                            width = 1.2.dp,
                            color = Color(0xFFE2E4E8),
                            shape = RoundedCornerShape(28.dp)
                        )
                        .testTag("delete_color_button"),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF3F4F6),
                        contentColor = Color(0xFFE53935)
                    )
                ) {
                    Text(
                        text = "Delete",
                        fontSize = 17.sp,
                        fontFamily = InterFontFamily,
                        fontWeight = FontWeight.Bold
                    )
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

            // Back Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                CircularBackButton(onBack = onNavigateBack)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Giant Circular Color Swatch with drop shadow
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .shadow(
                        elevation = 20.dp,
                        shape = CircleShape,
                        ambientColor = Color(0x35000000),
                        spotColor = Color(0x45000000)
                    )
                    .clip(CircleShape)
                    .background(parsedColor)
                    .border(1.2.dp, Color(0xFFE2E4E8), CircleShape)
                    .testTag("giant_color_swatch")
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Color Name in the matching color
            Text(
                text = color.name,
                fontSize = 32.sp,
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Black,
                color = parsedColor,
                modifier = Modifier.testTag("color_name_title")
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 4 Pill Badges: HEX, R, G, B
            ColorPillsRow(
                hex = color.hex,
                r = color.r,
                g = color.g,
                b = color.b
            )

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Edit Color Bottom Sheet
        if (showEditSheet) {
            AddColorBottomSheet(
                onDismiss = { showEditSheet = false },
                initialHex = color.hex,
                onColorAdded = { newName, newHex, newR, newG, newB ->
                    val updated = color.copy(
                        name = newName,
                        hex = newHex,
                        r = newR,
                        g = newG,
                        b = newB
                    )
                    viewModel.updateColor(updated)
                    showEditSheet = false
                }
            )
        }

        // Delete Confirmation Dialog
        if (showDeleteConfirm) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirm = false },
                title = { Text("Delete Color", fontWeight = FontWeight.Bold, fontFamily = InterFontFamily) },
                text = { Text("Are you sure you want to remove '${color.name}' from this Dome?", fontFamily = InterFontFamily) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDeleteConfirm = false
                            viewModel.deleteColor(color.id) {
                                onNavigateBack()
                            }
                        }
                    ) {
                        Text("Delete", color = Color(0xFFE53935), fontWeight = FontWeight.Bold, fontFamily = InterFontFamily)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteConfirm = false }) {
                        Text("Cancel", color = Color.Gray, fontFamily = InterFontFamily)
                    }
                }
            )
        }
    }
}
