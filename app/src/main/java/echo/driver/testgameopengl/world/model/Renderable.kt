package echo.driver.testgameopengl.world.model

open class Renderable(
    open val x: Float,
    open val y: Float,
) {
    companion object {
        fun MutableList<Renderable>.sortFromDownToUp() = sortByDescending { renderable ->
            when (renderable) {
                is Square -> renderable.y * renderable.squareSize - renderable.squareSize / 2
                else -> renderable.y
            }
        }

    }
}