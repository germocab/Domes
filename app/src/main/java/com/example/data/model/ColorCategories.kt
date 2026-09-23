package com.example.data.model

data class PredefinedColor(
    val name: String,
    val hex: String
)

data class ColorCategory(
    val title: String,
    val colors: List<PredefinedColor>
)

object ColorCategoriesData {
    val categories: List<ColorCategory> = listOf(
        ColorCategory(
            title = "Your latest choices",
            colors = listOf(
                PredefinedColor("Lime Green", "78C236"),
                PredefinedColor("Bright Orange", "FF4900"),
                PredefinedColor("Electric Blue", "1105D7"),
                PredefinedColor("Pale Sage", "BDE8BE"),
                PredefinedColor("Fern Green", "64A666"),
                PredefinedColor("Jet Black", "1F2321")
            )
        ),
        ColorCategory(
            title = "This season",
            colors = listOf(
                PredefinedColor("Mellow Yellow", "E5E059"),
                PredefinedColor("Olive Drab", "8E8221"),
                PredefinedColor("Leaf Green", "3E992C"),
                PredefinedColor("Deep Forest", "125619"),
                PredefinedColor("Dark Eggplant", "3A2352"),
                PredefinedColor("Teal Cyan", "12A89D")
            )
        ),
        ColorCategory(
            title = "Warm",
            colors = listOf(
                PredefinedColor("Ochre", "D4822B"),
                PredefinedColor("Coral Rose", "E92C47"),
                PredefinedColor("Canary Yellow", "EAE256"),
                PredefinedColor("Dijon Mustard", "A2A332"),
                PredefinedColor("Vibrant Red", "E51212"),
                PredefinedColor("Sunset Orange", "F27A18")
            )
        ),
        ColorCategory(
            title = "Cold",
            colors = listOf(
                PredefinedColor("Cobalt Blue", "1505D7"),
                PredefinedColor("Mint Aqua", "75F9D6"),
                PredefinedColor("Spring Green", "1CE26B"),
                PredefinedColor("Vivid Cyan", "00D2C4"),
                PredefinedColor("Deep Indigo", "3214A8"),
                PredefinedColor("Ocean Wave", "0077D7")
            )
        ),
        ColorCategory(
            title = "Nature",
            colors = listOf(
                PredefinedColor("Kelly Green", "12E04C"),
                PredefinedColor("Seafoam", "76F69D"),
                PredefinedColor("Sky Blue", "279FD1"),
                PredefinedColor("Tangerine", "FA7D00"),
                PredefinedColor("Moss Green", "369E0A"),
                PredefinedColor("Sage Leaf", "8FBC8F")
            )
        ),
        ColorCategory(
            title = "Pastel",
            colors = listOf(
                PredefinedColor("Pastel Pink", "FFB3BA"),
                PredefinedColor("Pastel Blue", "BAE1FF"),
                PredefinedColor("Pastel Green", "BAFFC9"),
                PredefinedColor("Pastel Yellow", "FFFFBA"),
                PredefinedColor("Pastel Lilac", "E8BAFF"),
                PredefinedColor("Pastel Peach", "FFDFBA")
            )
        ),
        ColorCategory(
            title = "Neon",
            colors = listOf(
                PredefinedColor("Neon Green", "39FF14"),
                PredefinedColor("Neon Red", "FF073A"),
                PredefinedColor("Electric Cyan", "04D9FF"),
                PredefinedColor("Neon Pink", "FF6EC7"),
                PredefinedColor("Neon Yellow", "FAED26"),
                PredefinedColor("Neon Purple", "BC13FE")
            )
        ),
        ColorCategory(
            title = "Monochrome",
            colors = listOf(
                PredefinedColor("Obsidian Black", "121619"),
                PredefinedColor("Charcoal", "333333"),
                PredefinedColor("Medium Gray", "666666"),
                PredefinedColor("Silver Gray", "999999"),
                PredefinedColor("Light Mist", "D1D5DB"),
                PredefinedColor("Pure White", "FFFFFF")
            )
        )
    )
}
