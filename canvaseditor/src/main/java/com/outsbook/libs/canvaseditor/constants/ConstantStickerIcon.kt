package com.outsbook.libs.canvaseditor.constants

import android.content.res.Resources

internal class ConstantStickerIcon {
    companion object{
        val DEFAULT_ICON_RADIUS = 14f * Resources.getSystem().displayMetrics.density

        const val LEFT_TOP = 0
        const val RIGHT_TOP = 1
        const val LEFT_BOTTOM = 2
        const val RIGHT_BOTTOM = 3
    }
}