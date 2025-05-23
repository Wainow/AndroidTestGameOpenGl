package echo.driver.testgameopengl.utils

import echo.driver.testgameopengl.render.Frustum
import echo.driver.testgameopengl.world.model.Square
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sqrt
import kotlin.random.Random

fun randomBoolean() = (0..1).random() == 1

inline fun <reified T : Square> List<T>.filterIsVisible(frustum: Frustum): List<T> = filter { square -> frustum.isVisible(square) }
inline fun <reified T : Square> List<T>.forEachVisible(frustum: Frustum, action: (T) -> Unit) = filterIsVisible(frustum).forEach(action)

fun nextGaussian(): Float {
    val u1 = Random.nextFloat()
    val u2 = Random.nextFloat()
    val z0 = sqrt(-2.0 * kotlin.math.ln(u1.toDouble())) * cos(2.0 * PI * u2.toDouble())
    return z0.toFloat()
}

fun randomFloatInRange(n: Float): Float {
    return Random.nextFloat() * 2 * n - n
}