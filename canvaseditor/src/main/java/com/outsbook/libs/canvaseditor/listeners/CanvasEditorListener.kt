package com.outsbook.libs.canvaseditor.listeners

import android.view.MotionEvent

interface CanvasEditorListener {
    fun onEnableUndo(isEnable: Boolean)
    fun onEnableRedo(isEnable: Boolean)
    fun onTouchEvent(event: MotionEvent) {}

    fun onStickerActive() {}
    fun onStickerRemove() {}
    fun onStickerDone() {}
    fun onStickerZoomAndRotate() {}
    fun onStickerFlip() {}
}