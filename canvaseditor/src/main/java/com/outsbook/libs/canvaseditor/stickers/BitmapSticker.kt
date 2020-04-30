package com.outsbook.libs.canvaseditor.stickers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

internal class BitmapSticker(context: Context, bitmap: Bitmap): Sticker() {
    private val realBounds: Rect

    override var drawable: Drawable = BitmapDrawable(context.resources, bitmap)

    final override val width: Int
        get() = drawable.intrinsicWidth

    final override val height: Int
        get() = drawable.intrinsicHeight

    init {
        realBounds = Rect(0, 0, width, height)
    }

    override fun setDrawable(drawable: Drawable): BitmapSticker {
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

    override fun setAlpha(alpha: Int): BitmapSticker {
        drawable.alpha = alpha
        return this
    }

}