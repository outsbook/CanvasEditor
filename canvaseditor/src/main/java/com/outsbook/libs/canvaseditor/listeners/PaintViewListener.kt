package com.outsbook.libs.canvaseditor.listeners

import android.view.MotionEvent
import com.outsbook.libs.canvaseditor.models.DrawObject

internal interface PaintViewListener {
    fun onTouchUp(obj: DrawObject)
    fun onClick(x: Float, y: Float)
    fun onTouchEvent(event: MotionEvent)
}