package echo.driver.testgameopengl.world.model

import echo.driver.testgameopengl.world.Color

data class Tree(
    override val squareSize: Float = BIG_SIZE,
    override val x: Float,
    override val y: Float,
    override val color: Color,
): Square(squareSize, x, y, color)