package com.outsbook.libs.canvaseditor.stickers

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import androidx.core.content.ContextCompat
import com.outsbook.libs.canvaseditor.R

internal class TextSticker(private val context: Context, drawable: Drawable?): Sticker() {
    override lateinit var drawable: Drawable

    private var text: String? = null
    private val mEllipsis = "\u2026"

    private val realBounds: Rect
    private val textRect: Rect
    private val textPaint: TextPaint
    private var staticLayout: StaticLayout? = null
    private var alignment: Layout.Alignment

    private var maxTextSizePixels: Float
    private var minTextSizePixels: Float
    private var lineSpacingMultiplier = 1.0f
    private var lineSpacingExtra = 0.0f

    init {
        if (drawable == null) {
            this.drawable = ContextCompat.getDrawable(context, R.drawable.shape_transfarent_background)!!
        }else{
            this.drawable = drawable
        }
        textPaint = TextPaint(TextPaint.ANTI_ALIAS_FLAG)
        realBounds = Rect(0, 0, width, height)
        textRect = Rect(0, 0, width, height)
        minTextSizePixels = convertSpToPx(6f)
        maxTextSizePixels = convertSpToPx(32f)
        alignment = Layout.Alignment.ALIGN_CENTER
        textPaint.textSize = maxTextSizePixels
    }

    override val width: Int
        get() = drawable.intrinsicWidth

    override val height: Int
        get() = drawable.intrinsicHeight

    override fun draw(canvas: Canvas) {
        val matrix = matrix
        canvas.save()
        canvas.concat(matrix)
        drawable.bounds = realBounds
        drawable.draw(canvas)

        canvas.restore()
        canvas.save()
        canvas.concat(matrix)
        if (textRect.width() == width) {
            val dy = height / 2 - staticLayout!!.height / 2
            canvas.translate(0f, dy.toFloat())
        } else {
            val dx = textRect.left
            val dy = textRect.top + textRect.height() / 2 - staticLayout!!.height / 2
            canvas.translate(dx.toFloat(), dy.toFloat())
        }
        staticLayout!!.draw(canvas)
        canvas.restore()
    }

    override fun setAlpha(alpha: Int): TextSticker {
        textPaint.alpha = alpha
        return this
    }

    override fun setDrawable(drawable: Drawable): TextSticker {
        this.drawable = drawable
        realBounds[0, 0, width] = height
        textRect[0, 0, width] = height
        return this
    }

    fun setDrawable(drawable: Drawable, region: Rect?): TextSticker {
        this.drawable = drawable
        realBounds[0, 0, width] = height
        if (region == null) {
            textRect[0, 0, width] = height
        } else {
            textRect[region.left, region.top, region.right] = region.bottom
        }
        return this
    }

    fun setText(text: String): TextSticker{
        this.text = text
        return this
    }

    fun setTypeface(typeface: Typeface?): TextSticker {
        textPaint.typeface = typeface
        return this
    }

    fun setTextColor(color: Int): TextSticker {
        textPaint.color = color
        return this
    }

    fun setTextAlign(alignment: Layout.Alignment): TextSticker {
        this.alignment = alignment
        return this
    }

    fun setMaxTextSize(size: Float): TextSticker {
        textPaint.textSize = convertSpToPx(size)
        maxTextSizePixels = textPaint.textSize
        return this
    }

    fun setMinTextSize(minTextSizeScaledPixels: Float): TextSticker {
        minTextSizePixels = convertSpToPx(minTextSizeScaledPixels)
        return this
    }

    fun setLineSpacing(add: Float, multiplier: Float): TextSticker {
        lineSpacingMultiplier = multiplier
        lineSpacingExtra = add
        return this
    }

    fun resizeText(): TextSticker {
        val availableHeightPixels = textRect.height()
        val availableWidthPixels = textRect.width()
        val text: CharSequence? = text
        if (text == null || text.isEmpty() || availableHeightPixels <= 0 || availableWidthPixels <= 0 || maxTextSizePixels <= 0) {
            return this
        }
        var targetTextSizePixels = maxTextSizePixels
        var targetTextHeightPixels = getTextHeightPixels(text, availableWidthPixels, targetTextSizePixels)

        while (targetTextHeightPixels > availableHeightPixels
            && targetTextSizePixels > minTextSizePixels) {
            targetTextSizePixels = Math.max(targetTextSizePixels - 2, minTextSizePixels)
            targetTextHeightPixels = getTextHeightPixels(text, availableWidthPixels, targetTextSizePixels)
        }

        if (targetTextSizePixels == minTextSizePixels
            && targetTextHeightPixels > availableHeightPixels) {
            val textPaintCopy = TextPaint(textPaint)
            textPaintCopy.textSize = targetTextSizePixels

            val staticLayout = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                StaticLayout.Builder
                    .obtain(text,0, text.length, textPaintCopy, availableWidthPixels)
                    .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                    .setLineSpacing(lineSpacingExtra, lineSpacingMultiplier)
                    .setIncludePad(false)
                    .build()
            } else {
                @Suppress("DEPRECATION")
                (StaticLayout(
                    text, textPaintCopy, availableWidthPixels, Layout.Alignment.ALIGN_NORMAL,
                    lineSpacingMultiplier, lineSpacingExtra, false
                ))
            }

            if (staticLayout.lineCount > 0) {
                val lastLine = staticLayout.getLineForVertical(availableHeightPixels) - 1
                if (lastLine >= 0) {
                    val startOffset = staticLayout.getLineStart(lastLine)
                    var endOffset = staticLayout.getLineEnd(lastLine)
                    var lineWidthPixels = staticLayout.getLineWidth(lastLine)
                    val ellipseWidth = textPaintCopy.measureText(mEllipsis)

                    while (availableWidthPixels < lineWidthPixels + ellipseWidth) {
                        endOffset--
                        lineWidthPixels = textPaintCopy.measureText(text.subSequence(startOffset, endOffset + 1).toString())
                    }
                    setText(text.subSequence(0, endOffset).toString() + mEllipsis)
                }
            }
        }
        textPaint.textSize = targetTextSizePixels
        staticLayout = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StaticLayout.Builder
                .obtain(this.text!!,0, this.text!!.length, textPaint, textRect.width())
                .setAlignment(alignment)
                .setLineSpacing(lineSpacingExtra, lineSpacingMultiplier)
                .setIncludePad(true)
                .build()
        } else {
            @Suppress("DEPRECATION")
            (StaticLayout(
                this.text!!, textPaint, availableWidthPixels, alignment,
                lineSpacingMultiplier, lineSpacingExtra, true
            ))
        }
        return this
    }

    private fun getTextHeightPixels(source: CharSequence, availableWidthPixels: Int, textSizePixels: Float): Int {
        textPaint.textSize = textSizePixels
        val staticLayout = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StaticLayout.Builder
                .obtain(source,0, 0, textPaint, availableWidthPixels)
                .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                .setLineSpacing(lineSpacingExtra, lineSpacingMultiplier)
                .setIncludePad(true)
                .build()
        } else {
            @Suppress("DEPRECATION")
            (StaticLayout(
                source, textPaint, availableWidthPixels, Layout.Alignment.ALIGN_NORMAL,
                lineSpacingMultiplier, lineSpacingExtra, true
            ))
        }

        return staticLayout.height
    }

    private fun convertSpToPx(scaledPixels: Float): Float {
        return scaledPixels * context.resources.displayMetrics.scaledDensity
    }
}