package echo.driver.testgameopengl

import android.annotation.SuppressLint
import android.content.Context
import android.opengl.GLSurfaceView

@SuppressLint("ViewConstructor")
class MyGLSurfaceView(
    context: Context,
    onGameOver: () -> Unit,
    onHealthChanged: (Float) -> Unit,
) : GLSurfaceView(context) {
    private var scene: Scene

    init {
        // Устанавливаем версию OpenGL ES
        setEGLContextClientVersion(2)
        // Назначаем Renderer
        scene = Scene(context, onGameOver = onGameOver, onHealthChanged = onHealthChanged)
        setRenderer(scene)
        renderMode = RENDERMODE_CONTINUOUSLY
    }

    fun updatePlayerPosition(x: Float, y: Float) {
        scene.updatePlayerPosition(x, y)
    }

    fun userAttack() = scene.userAttack()
}