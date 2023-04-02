package com.yprodan.game.ui.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.Choreographer
import android.view.MotionEvent
import android.view.View
import kotlinx.coroutines.*
import kotlin.math.cos
import kotlin.math.sin
import kotlin.properties.Delegates

class GameView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var gameController: GameController = GameController()
    private var beginTimeMillis: Long = 0L

    private val circlePaint = Paint().apply {
        color = Color.RED
        style = Paint.Style.FILL
    }

    private var scenePaint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.FILL
    }

    private val whiteSquarePaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }

    private val redSquarePaint = Paint().apply {
        color = Color.RED
        style = Paint.Style.FILL
    }

    fun updateBG(color: Int) {
        scenePaint.color = color
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        with(gameController) { generateViews(w, h, 30f) }
        super.onSizeChanged(w, h, oldw, oldh)
    }

    private val fpsValuesList = ArrayList<Long>(24)

    override fun onDraw(canvas: Canvas?) {
        calculateFPS()
        canvas?.let {
            it.drawRect(0f, 0f, width.toFloat(), height.toFloat(), scenePaint)
            with(gameController) {
                gameSquares.squares.forEach { square ->
                    when (square.type) {
                        SquareType.Red ->
                            it.drawRect(
                                0f,
                                square.y,
                                square.width,
                                square.y + square.height,
                                redSquarePaint
                            )
                        SquareType.WhiteLeft -> it.drawRect(
                            square.x,
                            square.y,
                            square.x + square.width,
                            square.y + square.height,
                            whiteSquarePaint
                        )
                        SquareType.WhiteRight -> it.drawRect(
                            curWidth - square.width,
                            square.y,
                            curWidth,
                            square.y + square.height,
                            whiteSquarePaint
                        )
                        SquareType.Hided -> {}
                    }
                }

                it.drawCircle(
                    circles.firstCircle.centerX,
                    circles.firstCircle.centerY,
                    radius,
                    circlePaint
                )
                it.drawCircle(
                    circles.secondCircle.centerX,
                    circles.secondCircle.centerY,
                    radius,
                    circlePaint
                )
            }
        }
        super.onDraw(canvas)
    }

    private suspend fun startGame() {
        delay(200)
        with(gameController) {
            while (true) {
                beginTimeMillis = System.nanoTime()
                delay(10)
                circles.move(3)
                gameSquares.move(2f, 4f)
                if (isCollision()) break
                invalidate()
            }
            gameEnd()
        }
    }

    private suspend fun gameEnd() {
        with(gameController) {
            delay(100)
            circles.firstCircle.hide()
            circles.secondCircle.hide()
            invalidate()
            gameSquares.finalGrow { invalidate() }
            gameSquares.hide { invalidate() }
            delay(250)
            listener?.gameEndEvent()
        }
    }

    private fun isCollision(): Boolean {
        with(gameController) {
            val circleCollisionDotsX = circles.firstCircle.xCollisionDots
            val circleCollisionDotsY = circles.firstCircle.yCollisionDots
            val circleCollisionDotsX2 = circles.secondCircle.xCollisionDots
            val circleCollisionDotsY2 = circles.secondCircle.yCollisionDots

            gameSquares.squares.forEach { square ->

                val onCollisionAction: () -> Boolean = {
                    when (square.type) {
                        SquareType.Red -> {
                            listener?.playSoundEvent(SoundEffect.SmashRed)
                            listener?.increaseScoreEvent()
                            square.type = SquareType.Hided
                            false
                        }
                        SquareType.WhiteLeft, SquareType.WhiteRight -> {
                            listener?.playSoundEvent(SoundEffect.SmashWhite)
                            true
                        }
                        SquareType.Hided -> false
                    }
                }

                for (j in circleCollisionDotsX.indices) {
                    if (checkCollisions(
                            circleCollisionDotsX[j],
                            square.collisionRangeX,
                            circleCollisionDotsY[j],
                            square.collisionRangeY,
                            onCollisionAction
                        )
                    ) return true
                    if (checkCollisions(
                            circleCollisionDotsX2[j],
                            square.collisionRangeX,
                            circleCollisionDotsY2[j],
                            square.collisionRangeY,
                            onCollisionAction
                        )
                    ) return true
                }
            }
            return false
        }
    }

    private fun checkCollisions(
        dotX: Float,
        collisionRangeX: ClosedFloatingPointRange<Float>,
        dotY: Float,
        collisionRangeY: ClosedFloatingPointRange<Float>,
        onCollision: () -> Boolean
    ): Boolean {
        if ((dotX in collisionRangeX) && (dotY in collisionRangeY)) {
            return onCollision()
        }
        return false
    }

    private fun calculateFPS() {
        Choreographer.getInstance().postFrameCallback {
            val framesCount = (100000000 / (it - beginTimeMillis))
            if (framesCount > 0) {
                if (fpsValuesList.size == 24) {
                    listener?.fpsEvent(fpsValuesList.average().toInt().toString())
                    fpsValuesList.clear()
                } else {
                    fpsValuesList.add(framesCount)
                }
            }

        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            gameController.circles.inverseWay()
            listener?.playSoundEvent(SoundEffect.Rotate)
            return true
        }
        return super.onTouchEvent(event)
    }

    private var listener: GameEventListener? = null
    fun setGameEventListener(listener: GameEventListener) {
        this.listener = listener
        this.listener?.gameStart { it.launch { startGame() } }
    }
}

