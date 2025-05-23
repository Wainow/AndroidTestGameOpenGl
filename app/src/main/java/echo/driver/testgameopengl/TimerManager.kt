package echo.driver.testgameopengl

import android.util.Log

object TimeManager {
    private const val FIXED_TIME_STEP = 1.0 / 10.0 // 60 FPS - фиксированный шаг
    private var accumulator = 0.0
    private var lastTime = System.nanoTime()

    fun updateTime() {
        val now = System.nanoTime()
        val delta = (now - lastTime) / 1_000_000_000.0 // Время в секундах
        lastTime = now

        accumulator += delta
    }

    fun onTickHappen(onTime: () -> Unit) {
        while (accumulator >= FIXED_TIME_STEP) {
            accumulator -= FIXED_TIME_STEP
            onTime() // Вызываем лямбда-функцию с фиксированным шагом
        }
    }

    fun onFrame(onTime: () -> Unit) {
        onTime() // Просто вызываем лямбду, без зависимостей от FPS
    }
}