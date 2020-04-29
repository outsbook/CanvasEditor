package com.outsbook.libs.canvaseditor.stickers

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable

internal open class DrawableSticker(override var drawable: Drawable): Sticker() {
    private val realBounds: Rect

    final override val width: Int
        get() = drawable.intrinsicWidth

    final override val height: Int
        get() = drawable.intrinsicHeight

    init {
        realBounds = Rect(0, 0, width, height)
    }

    override fun setDrawable(drawable: Drawable): DrawableSticker {
        this.drawable = drawable
        return this
    }

    override fun draw(canvas: Canvas) {
        canvas.save()
        canvas.concat(matrix)
        drawable.bounds = realBounds
        drawable.draw(canvas)
        canvas.restore()
    }

    override fun setAlpha(alpha: Int): DrawableSticker {
        drawable.alpha = alpha
        return this
    }
}