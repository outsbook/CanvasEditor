package com.outsbook.libs.canvaseditor.stickers

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.outsbook.libs.canvaseditor.R
import com.outsbook.libs.canvaseditor.constants.ActionMode
import com.outsbook.libs.canvaseditor.constants.ConstantSticker
import com.outsbook.libs.canvaseditor.constants.ConstantStickerIcon
import com.outsbook.libs.canvaseditor.enums.DrawType
import com.outsbook.libs.canvaseditor.events.DeleteIconEvent
import com.outsbook.libs.canvaseditor.events.DoneIconEvent
import com.outsbook.libs.canvaseditor.events.FlipIconEvent
import com.outsbook.libs.canvaseditor.events.ZoomIconEvent
import com.outsbook.libs.canvaseditor.listeners.StickerViewListener
import com.outsbook.libs.canvaseditor.models.DrawObject
import java.util.*
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt

internal class StickerView(context: Context, private val stickerViewListener: StickerViewListener) :
    FrameLayout(context) {
    var currentSticker: Sticker? = null

    private var currentMode = ActionMode.NONE

    private var isTouchInsideSticker = false

    private val stickerRect = RectF()
    private val icons: MutableList<StickerIcon> = ArrayList(4)
    private val bitmapPoints = FloatArray(8)
    private val bounds = FloatArray(8)
    private val point = FloatArray(2)
    private val currentCenterPoint = PointF()
    private val tmp = FloatArray(2)
    private var midPoint = PointF()

    private val sizeMatrix = Matrix()
    private val downMatrix = Matrix()
    private val moveMatrix = Matrix()

    private var downX = 0f
    private var downY = 0f
    private var oldDistance = 0f
    private var oldRotation = 0f

    private val borderPaint = Paint().apply {
        isAntiAlias = true
        color = Color.BLACK
        alpha = 50
    }
    private val iconPaint = Paint().apply {
        isAntiAlias = true
        color = Color.BLACK
        alpha = 128
    }

    private val touchSlop: Int = ViewConfiguration.get(context).scaledTouchSlop
    private var currentIcon: StickerIcon? = null

    init {
        configDefaultIcons()
    }

    private fun configDefaultIcons() {
        val deleteIcon = StickerIcon(
            ContextCompat.getDrawable(context, R.drawable.ic_close_white_20dp),
            ConstantStickerIcon.LEFT_TOP
        )
        deleteIcon.iconListener = DeleteIconEvent()
        val doneIcon = StickerIcon(
            ContextCompat.getDrawable(context, R.drawable.ic_done_white_20dp),
            ConstantStickerIcon.RIGHT_TOP
        )
        doneIcon.iconListener = DoneIconEvent()
        val zoomIcon = StickerIcon(
            ContextCompat.getDrawable(context, R.drawable.ic_rotate_scale_white_17dp),
            ConstantStickerIcon.RIGHT_BOTTOM
        )
        zoomIcon.iconListener = ZoomIconEvent()
        val flipIcon = StickerIcon(
            ContextCompat.getDrawable(context, R.drawable.ic_flip_white_20dp),
            ConstantStickerIcon.LEFT_BOTTOM
        )
        flipIcon.iconListener = FlipIconEvent()
        icons.clear()
        icons.add(deleteIcon)
        icons.add(doneIcon)
        icons.add(zoomIcon)
        icons.add(flipIcon)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        currentSticker?.let {
            transformSticker(it)
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed) {
            stickerRect.left = left.toFloat()
            stickerRect.top = top.toFloat()
            stickerRect.right = right.toFloat()
            stickerRect.bottom = bottom.toFloat()
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        drawStickers(canvas)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = ev.x
                downY = ev.y
                return findCurrentIconTouched() != null || currentSticker != null
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        stickerViewListener.onTouchEvent(event)
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> if (!onTouchDown(event)) {
                return false
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                oldDistance = calculateDistance(event)
                oldRotation = calculateRotation(event)
                midPoint = calculateMidPoint(event)
                if (currentSticker != null && isInStickerArea(
                        currentSticker!!, event.getX(1),
                        event.getY(1)
                    ) && findCurrentIconTouched() == null
                ) {
                    currentMode = ActionMode.ZOOM_WITH_TWO_FINGER
                }
            }
            MotionEvent.ACTION_MOVE -> {
                handleCurrentMode(event)
                invalidate()
            }
            MotionEvent.ACTION_UP -> onTouchUp(event)
            MotionEvent.ACTION_POINTER_UP -> {
                currentMode = ActionMode.NONE
            }
        }
        return true
    }

    private fun drawStickers(canvas: Canvas) {
        currentSticker?.draw(canvas)
        if (currentSticker != null) {
            getStickerPoints(currentSticker, bitmapPoints)
            val x1 = bitmapPoints[0]
            val y1 = bitmapPoints[1]
            val x2 = bitmapPoints[2]
            val y2 = bitmapPoints[3]
            val x3 = bitmapPoints[4]
            val y3 = bitmapPoints[5]
            val x4 = bitmapPoints[6]
            val y4 = bitmapPoints[7]

            //draw border
            canvas.drawLine(x1, y1, x2, y2, borderPaint)
            canvas.drawLine(x1, y1, x3, y3, borderPaint)
            canvas.drawLine(x2, y2, x4, y4, borderPaint)
            canvas.drawLine(x4, y4, x3, y3, borderPaint)

            //draw icons
            val rotation = calculateRotation(x4, y4, x3, y3)
            for (i in icons.indices) {
                val icon = icons[i]
                when (icon.position) {
                    ConstantStickerIcon.LEFT_TOP -> configIconMatrix(icon, x1, y1, rotation)
                    ConstantStickerIcon.RIGHT_TOP -> configIconMatrix(icon, x2, y2, rotation)
                    ConstantStickerIcon.LEFT_BOTTOM -> configIconMatrix(icon, x3, y3, rotation)
                    ConstantStickerIcon.RIGHT_BOTTOM -> configIconMatrix(icon, x4, y4, rotation)
                }
                icon.draw(canvas, iconPaint)
            }
        }
    }

    private fun getStickerPoints(sticker: Sticker?, dst: FloatArray) {
        if (sticker == null) {
            Arrays.fill(dst, 0f)
            return
        }
        sticker.getBoundPoints(bounds)
        sticker.getMappedPoints(dst, bounds)
    }

    private fun calculateDistance(event: MotionEvent?): Float {
        return if (event == null || event.pointerCount < 2) {
            0f
        } else calculateDistance(event.getX(0), event.getY(0), event.getX(1), event.getY(1))
    }

    private fun calculateDistance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        val x = x1 - x2.toDouble()
        val y = y1 - y2.toDouble()
        return sqrt(x * x + y * y).toFloat()
    }

    private fun calculateRotation(event: MotionEvent?): Float {
        return if (event == null || event.pointerCount < 2) {
            0f
        } else calculateRotation(event.getX(0), event.getY(0), event.getX(1), event.getY(1))
    }

    private fun calculateRotation(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        val x = x1 - x2.toDouble()
        val y = y1 - y2.toDouble()
        val radians = atan2(y, x)
        return Math.toDegrees(radians).toFloat()
    }

    private fun configIconMatrix(icon: StickerIcon, x: Float, y: Float, rotation: Float) {
        icon.x = x
        icon.y = y
        icon.matrix.reset()
        icon.matrix.postRotate(rotation, icon.width / 2.toFloat(), icon.height / 2.toFloat())
        icon.matrix.postTranslate(x - icon.width / 2, y - icon.height / 2)
    }

    private fun transformSticker(sticker: Sticker) {
        sizeMatrix.reset()
        val width = width.toFloat()
        val height = height.toFloat()
        val stickerWidth = sticker.width.toFloat()
        val stickerHeight = sticker.height.toFloat()
        //step 1
        val offsetX = (width - stickerWidth) / 2
        val offsetY = (height - stickerHeight) / 2
        sizeMatrix.postTranslate(offsetX, offsetY)

        //step 2
        val scaleFactor: Float
        scaleFactor = if (width < height) {
            width / stickerWidth
        } else {
            height / stickerHeight
        }
        sizeMatrix.postScale(scaleFactor / 2f, scaleFactor / 2f, width / 2f, height / 2f)
        sticker.matrix.reset()
        sticker.setMatrix(sizeMatrix)
        invalidate()
    }

    private fun findCurrentIconTouched(): StickerIcon? {
        for (icon in icons) {
            val x = icon.x - downX
            val y = icon.y - downY
            val distancePow2 = x * x + y * y
            if (distancePow2 <= (icon.iconRadius + icon.iconRadius.toDouble()).pow(2.0)) {
                return icon
            }
        }
        return null
    }

    private fun onTouchDown(event: MotionEvent): Boolean {
        currentMode = ActionMode.DRAG
        downX = event.x
        downY = event.y
        midPoint = calculateMidPoint()
        oldDistance = calculateDistance(midPoint.x, midPoint.y, downX, downY)
        oldRotation = calculateRotation(midPoint.x, midPoint.y, downX, downY)
        currentIcon = findCurrentIconTouched()

        if (currentIcon != null) {
            currentMode = ActionMode.ICON
            currentIcon!!.onActionDown(this, event)
        }

        if (currentSticker != null) {
            isTouchInsideSticker = currentSticker!!.contains(downX, downY)
            downMatrix.set(Sticker.getMatrix(currentSticker!!))
        }

        if (currentIcon == null && !isTouchInsideSticker) {
            doneSticker(currentSticker)
            return false
        }

        invalidate()
        return true
    }

    private fun handleCurrentMode(event: MotionEvent) {
        when (currentMode) {
            ActionMode.NONE, ActionMode.CLICK -> {
            }
            ActionMode.DRAG -> if (currentSticker != null && isTouchInsideSticker) {
                moveMatrix.set(downMatrix)
                moveMatrix.postTranslate(event.x - downX, event.y - downY)
                currentSticker!!.setMatrix(moveMatrix)
            }

            ActionMode.ZOOM_WITH_TWO_FINGER -> if (currentSticker != null && isTouchInsideSticker) {
                val newDistance = calculateDistance(event)
                val newRotation = calculateRotation(event)
                moveMatrix.set(downMatrix)
                moveMatrix.postScale(
                    newDistance / oldDistance, newDistance / oldDistance, midPoint.x,
                    midPoint.y
                )
                moveMatrix.postRotate(newRotation - oldRotation, midPoint.x, midPoint.y)
                currentSticker!!.setMatrix(moveMatrix)
            }
            ActionMode.ICON -> if (currentSticker != null && currentIcon != null) {
                currentIcon!!.onActionMove(this, event)
            }
        }
    }

    private fun onTouchUp(event: MotionEvent) {
        if (currentMode == ActionMode.ICON && currentIcon != null && currentSticker != null) {
            currentIcon!!.onActionUp(this, event)
        }
        if (currentMode == ActionMode.DRAG && abs(event.x - downX) < touchSlop && abs(event.y - downY) < touchSlop && currentSticker != null) {
            if (!isTouchInsideSticker)
                stickerViewListener.onClickStickerOutside(event.x, event.y)
            currentMode = ActionMode.CLICK
        }
        currentMode = ActionMode.NONE
    }

    private fun calculateMidPoint(event: MotionEvent?): PointF {
        if (event == null || event.pointerCount < 2) {
            midPoint[0f] = 0f
            return midPoint
        }
        val x = (event.getX(0) + event.getX(1)) / 2
        val y = (event.getY(0) + event.getY(1)) / 2
        midPoint[x] = y
        return midPoint
    }

    private fun calculateMidPoint(): PointF {
        if (currentSticker == null) {
            midPoint[0f] = 0f
            return midPoint
        }
        currentSticker?.getMappedCenterPoint(midPoint, point, tmp)
        return midPoint
    }

    private fun isInStickerArea(sticker: Sticker, downX: Float, downY: Float): Boolean {
        tmp[0] = downX
        tmp[1] = downY
        return sticker.contains(tmp)
    }

    fun addSticker(sticker: Sticker): StickerView {
        return addSticker(sticker, ConstantSticker.CENTER)
    }

    private fun addSticker(sticker: Sticker, position: Int): StickerView {
        if (ViewCompat.isLaidOut(this)) {
            addStickerImmediately(sticker, position)
        } else {
            post { addStickerImmediately(sticker, position) }
        }
        return this
    }

    private fun addStickerImmediately(sticker: Sticker, position: Int) {
        setStickerPosition(sticker, position)
        val scaleFactor: Float
        val widthScaleFactor: Float = width.toFloat() / sticker.drawable.intrinsicWidth
        val heightScaleFactor: Float = height.toFloat() / sticker.drawable.intrinsicHeight
        scaleFactor =
            if (widthScaleFactor > heightScaleFactor) heightScaleFactor else widthScaleFactor
        sticker.matrix.postScale(
            scaleFactor / 2,
            scaleFactor / 2,
            width / 2.toFloat(),
            height / 2.toFloat()
        )
        currentSticker = sticker
        //stickers.add(sticker)
        invalidate()
    }

    private fun setStickerPosition(sticker: Sticker, position: Int) {
        val width = width.toFloat()
        val height = height.toFloat()
        var offsetX = width - sticker.width
        var offsetY = height - sticker.height
        when {
            position and ConstantSticker.TOP > 0 -> offsetY /= 4f
            position and ConstantSticker.BOTTOM > 0 -> offsetY *= 3f / 4f
            else -> offsetY /= 2f
        }
        when {
            position and ConstantSticker.LEFT > 0 -> offsetX /= 4f
            position and ConstantSticker.RIGHT > 0 -> offsetX *= 3f / 4f
            else -> offsetX /= 2f
        }
        sticker.matrix.postTranslate(offsetX, offsetY)
    }

    private fun removeSticker(sticker: Sticker?) {
        if (sticker == null)
            return
        currentSticker = null
        this.visibility = View.GONE
        stickerViewListener.onRemove()
    }

    private fun doneSticker(sticker: Sticker?) {
        if (sticker == null)
            return
        currentSticker = null
        this.visibility = View.GONE
        val obj = DrawObject(null, sticker, DrawType.STICKER)
        stickerViewListener.onDone(obj)
    }

    private fun zoomAndRotateSticker(sticker: Sticker?, event: MotionEvent) {
        if (sticker == null)
            return
        val newDistance = calculateDistance(midPoint.x, midPoint.y, event.x, event.y)
        val newRotation = calculateRotation(midPoint.x, midPoint.y, event.x, event.y)
        moveMatrix.set(downMatrix)
        moveMatrix.postScale(
            newDistance / oldDistance, newDistance / oldDistance, midPoint.x,
            midPoint.y
        )
        moveMatrix.postRotate(newRotation - oldRotation, midPoint.x, midPoint.y)
        currentSticker!!.setMatrix(moveMatrix)
        stickerViewListener.onZoomAndRotate()
    }

    private fun flipSticker(sticker: Sticker?) {
        if (sticker == null)
            return
        sticker.getCenterPoint(midPoint)
        sticker.matrix.preScale(-1f, 1f, midPoint.x, midPoint.y)
        sticker.isFlippedHorizontally = !sticker.isFlippedHorizontally
        invalidate()
        stickerViewListener.onFlip()
    }

    fun remove() {
        removeSticker(currentSticker)
    }

    fun done() {
        doneSticker(currentSticker)
    }

    fun zoomAndRotate(event: MotionEvent) {
        zoomAndRotateSticker(currentSticker, event)
    }

    fun flip() {
        flipSticker(currentSticker)
    }
}