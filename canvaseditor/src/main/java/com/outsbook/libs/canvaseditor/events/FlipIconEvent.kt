package com.outsbook.libs.canvaseditor.events

import android.view.MotionEvent
import com.outsbook.libs.canvaseditor.listeners.StickerIconListener
import com.outsbook.libs.canvaseditor.stickers.StickerView

internal class FlipIconEvent: StickerIconListener {
    override fun onActionDown(stickerView: StickerView?, event: MotionEvent?) {}
    override fun onActionMove(stickerView: StickerView, event: MotionEvent) {}
    override fun onActionUp(stickerView: StickerView, event: MotionEvent?) {
        stickerView.flip()
    }
}