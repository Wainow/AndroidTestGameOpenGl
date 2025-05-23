package echo.driver.testgameopengl

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.pow

class JoystickView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val joystickRadius = 100f // Радиус джойстика
    private val innerCircleRadius = 50f // Радиус внутреннего круга (передвигаемой части джойстика)
    private var xCenter = 0f // Центр джойстика по оси X
    private var yCenter = 0f // Центр джойстика по оси Y
    private var xPosition = 0f // Текущая позиция внутреннего круга по оси X
    private var yPosition = 0f // Текущая позиция внутреннего круга по оси Y
    private val paint = Paint()

    init {
        paint.color = Color.GRAY
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // Центр джойстика находится в нижней центральной части экрана
        xCenter = (width / 2).toFloat()
        yCenter = (height - 200).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Рисуем внешний круг джойстика
        canvas.drawCircle(xCenter, yCenter, joystickRadius, paint)

        // Рисуем внутренний круг джойстика (который перемещается)
        paint.color = Color.DKGRAY
        canvas.drawCircle(xCenter + xPosition, yCenter + yPosition, innerCircleRadius, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        val xTouch = event.x
        val yTouch = event.y

        when (action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                // Рассчитываем расстояние от центра джойстика
                val distance = Math.sqrt(
                    ((xTouch - xCenter).toDouble().pow(2.0) + (yTouch - yCenter).toDouble().pow(2.0))
                )

                if (distance < joystickRadius) {
                    // Если касание в пределах джойстика, обновляем положение внутреннего круга
                    xPosition = (xTouch - xCenter)
                    yPosition = (yTouch - yCenter)
                } else {
                    // Если касание вне джойстика, то двигаем внутренний круг до края
                    val angle = Math.atan2((yTouch - yCenter).toDouble(), (xTouch - xCenter).toDouble())
                    xPosition = (Math.cos(angle) * joystickRadius).toFloat()
                    yPosition = (Math.sin(angle) * joystickRadius).toFloat()
                }

                invalidate() // Перерисовываем джойстик
                return true
            }
            MotionEvent.ACTION_UP -> {
                // Возвращаем внутренний круг в центр, когда касание отпускается
                xPosition = 0f
                yPosition = 0f
                invalidate() // Перерисовываем джойстик
            }
        }
        return super.onTouchEvent(event)
    }

    // Функция для получения направления от джойстика
    fun getDirection(): Pair<Float, Float> {
        return Pair(xPosition / joystickRadius, yPosition / joystickRadius)
    }
}