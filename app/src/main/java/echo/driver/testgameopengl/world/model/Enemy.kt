package echo.driver.testgameopengl.world.model

import echo.driver.testgameopengl.world.Color

data class Enemy(
    override val squareSize: Float = NORMAL_SIZE,
    override var x: Float,
    override var y: Float,
    override var color: Color = Color.BLACK,
    override var health: Float = 40f,
    override val damage: Float = 1f,
    override val speed: Float = 0.05f,
    override val visionRadius: Float = 10f,
    override val onDie: (Mob) -> Unit = {}
): Mob(squareSize = squareSize, x = x, y = y, color = color) {

    fun tryToKill(user: User) {
        checkVision(user) {
            moveTo(user) {
                attack(user)
            }
        }
    }
}