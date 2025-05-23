package echo.driver.testgameopengl.render

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.opengl.GLES20
import android.opengl.GLUtils
import androidx.core.content.ContextCompat
import echo.driver.testgameopengl.R

class TextureLoader(private val context: Context) {
    val treeTexture = loadTexture(R.drawable.new_tree_3)
    val zombieTexture = loadTexture(R.drawable.new_zombie_pixel)
    val grass2 = loadTexture(R.drawable.grass_pixel)
    val money = loadTexture(R.drawable.new_coin)
    val house = loadTexture(R.drawable.house_pixel_3)
    val hero = loadTexture(R.drawable.hero_square)
    val heroBack = loadTexture(R.drawable.new_hero_back)
    val friend = loadTexture(R.drawable.mage_square)
    val frame = loadTexture(R.drawable.frame)

    private fun loadTexture(resourceId: Int, fromVector: Boolean = false): Int {
        val textureHandle = IntArray(1)
        GLES20.glGenTextures(1, textureHandle, 0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0])

        val bitmap =
            if (fromVector) vectorToBitmap(context, resourceId, 108, 108)
            else BitmapFactory.decodeResource(context.resources, resourceId)

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)
        bitmap?.recycle()

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)

        return textureHandle[0]
    }

    private fun vectorToBitmap(context: Context, drawableId: Int, width: Int, height: Int): Bitmap? {
        val drawable: Drawable = ContextCompat.getDrawable(context, drawableId) ?: throw RuntimeException("Drawable not found for ID: $drawableId")

        drawable.setBounds(0, 0, width, height)

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        drawable.draw(canvas)

        return bitmap
    }
}