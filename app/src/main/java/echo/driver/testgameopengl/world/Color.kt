package echo.driver.testgameopengl.world

data class Color(
    val r: Float,
    val g: Float,
    val b: Float,
    val a: Float,
) {
    companion object {
        val SKY = Color(0.5f, 0.7f, 1.0f, 1.0f)

        val WHITE = Color(1.0f, 1.0f, 1.0f, 1.0f)
        val BLACK = Color(0.0f, 0.0f, 0.0f, 1.0f)

        val RED = Color(1.0f, 0.0f, 0.0f, 1.0f)
        val DARK_RED = Color(0.5f, 0.0f, 0.0f, 1.0f)
        val LIGHT_RED = Color(1.0f, 0.5f, 0.5f, 1.0f)

        val GREEN = Color(0.0f, 1.0f, 0.0f, 1.0f)
        val GRASS = Color(0.0f, 0.6f, 0.0f, 1.0f)
        val DARK_GREEN = Color(0.0f, 0.5f, 0.0f, 1.0f)
        val LIGHT_GREEN = Color(0.5f, 1.0f, 0.5f, 1.0f)
        val TREE_GREEN = Color(0.3f, 0.4f, 0.2f, 1.0f)

        val BLUE = Color(0.0f, 0.0f, 1.0f, 1.0f)
        val DARK_BLUE = Color(0.0f, 0.0f, 0.5f, 1.0f)
        val LIGHT_BLUE = Color(0.5f, 0.5f, 1.0f, 1.0f)
        val SKY_BLUE = Color(0.5f, 0.7f, 1.0f, 1.0f)
        val NAVY_BLUE = Color(0.0f, 0.0f, 0.3f, 1.0f)

        val YELLOW = Color(1.0f, 1.0f, 0.0f, 1.0f)
        val DARK_YELLOW = Color(0.5f, 0.5f, 0.0f, 1.0f)
        val LIGHT_YELLOW = Color(1.0f, 1.0f, 0.5f, 1.0f)

        val ORANGE = Color(1.0f, 0.5f, 0.0f, 1.0f)
        val DARK_ORANGE = Color(0.8f, 0.4f, 0.0f, 1.0f)
        val LIGHT_ORANGE = Color(1.0f, 0.7f, 0.3f, 1.0f)

        val PURPLE = Color(0.5f, 0.0f, 0.5f, 1.0f)
        val DARK_PURPLE = Color(0.3f, 0.0f, 0.3f, 1.0f)
        val LIGHT_PURPLE = Color(0.7f, 0.3f, 0.7f, 1.0f)

        val PINK = Color(1.0f, 0.0f, 1.0f, 1.0f)
        val LIGHT_PINK = Color(1.0f, 0.5f, 1.0f, 1.0f)
        val DARK_PINK = Color(0.8f, 0.0f, 0.8f, 1.0f)

        val BROWN = Color(0.6f, 0.4f, 0.2f, 1.0f)
        val DARK_BROWN = Color(0.4f, 0.2f, 0.1f, 1.0f)
        val LIGHT_BROWN = Color(0.8f, 0.6f, 0.4f, 1.0f)

        val GRAY = Color(0.5f, 0.5f, 0.5f, 1.0f)
        val DARK_GRAY = Color(0.3f, 0.3f, 0.3f, 1.0f)
        val LIGHT_GRAY = Color(0.7f, 0.7f, 0.7f, 1.0f)

        val CYAN = Color(0.0f, 1.0f, 1.0f, 1.0f)
        val DARK_CYAN = Color(0.0f, 0.5f, 0.5f, 1.0f)
        val LIGHT_CYAN = Color(0.5f, 1.0f, 1.0f, 1.0f)

        val MAGENTA = Color(1.0f, 0.0f, 1.0f, 1.0f)
        val DARK_MAGENTA = Color(0.5f, 0.0f, 0.5f, 1.0f)
        val LIGHT_MAGENTA = Color(1.0f, 0.5f, 1.0f, 1.0f)

        val GOLD = Color(1.0f, 0.84f, 0.0f, 1.0f)
        val SILVER = Color(0.75f, 0.75f, 0.75f, 1.0f)
        val BRONZE = Color(0.8f, 0.5f, 0.2f, 1.0f)

        val TRANSPARENT = Color(0.0f, 0.0f, 0.0f, 0.0f)

        fun randomGreen() = TREE_GREEN//if(randomBoolean()) GRASS else DARK_GREEN
    }
}