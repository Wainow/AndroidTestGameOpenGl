package echo.driver.testgameopengl.world.model

import echo.driver.testgameopengl.utils.nextGaussian
import echo.driver.testgameopengl.utils.randomFloatInRange
import echo.driver.testgameopengl.world.Color
import kotlin.random.Random

class Friend(
    override val squareSize: Float = NORMAL_SIZE,
    override var x: Float,
    override var y: Float,
    override var color: Color = Color.SKY_BLUE,
    override var health: Float = 100f,
    override val damage: Float = 1f,
    override val speed: Float = 0.1f,
    override val visionRadius: Float = 10f,
    override val onDie: (Mob) -> Unit = {}
) : Mob(squareSize = squareSize, x = x, y = y, color = color) {
    private var target: Square? = null

    companion object {
        fun randomFriend(x: Float, y: Float, onDie: (Mob) -> Unit = {}): Friend {
            // Параметры нормального распределения для отклонений
            val healthStdDev = 30f  // Стандартное отклонение для здоровья
            val damageStdDev = 1.5f  // Стандартное отклонение для урона
            val speedStdDev = 0.3f  // Стандартное отклонение для скорости
            val visionRadiusStdDev = 3f  // Стандартное отклонение для радиуса видимости

            // Генерация случайных значений с нормальным распределением вокруг дефолтных значений
            val randomHealth = (100f + nextGaussian() * healthStdDev).coerceIn(50f, 150f)
            val randomDamage = (1f + nextGaussian() * damageStdDev).coerceIn(1f, 50f)
            val randomSpeed = (0.1f + nextGaussian() * speedStdDev).coerceIn(0.05f, 0.2f)
            val randomVisionRadius = (nextGaussian() * visionRadiusStdDev).coerceIn(1f, 20f)

            // Цвет для друга (с возможностью случайного отклонения)
            val randomColor = if (Random.nextFloat() < 0.05) { // 5% шанс случайного цвета
                Color(
                    Random.nextFloat(),
                    Random.nextFloat(),
                    Random.nextFloat(),
                    1.0f
                ) // случайный цвет
            } else {
                Color.SKY_BLUE // Default color
            }

            return Friend(
                x = x / 0.3f,
                y = y / 0.3f,
                health = randomHealth,
                damage = randomDamage,
                speed = randomSpeed,
                visionRadius = randomVisionRadius,
                color = randomColor,
                onDie = onDie,
            )
        }
    }

    fun tryToKill(enemy: Enemy?) {
        when (target) {
            null -> {
                target = if (enemy != null) {
                    if (isSeeing(enemy)) enemy
                    else randomSquare(10f)
                } else {
                    randomSquare(10f)
                }
            }
            is Enemy -> {
                (target as Enemy).let { t ->
                    if (t.isAlive()) {
                        moveTo(t) { attack(t) }
                    } else {
                        target = null
                    }
                }
            }
            is Square -> {
                if (enemy != null && isSeeing(enemy)) {
                    target = enemy
                } else {
                    moveTo(target!!) {
                        target = null
                    }
                }
            }
        }
    }

    private fun randomSquare(randomRange: Float) = Square(x = x + randomFloatInRange(randomRange), y = y + randomFloatInRange(randomRange))
}