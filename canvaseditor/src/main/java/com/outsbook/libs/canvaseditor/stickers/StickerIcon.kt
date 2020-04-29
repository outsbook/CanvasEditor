package com.outsbook.libs.canvaseditor.stickers

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.view.MotionEvent
import com.outsbook.libs.canvaseditor.constants.ConstantStickerIcon
import com.outsbook.libs.canvaseditor.listeners.StickerIconListener

internal class StickerIcon(drawable: Drawable?, gravity: Int): DrawableSticker(drawable!!), StickerIconListener {
    var iconRadius = ConstantStickerIcon.DEFAULT_ICON_RADIUS
    var x = 0f
    var y = 0f

    var position = ConstantStickerIcon.LEFT_TOP
    var iconListener: StickerIconListener? = null

    init {
        position = gravity
    }

    fun draw(canvas: Canvas, paint: Paint?) {
        canvas.drawCircle(x, y, iconRadius, paint!!)
        super.draw(canvas)
    }

    override fun onActionDown(stickerView: StickerView?, event: MotionEvent?) {
        iconListener?.onActionDown(stickerView, event)
    }

    override fun onActionMove(stickerView: StickerView, event: MotionEvent) {
        iconListener?.onActionMove(stickerView, event)
    }

    override fun onActionUp(stickerView: StickerView, event: MotionEvent?) {
        iconListener?.onActionUp(stickerView, event)
    }
}