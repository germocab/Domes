package com.example.util

import androidx.compose.ui.graphics.Color
import kotlin.math.abs
import kotlin.math.sqrt

object ColorUtils {

    fun hexToColor(hex: String): Color {
        val clean = hex.removePrefix("#").trim()
        val parsed = clean.toLongOrNull(16) ?: 0xFF0000
        return when (clean.length) {
            6 -> Color(
                red = ((parsed shr 16) and 0xFF).toInt(),
                green = ((parsed shr 8) and 0xFF).toInt(),
                blue = (parsed and 0xFF).toInt(),
                alpha = 255
            )
            8 -> Color(
                alpha = ((parsed shr 24) and 0xFF).toInt(),
                red = ((parsed shr 16) and 0xFF).toInt(),
                green = ((parsed shr 8) and 0xFF).toInt(),
                blue = (parsed and 0xFF).toInt()
            )
            else -> Color(0xFFD10F0F)
        }
    }

    fun colorToHex(color: Color): String {
        val r = (color.red * 255f).toInt().coerceIn(0, 255)
        val g = (color.green * 255f).toInt().coerceIn(0, 255)
        val b = (color.blue * 255f).toInt().coerceIn(0, 255)
        return "%02X%02X%02X".format(r, g, b)
    }

    fun rgbToHex(r: Int, g: Int, b: Int): String {
        val rc = r.coerceIn(0, 255)
        val gc = g.coerceIn(0, 255)
        val bc = b.coerceIn(0, 255)
        return "%02X%02X%02X".format(rc, gc, bc)
    }

    fun hexToRgb(hex: String): Triple<Int, Int, Int> {
        val clean = hex.removePrefix("#").trim()
        val num = clean.toLongOrNull(16) ?: 0
        val r = ((num shr 16) and 0xFF).toInt()
        val g = ((num shr 8) and 0xFF).toInt()
        val b = (num and 0xFF).toInt()
        return Triple(r, g, b)
    }

    fun isLightColor(color: Color): Boolean {
        // Relative luminance
        val lum = 0.299 * color.red + 0.587 * color.green + 0.114 * color.blue
        return lum > 0.6
    }

    fun hsvToColor(hue: Float, saturation: Float, value: Float): Color {
        val hsv = floatArrayOf(hue.coerceIn(0f, 360f), saturation.coerceIn(0f, 1f), value.coerceIn(0f, 1f))
        val colorInt = android.graphics.Color.HSVToColor(hsv)
        return Color(colorInt)
    }

    fun colorToHsv(color: Color): Triple<Float, Float, Float> {
        val r = (color.red * 255).toInt()
        val g = (color.green * 255).toInt()
        val b = (color.blue * 255).toInt()
        val hsv = FloatArray(3)
        android.graphics.Color.RGBToHSV(r, g, b, hsv)
        return Triple(hsv[0], hsv[1], hsv[2])
    }

    private data class NamedColor(val name: String, val r: Int, val g: Int, val b: Int)

    private val colorPaletteDatabase = listOf(
        NamedColor("Crimson Red", 209, 15, 15),
        NamedColor("Emerald Green", 14, 128, 68),
        NamedColor("Pine", 45, 71, 57),
        NamedColor("Obsidian", 18, 22, 25),
        NamedColor("Antique Gold", 229, 198, 135),
        NamedColor("Sage Khaki", 174, 170, 121),
        NamedColor("Barbie Pink", 255, 0, 85),
        NamedColor("Wine Berry", 107, 8, 54),
        NamedColor("Vivid Violet", 185, 0, 140),
        NamedColor("Bubblegum", 255, 123, 229),
        NamedColor("Deep Navy", 12, 16, 117),
        NamedColor("Cerulean Blue", 29, 120, 186),
        NamedColor("Deep Teal", 36, 66, 70),
        NamedColor("Amber Gold", 212, 139, 28),
        NamedColor("Olive Green", 122, 128, 15),
        NamedColor("Ruby Red", 220, 20, 60),
        NamedColor("Scarlet", 255, 36, 0),
        NamedColor("Coral", 255, 127, 80),
        NamedColor("Tangerine", 242, 133, 0),
        NamedColor("Marigold", 234, 162, 33),
        NamedColor("Mustard", 225, 173, 1),
        NamedColor("Lemon Yellow", 255, 244, 79),
        NamedColor("Lime Green", 50, 205, 50),
        NamedColor("Mint", 152, 255, 152),
        NamedColor("Forest Green", 34, 139, 34),
        NamedColor("Cyan", 0, 255, 255),
        NamedColor("Turquoise", 64, 224, 208),
        NamedColor("Aquamarine", 127, 255, 212),
        NamedColor("Sky Blue", 135, 206, 235),
        NamedColor("Royal Blue", 65, 105, 225),
        NamedColor("Indigo", 75, 0, 130),
        NamedColor("Lavender", 230, 230, 250),
        NamedColor("Magenta", 255, 0, 255),
        NamedColor("Hot Pink", 255, 105, 180),
        NamedColor("Blush", 222, 93, 131),
        NamedColor("Peach", 255, 218, 185),
        NamedColor("Terracotta", 226, 114, 91),
        NamedColor("Charcoal", 54, 69, 79),
        NamedColor("Slate Gray", 112, 128, 144),
        NamedColor("Silver", 192, 192, 192),
        NamedColor("Snow White", 250, 250, 250),
        NamedColor("Midnight Blue", 25, 25, 112),
        NamedColor("Lilac", 200, 162, 200),
        NamedColor("Periwinkle", 204, 204, 255),
        NamedColor("Copper", 184, 115, 51),
        NamedColor("Bronze", 205, 127, 50),
        NamedColor("Espresso", 75, 54, 33),
        NamedColor("Moss Green", 138, 154, 91)
    )

    fun findClosestColorName(r: Int, g: Int, b: Int): String {
        var closest = colorPaletteDatabase[0]
        var minDistance = Double.MAX_VALUE
        for (named in colorPaletteDatabase) {
            val dr = (named.r - r).toDouble()
            val dg = (named.g - g).toDouble()
            val db = (named.b - b).toDouble()
            val dist = sqrt(dr * dr + dg * dg + db * db)
            if (dist < minDistance) {
                minDistance = dist
                closest = named
            }
        }
        return closest.name
    }
}
