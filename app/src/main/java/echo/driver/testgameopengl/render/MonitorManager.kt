package echo.driver.testgameopengl.render

import android.opengl.Matrix
import echo.driver.testgameopengl.world.model.Square

class MonitorManager {
    val viewMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)

    var compressionFactor = 0.5f

    fun initView(width: Int, height: Int) {
        val aspectRatio: Float = width.toFloat() / height.toFloat()

        val left = -1f * aspectRatio * compressionFactor
        val right = 1f * aspectRatio * compressionFactor
        val bottom = -1f * compressionFactor
        val top = 1f * compressionFactor
        Matrix.setIdentityM(projectionMatrix, 0)
        Matrix.orthoM(projectionMatrix, 0, left, right, bottom, top, -1f, 1f)
    }

    /**
     * Метод рендеринга камеры и объекта.
     * Камера следит за пользователем и всегда находится в центре экрана.
     */
    fun renderView(user: Square) {
        Matrix.setIdentityM(viewMatrix, 0)
        val offsetX = -user.x * user.squareSize
        val offsetY = -user.y * user.squareSize
        Matrix.translateM(viewMatrix, 0, offsetX, offsetY, 0f)
    }

    fun getProjectionViewMatrix(): FloatArray {
        val resultMatrix = FloatArray(16)
        Matrix.multiplyMM(resultMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
        return resultMatrix
    }
}
