package echo.driver.testgameopengl.world

import echo.driver.testgameopengl.world.model.Building
import echo.driver.testgameopengl.world.model.Enemy
import echo.driver.testgameopengl.world.model.Friend
import echo.driver.testgameopengl.world.model.Item
import echo.driver.testgameopengl.world.model.Square
import echo.driver.testgameopengl.world.model.Square.Companion.BIG_SIZE
import echo.driver.testgameopengl.world.model.Square.Companion.NORMAL_SIZE
import echo.driver.testgameopengl.world.model.Square.Companion.SMALL_SIZE
import echo.driver.testgameopengl.world.model.Tree
import echo.driver.testgameopengl.world.model.User
import kotlin.random.Random

data class World(
    val size: Float = 30f,
    val onGameOver: () -> Unit,
    val onHealthChanged: (Float) -> Unit,
) {
    val earth = initSquares()
    var items = initRandom(1000) { x, y -> Item(x = x, y = y, squareSize = SMALL_SIZE, color = Color.YELLOW) }
    var trees = initRandom(4000) { x, y -> Tree(x = x, y = y, squareSize = BIG_SIZE, color = Color.DARK_BLUE) }
    var user = User(squareSize = NORMAL_SIZE, x = 0f, y = 0f, color = Color.BLUE, onDie = { onGameOver() }, onHealthChanged = { onHealthChanged(it) })
    var enemies = initRandom(100) { x, y -> Enemy(x = x, y = y, squareSize = NORMAL_SIZE, speed = 0.01f, color = Color.PINK, onDie = { removeEnemy(it as Enemy) }) }
    var buildings = initRandom(100) { x, y -> Building(x = x, y = y, squareSize = NORMAL_SIZE, color = Color.BRONZE, firstColor = Color.BROWN, secondColor = Color.BRONZE) }

    var friends = mutableListOf<Friend>()

    private fun <T: Square> initRandom(count: Int, createLogic: (x: Float, y: Float) -> T): MutableList<T> {
        val ties = mutableListOf<T>()
        repeat(count) {
            val randomX = Random.nextFloat() * size * 2 - size
            val randomY = Random.nextFloat() * size * 2 - size

            val newT = createLogic(randomX, randomY)
            ties.add(newT)
        }

        return ties
    }

    private fun initSquares(): List<Square> {
        val squaresList = mutableListOf<Square>()
        val squareSize = 1f

        for (x in -size.toInt()..size.toInt()) {
            for (y in -size.toInt()..size.toInt()) {
                squaresList.add(Square(x = x.toFloat(), y = y.toFloat(), squareSize = squareSize))
            }
        }
        return squaresList
    }

    private fun removeEnemy(enemy: Enemy) {
        enemies.remove(enemy)
    }
    fun updateUserPosition(x: Float, y: Float) { user.update(x = x, y = y) }

    fun userAttack() = user.attack()

    fun collectItem(item: Item) {
        items.remove(item)
        user.addItem(item)
    }
}
