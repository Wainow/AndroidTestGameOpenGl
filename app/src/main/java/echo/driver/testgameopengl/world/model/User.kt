package echo.driver.testgameopengl.world.model

import echo.driver.testgameopengl.world.Color
import kotlin.math.cos
import kotlin.math.sin

data class User(
    override val squareSize: Float = NORMAL_SIZE,
    override var x: Float,
    override var y: Float,
    override var color: Color = Color.BLUE,
    override var health: Float = 100f,
    override val onDie: (Mob) -> Unit = {},
    override var onHealthChanged: (Float) -> Unit = {},
    override val damage: Float = 3f,
    var items: MutableList<Item> = mutableListOf(),
): Mob(squareSize = squareSize, x = x, y = y, color = color, damage = damage) {

    var isMovingFront = false
    var isMovingBack = false
    var isMovingLeft = false
    var isMovingRight = false

    var attackAnimationSquares = mutableListOf(
        Square(squareSize = squareSize / 2, x = x, y = y, color = Color.ORANGE),
        Square(squareSize = squareSize / 2, x = x + squareSize, y = y, color = Color.ORANGE),
        Square(squareSize = squareSize / 2, x = x + 2 * squareSize, y = y, color = Color.ORANGE),
        Square(squareSize = squareSize / 2, x = x + 3 * squareSize, y = y, color = Color.ORANGE),
        Square(squareSize = squareSize / 2, x = x + 4 * squareSize, y = y, color = Color.ORANGE),
        Square(squareSize = squareSize / 2, x = x + 4 * squareSize, y = y, color = Color.ORANGE),
        Square(squareSize = squareSize / 2, x = x + 4 * squareSize, y = y, color = Color.ORANGE),
        Square(squareSize = squareSize / 2, x = x + 4 * squareSize, y = y, color = Color.ORANGE),
        Square(squareSize = squareSize / 2, x = x + 4 * squareSize, y = y, color = Color.ORANGE),
        Square(squareSize = squareSize / 2, x = x + 4 * squareSize, y = y, color = Color.ORANGE),
    )
    var currentAnimationSquare: Square? = null
    var animationFrameNumber: Int = -1
        set(value) {
            if (value < attackAnimationSquares.size - 1) {
                field = value
                currentAnimationSquare = attackAnimationSquares[value]
            } else {
                field = -1
                currentAnimationSquare = null
            }
        }
    fun isAnimationShowing() = currentAnimationSquare != null

    fun addItem(item: Item) {
        items.add(item)
    }

    fun update(x: Float, y: Float) {
        if (items.isNotEmpty()) {
            val multiplier = squareSize / items.first().squareSize
            val multiplier2 = squareSize / attackAnimationSquares.first().squareSize
            val stackLimit = 120  // Количество элементов в одном ряду
            val stackOffset = 0.5f  // Смещение следующего ряда по x
            this.items = items.mapIndexed { i, item ->
                val stackIndex = i / stackLimit  // Номер текущего стека
                val positionInStack = i % stackLimit  // Позиция в текущем стеке

                item.copy(
                    x = x * multiplier - 0.5f + stackIndex * stackOffset,  // Смещаем по x для каждого ряда
                    y = y * multiplier + (positionInStack * 0.1f)  // Позиция в текущем стеке по y
                )
            }.toMutableList()


            val radius = 16 * squareSize // Радиус "гуляния" вокруг игрока
            attackAnimationSquares = attackAnimationSquares.mapIndexed { i, item ->
                val angle = Math.toRadians((i * 360.0 / attackAnimationSquares.size + animationFrameNumber * 15) % 360) // Вращение с шагом
                val newX = x * multiplier2 + radius * cos(angle).toFloat()
                val newY = y * multiplier2 + radius * sin(angle).toFloat()
                Square(item.squareSize, newX, newY, item.color)
            }.toMutableList()
        }
        if (this.y > y) {
            isMovingFront = true
            isMovingBack = false
        } else if (this.y < y) {
            isMovingFront = false
            isMovingBack = true
        }
        this.x = x
        this.y = y
    }

    fun attack() {
        showAnimation()
    }

    override fun onTickHappened() {
        super.onTickHappened()
        if (isAnimationShowing()) showAnimation()
    }

    private fun showAnimation() = animationFrameNumber++

}