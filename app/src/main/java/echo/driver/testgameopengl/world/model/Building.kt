package echo.driver.testgameopengl.world.model

import echo.driver.testgameopengl.world.Color

class Building(
    override var squareSize: Float = NORMAL_SIZE,
    override val x: Float,
    override val y: Float,
    override var color: Color = Color.GOLD,
    val items: MutableList<Item> = mutableListOf(),
    var firstColor: Color = Color.YELLOW,
    var secondColor: Color = Color.GOLD,
    val finishedBuild: MutableList<Square> = mutableListOf(),
): Square(squareSize, x, y, color) {
    companion object {
        const val DEFAULT_AMOUNT_FOR_BUILD = 10
    }

    val buildingSquare = Square(x = (x * squareSize) / BIG_SIZE, y = (y * squareSize) / BIG_SIZE, squareSize = BIG_SIZE, color = Color.YELLOW)
    var isBuild = false
    private val amountForBuild = DEFAULT_AMOUNT_FOR_BUILD

    fun onTickHappened() {
        changeColor()
    }

    private fun changeColor() {
        color = if (color == firstColor) secondColor else firstColor
    }

    fun randomFriend(onDie: (Mob) -> Unit = {}) = Friend.randomFriend(globalX(), globalY(), onDie = onDie)

    fun addItems(user: User): Boolean {
        if (isBuild) return false
        val userCopy = user.copy()
        val userItems = userCopy.items.toMutableList()
        val addedCount = userItems.count()
        val count = items.count()
        val neededCount = amountForBuild - count

        if (addedCount >= neededCount) {
            val itemsToAdd = userItems.take(neededCount)
            items.addAll(itemsToAdd)
            user.items.removeAll(itemsToAdd)
            isBuild = true
            buildHouse()
            return true
        } else {
            userItems.forEachIndexed { i, item ->
                val multiplier = squareSize / item.squareSize
                val newItem = item.copy(x = x * multiplier, y = y * multiplier + (count * 0.1f) + (i * 0.1f))
                items.add(newItem)
            }
            user.items.clear()
            return false
        }
    }

    fun buildHouse() {
        if (isBuild) {
            val brickSize = squareSize / 2
            val brickSizeMultiplier = squareSize / brickSize

            // два маленьких блока над основным
            for (col in 0 until 2) {
                val square = Square(
                    squareSize = brickSize,
                    x = x * brickSizeMultiplier + (if (col == 0) -0.25f else 0.25f) * brickSizeMultiplier, // Центрируем по `x`
                    y = (y + 7.5f * squareSize) * brickSizeMultiplier, // Высота крыши
                    color = Color.RED
                )
                finishedBuild.add(square)
            }

            // основной блок
            val square = Square(
                squareSize = squareSize,
                x = x,
                y = y,
                color = Color.BROWN
            )
            finishedBuild.add(square)

            // два маленьких блока над сбоку от основного
            for (col in 0 until 2) {
                val square = Square(
                    squareSize = brickSize,
                    x = x * brickSizeMultiplier + (if (col == 0) -0.5f else 0.5f) * brickSizeMultiplier, // Центрируем по `x`
                    y = (y + 3 * squareSize) * brickSizeMultiplier, // Высота крыши
                    color = Color.RED
                )
                finishedBuild.add(square)
            }

            val doorSize = squareSize / 2.5f
            val doorSizeMultiplier = squareSize / doorSize
            // дверь внутри основного блока
            val door = Square(
                squareSize = doorSize,
                x = x * doorSizeMultiplier,
                y = (y - 2.5f * squareSize) * doorSizeMultiplier, // Высота крыши
                color = Color.ORANGE
            )
            finishedBuild.add(door)
        }
    }
}