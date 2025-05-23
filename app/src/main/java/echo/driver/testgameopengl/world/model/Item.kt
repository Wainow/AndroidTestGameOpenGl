package echo.driver.testgameopengl.world.model

import echo.driver.testgameopengl.world.Color

data class Item(
    override val squareSize: Float = SMALL_SIZE,
    override val x: Float,
    override val y: Float,
    override val color: Color = Color.randomGreen(),
): Square(squareSize, x, y, color)