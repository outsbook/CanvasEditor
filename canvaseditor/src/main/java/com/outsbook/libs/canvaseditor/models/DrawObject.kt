package com.outsbook.libs.canvaseditor.models

import com.outsbook.libs.canvaseditor.enums.DrawType
import com.outsbook.libs.canvaseditor.models.PathAndPaint
import com.outsbook.libs.canvaseditor.stickers.Sticker

internal data class DrawObject(
    val pathAndPaint: PathAndPaint?,
    val sticker: Sticker?,
    val drawType: DrawType
)