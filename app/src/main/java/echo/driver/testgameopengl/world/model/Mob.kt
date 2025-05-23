package echo.driver.testgameopengl.world.model

import echo.driver.testgameopengl.world.Color
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

open class Mob(
    override val squareSize: Float = 0.1f,
    override var x: Float,
    override var y: Float,
    override var color: Color = Color.SKY_BLUE,
    var currentColor: Color = color,
    private val damageColor: Color = Color.RED,
    open var health: Float = 100f,
    open val damage: Float = 1f,
    open val speed: Float = 0.7f,
    open val visionRadius: Float = 10f,
    open val onDie: (Mob) -> Unit = {},
    open var onHealthChanged: (Float) -> Unit = {}
): Square(squareSize, x, y, color) {
    private var canDecreaseHealth = true

    fun moveTo(square: Square, speed: Float = this.speed, action: (Square) -> Unit = {}) {
        val globalX = square.globalX()
        val globalY = square.globalY()

        val localX = toLocalX(globalX)
        val localY = toLocalY(globalY)

        val angle = atan2(localY - y, localX - x)

        val newX = x + cos(angle) * speed
        val newY = y + sin(angle) * speed

        val distanceToUser = sqrt(((newX - localX) * (newX - localX) + (newY - localY) * (newY - localY)).toDouble()).toFloat()
        if (distanceToUser > squareSize / 2 + square.squareSize / 2) {
            x = newX
            y = newY
        } else {
            action(square)
        }
    }

    open fun onTickHappened() {
        restoreHealth()
    }

    private fun onDamaged() {
        currentColor = damageColor
        canDecreaseHealth = false
    }

    private fun restoreHealth() {
        currentColor = color
        canDecreaseHealth = true
    }

    fun changeHealth(value: Float) {
        if (canDecreaseHealth) {
            if (value < health) onDamaged()
            if (value <= 0f) onDie(this)
            health = value
            onHealthChanged(value)
        }
    }

    fun attack(mob: Mob) {
        mob.changeHealth(mob.health - damage)
        //push(userCube)
    }

    fun isSeeing(square: Square): Boolean {
        val (userGlobalX, userGlobalY) = square.toGlobalCoordinates()
        val (itemGlobalX, itemGlobalY) = toGlobalCoordinates()

        val userLeft = userGlobalX - square.squareSize / 2
        val userRight = userGlobalX + square.squareSize / 2
        val userBottom = userGlobalY - square.squareSize / 2
        val userTop = userGlobalY + square.squareSize / 2

        val itemLeft = itemGlobalX - (visionRadius + squareSize / 2)
        val itemRight = itemGlobalX + (visionRadius + squareSize / 2)
        val itemBottom = itemGlobalY - (visionRadius + squareSize / 2)
        val itemTop = itemGlobalY + (visionRadius + squareSize / 2)

        return !(itemRight < userLeft || itemLeft > userRight || itemBottom > userTop || itemTop < userBottom)
    }

    fun checkVision(square: Square, action: (Square) -> Unit) {
        if (isSeeing(square)) {
            action(square)
        }
    }

    fun isAlive() = health > 0
}