package echo.driver.testgameopengl.render

import android.graphics.RectF
import echo.driver.testgameopengl.world.model.Square

class Frustum {
    private var bounds = RectF()

    fun updateBounds(viewMatrix: FloatArray, compressionFactor: Float) {
        val mCompressionFactor = compressionFactor * 2.4f
        val left = -1f * mCompressionFactor
        val right = 1f * mCompressionFactor
        val bottom = -1f * mCompressionFactor
        val top = 1f * mCompressionFactor

        // Смещаем границы с учетом положения камеры
        val offsetX = -viewMatrix[12] // X-координата смещения камеры
        val offsetY = -viewMatrix[13] // Y-координата смещения камеры

        bounds = RectF(
            left + offsetX,
            top + offsetY,
            right + offsetX,
            bottom + offsetY,
        )
    }

    fun isVisible(square: Square): Boolean {
        val squareLeft = square.x * square.squareSize - square.squareSize / 2
        val squareRight = square.x * square.squareSize + square.squareSize / 2
        val squareBottom = square.y * square.squareSize - square.squareSize / 2
        val squareTop = square.y * square.squareSize + square.squareSize / 2

        return squareRight >= bounds.left &&
                squareLeft <= bounds.right &&
                squareTop >= bounds.bottom &&
                squareBottom <= bounds.top
    }
}