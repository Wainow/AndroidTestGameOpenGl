package echo.driver.testgameopengl.world.model

import android.opengl.Matrix
import echo.driver.testgameopengl.world.Color
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

open class Square(
    open val squareSize: Float = NORMAL_SIZE,
    override val x: Float,
    override val y: Float,
    open val color: Color = Color.randomGreen()
): Renderable(x, y) {
    private val modelMatrix = FloatArray(16)
    val mvpMatrix = FloatArray(16)

    companion object {
        fun List<Square>.findNearest(square: Square) = minByOrNull { enemy ->
            val dx = enemy.x - square.x
            val dy = enemy.y - square.y
            dx * dx + dy * dy
        }

        const val SMALL_SIZE = 0.05f
        const val NORMAL_SIZE = 0.3f
        const val BIG_SIZE = 0.6f
    }

    fun globalX() = x * squareSize
    fun globalY() = y * squareSize
    fun toLocalX(x: Float) = x / squareSize
    fun toLocalY(y: Float) = y / squareSize
    fun toGlobalCoordinates() = Pair(globalX(), globalY())

    private fun vertices() = floatArrayOf(
        -0.5f * squareSize, -0.5f * squareSize, 0f,
        0.5f * squareSize, -0.5f * squareSize, 0f,
        0.5f * squareSize, 0.5f * squareSize, 0f,
        -0.5f * squareSize, 0.5f * squareSize, 0f
    )

    fun vertexBuffer(): FloatBuffer =
        ByteBuffer.allocateDirect(vertices().size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer().apply {
                put(vertices())
                position(0)
            }

    fun getTextureCoordinateBuffer(): FloatBuffer {
        val textureCoords = floatArrayOf(
            0f, 1f, // Нижний левый угол
            1f, 1f, // Нижний правый угол
            1f, 0f, // Верхний правый угол
            0f, 0f  // Верхний левый угол
        )
        return ByteBuffer.allocateDirect(textureCoords.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer().apply {
                put(textureCoords)
                position(0)
            }
    }

    fun isCollidingWith(square: Square): Boolean {
        val (userGlobalX, userGlobalY) = square.toGlobalCoordinates()
        val (itemGlobalX, itemGlobalY) = toGlobalCoordinates()

        val userLeft = userGlobalX - square.squareSize / 2
        val userRight = userGlobalX + square.squareSize / 2
        val userBottom = userGlobalY - square.squareSize / 2
        val userTop = userGlobalY + square.squareSize / 2

        val itemLeft = itemGlobalX - squareSize / 2
        val itemRight = itemGlobalX + squareSize / 2
        val itemBottom = itemGlobalY - squareSize / 2
        val itemTop = itemGlobalY + squareSize / 2

        return !(itemRight < userLeft || itemLeft > userRight || itemBottom > userTop || itemTop < userBottom)
    }

    fun updateMatrices(projectionViewMatrix: FloatArray) {
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.translateM(modelMatrix, 0, x * squareSize, y * squareSize, 0f)
        Matrix.multiplyMM(mvpMatrix, 0, projectionViewMatrix, 0, modelMatrix, 0)
    }
}