class GameSquare(
    val height: Float = 50f,
    var x: Float = 0f,
    var y: Float = 0f,
    var width: Float = 100f,
    var widthState: IncreaseDecreaseWidthState = IncreaseDecreaseWidthState.Increase,
    var type: SquareType = SquareType.WhiteLeft,
    var currWidth: Float
) {

    val collisionRangeX: ClosedFloatingPointRange<Float>
        get() = if (type == SquareType.WhiteRight) currWidth - width..currWidth else x..x + width
    val collisionRangeY: ClosedFloatingPointRange<Float>
        get() = y..y + height

}

enum class SquareType { Red, WhiteLeft, WhiteRight, Hided }
enum class IncreaseDecreaseWidthState { Increase, Decrease }

class GameController {
    var radius by Delegates.notNull<Float>()
    var curWidth by Delegates.notNull<Float>()
    private var curHeight by Delegates.notNull<Float>()
    private var centerX by Delegates.notNull<Float>()
    private var centerY by Delegates.notNull<Float>()

    var gameSquares by Delegates.notNull<GameSquares>()

    var circles by Delegates.notNull<GameCircles>()

    fun generateViews(w: Int, h: Int, radius: Float) {
        this.radius = radius
        curWidth = w.toFloat()
        curHeight = h.toFloat()
        centerX = (curWidth / 2) - this.radius / 2
        centerY = (curHeight / 2) - this.radius / 2

        gameSquares = GameSquares(
            arrayOf(
                GameSquare(
                    type = SquareType.WhiteRight,
                    widthState = IncreaseDecreaseWidthState.Increase,
                    width = GameSquares.generateWidth(curWidth / 2),
                    currWidth = curWidth
                ),
                GameSquare(
                    y = -400f,
                    width = curWidth,
                    type = SquareType.Red,
                    currWidth = curWidth
                ),
                GameSquare(
                    y = -800f,
                    type = SquareType.WhiteLeft,
                    widthState = IncreaseDecreaseWidthState.Increase,
                    width = GameSquares.generateWidth(curWidth / 2),
                    currWidth = curWidth
                ),
                GameSquare(
                    y = -1200f,
                    width = curWidth,
                    type = SquareType.Red,
                    widthState = IncreaseDecreaseWidthState.Increase,
                    currWidth = curWidth
                ),
                GameSquare(
                    y = -1600f,
                    widthState = IncreaseDecreaseWidthState.Increase,
                    width = GameSquares.generateWidth(curWidth / 2),
                    currWidth = curWidth
                ),
            ), curWidth, curHeight
        )
        circles = GameCircles(
            firstCircle = GameCircle(centerX, centerY, radius),
            secondCircle = GameCircle(centerX, centerY, radius),
            1, centerX, centerY
        )
    }
}

data class GameSquares(val squares: Array<GameSquare>, val width: Float, val height: Float) {
    private val halfWidth = width / 2
    private val width02 = width / 5

    suspend fun finalGrow(onGrowEvent: () -> Unit) {
        while (true) {
            delay(2)
            squares.forEach {
                when (it.type) {
                    SquareType.Red -> {}
                    SquareType.WhiteLeft -> {
                        if (it.width < width)
                            it.width += 5
                    }
                    SquareType.WhiteRight -> {
                        if (it.x > 0)
                            it.x -= 5
                        if (it.width < width)
                            it.width += 5
                    }
                    SquareType.Hided -> {
                        if (it.width < width)
                            it.width += 5
                    }
                }
            }
            if (squares[0].width >= width
                && squares[1].width >= width
                && squares[2].width >= width
                && squares[3].width >= width
                && squares[4].width >= width
            ) break
            onGrowEvent()
        }
    }

