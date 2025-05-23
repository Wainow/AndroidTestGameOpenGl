package echo.driver.testgameopengl.render

import android.content.Context
import echo.driver.testgameopengl.utils.forEachVisible
import echo.driver.testgameopengl.world.World
import echo.driver.testgameopengl.world.model.Building
import echo.driver.testgameopengl.world.model.Enemy
import echo.driver.testgameopengl.world.model.Friend
import echo.driver.testgameopengl.world.model.Item
import echo.driver.testgameopengl.world.model.Mob
import echo.driver.testgameopengl.world.model.Renderable
import echo.driver.testgameopengl.world.model.Renderable.Companion.sortFromDownToUp
import echo.driver.testgameopengl.world.model.Square
import echo.driver.testgameopengl.world.model.Square.Companion.findNearest
import echo.driver.testgameopengl.world.model.Tree
import echo.driver.testgameopengl.world.model.User

class Render(
    context: Context,
    program: Int,
    private val monitorManager: MonitorManager,
) {
    companion object {
        const val FEATURE_TEXTURES = true
    }

    private var frustum = Frustum()
    private val coreRender = CoreRender(program, monitorManager.getProjectionViewMatrix())
    private val textureLoader = TextureLoader(context)

    fun drawWorld(world: World, projectionMatrix: FloatArray) {
        frustum.updateBounds(monitorManager.viewMatrix, monitorManager.compressionFactor)
        coreRender.projectionMatrix = projectionMatrix

        if (FEATURE_TEXTURES) world.earth.forEachVisible(frustum) { coreRender.drawSquare(it, textureLoader.grass2) }
        val renderables = mutableListOf<Renderable>()

        world.items.forEachVisible(frustum, renderables::add)
        world.enemies.forEachVisible(frustum, renderables::add)
        world.buildings.forEachVisible(frustum, renderables::add)
        world.friends.forEachVisible(frustum, renderables::add)
        world.trees.forEachVisible(frustum, renderables::add)
        renderables.add(world.user)

        renderables.sortFromDownToUp()

        renderables.forEach { elem ->
            when(elem) {
                is Friend -> drawFriend(world, elem)
                is Tree -> drawTree(elem)
                is Building -> drawBuilding(world, elem)
                is Enemy -> drawEnemy(world, elem)
                is Item -> drawItem(world, elem)
                is User -> drawUser(world, elem)
                is Square -> coreRender.drawSquare(elem)
            }
        }
    }

    private fun drawFriend(world: World, friend: Friend) {
        coreRender.drawSquare(friend, textureLoader.friend)
        val nearestEnemy = world.enemies.findNearest(friend) as Enemy?
        friend.tryToKill(nearestEnemy)
    }

    private fun drawUser(world: World, user: User) {
        world.enemies.forEachVisible(frustum) {
            if (it.isCollidingWith(user) && user.isAnimationShowing()) {
                user.attack(it)
            }
        }
        user.items.forEachVisible(frustum) { item ->
            coreRender.drawSquare(item, textureLoader.money)
        }
        coreRender.drawSquare(user, if (user.isMovingBack) textureLoader.heroBack else textureLoader.hero )
        user.currentAnimationSquare?.let { coreRender.drawSquare(it) }
    }

    private fun drawBuilding(world: World, building: Building) {
        if (building.isCollidingWith(world.user)) {
            val result = building.addItems(world.user)
            if (result) {
                val newFriend = building.randomFriend {
                    world.friends.remove(it)
                }
                world.friends.add(newFriend)
            }
        }

        if (building.isBuild.not()) {
            coreRender.drawSquare(building, textureLoader.frame)
            building.items.forEachVisible(frustum) { item ->
                coreRender.drawSquare(item, textureLoader.money)
            }
        } else {
            //building.finishedBuild.forEachVisible(frustum) { square ->
              //  coreRender.drawSquare(square)
            //}
            coreRender.drawSquare(building.buildingSquare, textureLoader.house)
        }
    }

    private fun drawTree(tree: Tree) {
        if (FEATURE_TEXTURES) coreRender.drawSquare(tree, textureLoader.treeTexture)
        else coreRender.drawSquare(tree, tree.color)
    }
    private fun drawMob(mob: Mob) {
        coreRender.drawSquare(mob, mob.currentColor)
    }
    private fun drawItem(world: World, item: Item) {
        if (item.isCollidingWith(world.user)) {
            world.collectItem(item)
        } else {
            coreRender.drawSquare(item, textureLoader.money)
        }
    }
    private fun drawEnemy(world: World, enemy: Enemy) {
        if (enemy.isCollidingWith(world.user)) {
            enemy.attack(world.user)
        }
        if (FEATURE_TEXTURES) coreRender.drawSquare(enemy, textureLoader.zombieTexture)
        else coreRender.drawSquare(enemy, enemy.currentColor)
        enemy.tryToKill(world.user)
    }
}
