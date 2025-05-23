package echo.driver.testgameopengl

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import echo.driver.testgameopengl.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // Объявляем переменную для ViewBinding
    private lateinit var binding: ActivityMainBinding

    private lateinit var glSurfaceView: MyGLSurfaceView

    private var playerX = 0f
    private var playerY = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Используем ViewBinding для связывания макета
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root) // Устанавливаем корневой элемент из ViewBinding

        // Инициализируем GLSurfaceView
        glSurfaceView = MyGLSurfaceView(
            context = this,
            onGameOver = {
                binding.surfaceContainer.isInvisible = true
                binding.health.isInvisible = true
            },
            onHealthChanged = { runOnUiThread {
                binding.health.text = "Health $it"
            } }
        )

        // Добавляем glSurfaceView в контейнер surfaceContainer
        binding.surfaceContainer.addView(glSurfaceView)

        Handler(Looper.getMainLooper()).postDelayed(object : Runnable {
            override fun run() {
                val direction = binding.joystick.getDirection()
                playerX += direction.first * 0.05f // Умножаем для регулировки скорости
                playerY -= direction.second * 0.05f

                // Здесь обновляйте положение персонажа на экране
                glSurfaceView.updatePlayerPosition(playerX, playerY)

                // Повторяем обновление каждый кадр
                Handler(Looper.getMainLooper()).postDelayed(this, 16)
            }
        }, 16)

        binding.attackBtn.setOnClickListener {
            glSurfaceView.userAttack()
        }
    }

    override fun onResume() {
        super.onResume()
        glSurfaceView.onResume()
    }

    override fun onPause() {
        super.onPause()
        glSurfaceView.onPause()
    }
}