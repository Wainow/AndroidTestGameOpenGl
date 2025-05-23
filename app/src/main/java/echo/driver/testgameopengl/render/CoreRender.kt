package echo.driver.testgameopengl.render

import android.opengl.GLES20
import echo.driver.testgameopengl.world.Color
import echo.driver.testgameopengl.world.model.Square

class CoreRender(
    private val program: Int,
    var projectionMatrix: FloatArray,
) {
    private val mvpMatrixHandle: Int = GLES20.glGetUniformLocation(program, "uMVPMatrix")
    private val uColorHandle: Int = GLES20.glGetUniformLocation(program, "uColor")
    private val uTextureHandle: Int = GLES20.glGetUniformLocation(program, "uTexture")
    private val vPositionHandle: Int = GLES20.glGetAttribLocation(program, "vPosition")
    private val useTextureHandle: Int = GLES20.glGetUniformLocation(program, "useTexture")

    fun drawSquare(square: Square, color: Color = square.color) {
        square.updateMatrices(projectionMatrix)

        GLES20.glUseProgram(program)

        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, square.mvpMatrix, 0)
        GLES20.glUniform4f(uColorHandle, color.r, color.g, color.b, color.a)
        GLES20.glUniform1i(useTextureHandle, 0) // Отключаем текстуру

        GLES20.glVertexAttribPointer(
            vPositionHandle,
            3,
            GLES20.GL_FLOAT,
            false,
            3 * 4,
            square.vertexBuffer()
        )
        GLES20.glEnableVertexAttribArray(vPositionHandle)

        val texCoordHandle = GLES20.glGetAttribLocation(program, "aTexCoord")
        if (texCoordHandle >= 0) {
            GLES20.glDisableVertexAttribArray(texCoordHandle)
        }

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4)

        GLES20.glDisableVertexAttribArray(vPositionHandle)
    }

    fun drawSquare(square: Square, textureId: Int) {
        square.updateMatrices(projectionMatrix)

        GLES20.glUseProgram(program)

        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA)

        GLES20.glUniformMatrix4fv(mvpMatrixHandle, 1, false, square.mvpMatrix, 0)
        GLES20.glUniform1i(useTextureHandle, 1) // Включаем текстуру

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
        GLES20.glUniform1i(uTextureHandle, 0)

        GLES20.glVertexAttribPointer(
            vPositionHandle,
            3,
            GLES20.GL_FLOAT,
            false,
            3 * 4,
            square.vertexBuffer()
        )
        GLES20.glEnableVertexAttribArray(vPositionHandle)

        val texCoordHandle = GLES20.glGetAttribLocation(program, "aTexCoord")
        GLES20.glVertexAttribPointer(
            texCoordHandle,
            2,
            GLES20.GL_FLOAT,
            false,
            2 * 4,
            square.getTextureCoordinateBuffer()
        )
        GLES20.glEnableVertexAttribArray(texCoordHandle)

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4)

        GLES20.glDisableVertexAttribArray(vPositionHandle)
        GLES20.glDisableVertexAttribArray(texCoordHandle)
    }
}