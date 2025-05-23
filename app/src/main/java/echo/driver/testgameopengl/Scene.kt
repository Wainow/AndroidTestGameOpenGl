package echo.driver.testgameopengl

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import echo.driver.testgameopengl.render.MonitorManager
import echo.driver.testgameopengl.render.Render
import echo.driver.testgameopengl.render.Shader
import echo.driver.testgameopengl.world.Color
import echo.driver.testgameopengl.world.World
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class Scene(
    private val context: Context,
    onGameOver: () -> Unit,
    onHealthChanged: (Float) -> Unit,
) : GLSurfaceView.Renderer {
    private var program = 0
    private val world = World(onGameOver = onGameOver, onHealthChanged = onHealthChanged)
    private val shader = Shader()
    private val monitorManager = MonitorManager()
    private lateinit var render: Render

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig?) {
        val color = Color.TREE_GREEN
        GLES20.glClearColor(color.r, color.g, color.b, color.a)
        GLES20.glDisable(GLES20.GL_DEPTH_TEST)

        shader.initShader()
        program = GLES20.glCreateProgram().also {
            GLES20.glAttachShader(it, shader.vertexShader)
            GLES20.glAttachShader(it, shader.fragmentShader)
            GLES20.glLinkProgram(it)
        }
        render = Render(
            context,
            program,
            monitorManager,
        )
    }

    override fun onDrawFrame(unused: GL10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        TimeManager.updateTime() // Обновляем время
        TimeManager.onFrame {
            render.drawWorld(world, monitorManager.getProjectionViewMatrix())
            monitorManager.renderView(world.user)
        }
        TimeManager.onTickHappen {
            world.user.onTickHappened()
            world.enemies.forEach {
                it.onTickHappened()
            }
            world.friends.forEach {
                it.onTickHappened()
            }
            world.buildings.forEach {
                building -> building.onTickHappened()
            }
        }
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        monitorManager.initView(width, height)
    }

    fun updatePlayerPosition(x: Float, y: Float) {
        world.updateUserPosition(x, y)
    }

    fun userAttack() = world.userAttack()
}