    suspend fun hide(onHideEvent: () -> Unit) {
        while (true) {
            delay(2)
            squares.forEach {
                when (it.type) {
                    SquareType.Red -> {
                        it.width -= 5
                    }
                    SquareType.WhiteLeft -> {
                        it.x += 5
                        it.width -= 5
                    }
                    SquareType.WhiteRight -> {
                        it.width -= 5
                    }
                    SquareType.Hided -> {
                        it.width -= 5
                    }
                }
            }

            if ((squares[0].width <= 0)
                && ((squares[1].width <= 0))
                && ((squares[2].width <= 0))
                && ((squares[3].width <= 0))
                && ((squares[4].width <= 0))
            )
                break
            onHideEvent()
        }
    }

    fun move(stepX: Float, stepY: Float) {
        squares.forEach {
            if (it.type != SquareType.Red) {

                if (it.width >= halfWidth && it.widthState == IncreaseDecreaseWidthState.Increase) it.widthState =
                    IncreaseDecreaseWidthState.Decrease
                if (it.width <= width02 && it.widthState == IncreaseDecreaseWidthState.Decrease) it.widthState =
                    IncreaseDecreaseWidthState.Increase

                when (it.widthState) {
                    IncreaseDecreaseWidthState.Increase -> {
                        it.width += stepX
                    }
                    IncreaseDecreaseWidthState.Decrease -> {
                        it.width -= stepY
                    }
                }
            }

            if (it.y > height) {
                it.y = 0f
                it.type = generateNewType()
                if (it.type == SquareType.Red) it.width = width// + 10//
                else it.width = generateWidth()

            } else {
                it.y += stepY
            }
        }
    }

    companion object {
        fun generateNewType() = SquareType.values()[(0..2).random()]
        fun generateWidth() = (20..50).random().toFloat() * 5
        fun generateWidth(maxX: Float) = (100..maxX.toInt()).random().toFloat()
    }
}

interface GameEventListener {
    fun gameStart(callback: (CoroutineScope) -> Unit)
    fun increaseScoreEvent()
    fun playSoundEvent(sound: SoundEffect)
    fun fpsEvent(fps: String)
    fun gameEndEvent()
}

data class GameCircles(
    val firstCircle: GameCircle,
    val secondCircle: GameCircle,
    var way: Int,
    var centerX: Float,
    var centerY: Float
) {
    private val radianArr = Array(360) { it * 0.017453292f }

    private var stepCounter = 0
    fun move(step: Int) {
        val radian = radianArr[stepCounter]

        firstCircle.centerX =
            (way * (centerX - firstCircle.radius) * cos(radian)) + centerX + firstCircle.radius / 2// - 30
        firstCircle.centerY =
            (way * (centerX - firstCircle.radius) * sin(radian)) + centerY// + 15
        secondCircle.centerX =
            (-way * (centerX - secondCircle.radius) * cos(radian)) + centerX + secondCircle.radius / 2// - 30
        secondCircle.centerY =
            (-way * (centerX - secondCircle.radius) * sin(radian)) + centerY// + 15

        stepCounter += step * way

        when (stepCounter) {
            radianArr.size -> {
                stepCounter = 0
            }
            -step, 0 -> {
                stepCounter = radianArr.size - step
            }
        }
    }

    fun inverseWay() {
        way *= -1
    }
}

data class GameCircle(var centerX: Float, var centerY: Float, val radius: Float) {
    fun hide() {
        centerX = -100f
        centerY = -100f
    }

    private val collisionValuesX = Array(8) { radius * cos(it * 45f) }
    private val collisionValuesY = Array(8) { radius * sin(it * 45f) }

    val xCollisionDots: Array<Float>
        get() = Array(collisionValuesX.size) { collisionValuesX[it] + centerX }

    val yCollisionDots: Array<Float>
        get() = Array(collisionValuesY.size) { collisionValuesY[it] + centerY }
}

enum class SoundEffect { ButtonClick, Rotate, SmashWhite, SmashRed, Lol, Best, Good, Almost }