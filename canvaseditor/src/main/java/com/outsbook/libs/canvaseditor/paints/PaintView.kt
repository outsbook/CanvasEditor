package com.outsbook.libs.canvaseditor.paints

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.outsbook.libs.canvaseditor.enums.DrawType
import com.outsbook.libs.canvaseditor.listeners.PaintViewListener
import com.outsbook.libs.canvaseditor.models.DrawObject
import com.outsbook.libs.canvaseditor.models.PathAndPaint
import com.outsbook.libs.canvaseditor.stickers.Sticker
import kotlin.math.abs

internal class PaintView (context: Context, private val paintViewListener: PaintViewListener) :
    FrameLayout(context) {

    private val drawColor = ResourcesCompat.getColor(resources, android.R.color.black, null)
    private var path = Path()
    private var motionTouchEventX = 0f
    private var motionTouchEventY = 0f
    private var currentX = 0f
    private var currentY = 0f
    private var isDrawPath = false
    private val touchTolerance = ViewConfiguration.get(context).scaledTouchSlop

    private lateinit var extraCanvas: Canvas
    lateinit var extraBitmap: Bitmap

    val paint = Paint().apply {
        color = drawColor
        isAntiAlias = true
        isDither = true
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 10f
    }

    fun initCanvas() {
        extraBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        extraCanvas = Canvas(extraBitmap)
        extraCanvas.drawColor(ContextCompat.getColor(context, android.R.color.white))
        invalidate()
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        initCanvas()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(extraBitmap, 0f, 0f, null)
    }

    fun drawPath(pathAndPaint: PathAndPaint) {
        extraCanvas.drawPath(pathAndPaint.path, pathAndPaint.paint)
        invalidate()
    }

    fun drawSticker(sticker: Sticker) {
        sticker.draw(extraCanvas)
        invalidate()
    }

    //region touch events
    private val gestureDetector =
        GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapConfirmed(event: MotionEvent?): Boolean {
                event?.let {
                    paintViewListener.onClick(it.x, it.y)
                }
                return super.onSingleTapConfirmed(event)
            }
        })

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        paintViewListener.onTouchEvent(event)
        motionTouchEventX = event.x
        motionTouchEventY = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> touchStart()
            MotionEvent.ACTION_MOVE -> touchMove()
            MotionEvent.ACTION_UP -> touchUp()
        }
        gestureDetector.onTouchEvent(event)
        return true
    }

    private fun touchStart() {
        path.reset()
        path.moveTo(motionTouchEventX, motionTouchEventY)
        currentX = motionTouchEventX
        currentY = motionTouchEventY
    }

    private fun touchMove() {
        val dx = abs(motionTouchEventX - currentX)
        val dy = abs(motionTouchEventY - currentY)
        if (dx >= touchTolerance || dy >= touchTolerance) {
            path.quadTo(
                currentX,
                currentY,
                (motionTouchEventX + currentX) / 2,
                (motionTouchEventY + currentY) / 2
            )
            currentX = motionTouchEventX
            currentY = motionTouchEventY
            extraCanvas.drawPath(path, paint)
            isDrawPath = true
        }
        invalidate()
    }

    private fun touchUp() {
        if (isDrawPath) {
            val obj = DrawObject(PathAndPaint(Path(path), Paint(paint)), null, DrawType.PATH)
            paintViewListener.onTouchUp(obj)
        }
        invalidate()
        path.reset()
        isDrawPath = false
    }
    //endregion
}