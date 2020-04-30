package com.outsbook.examplekotlin

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.outsbook.libs.canvaseditor.listeners.CanvasEditorListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var strokeWidth: Float = 20f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonUndo.imageAlpha = 50
        buttonRedo.imageAlpha = 50

        canvasEditorView.setListener(object: CanvasEditorListener {
            override fun onEnableUndo(isEnable: Boolean) {
                buttonUndo.imageAlpha = if(isEnable) 255 else 50
            }
            override fun onEnableRedo(isEnable: Boolean) {
                buttonRedo.imageAlpha = if(isEnable) 255 else 50
            }
        })

        //set stroke width
        canvasEditorView.setStrokeWidth(strokeWidth)
        //set paint color
        canvasEditorView.setPaintColor(ContextCompat.getColor(this, R.color.colorBlack))

        buttonSticker.setOnClickListener{
            //Add drawable sticker
            val drawable = ContextCompat.getDrawable(this, R.drawable.app_icon)
            canvasEditorView.addDrawableSticker(drawable!!)
        }

        buttonText.setOnClickListener{
            //Add text sticker
            val color = ContextCompat.getColor(this, R.color.colorPrimary)
            canvasEditorView.addTextSticker("Canvas", color, null)
        }

        buttonStickerText.setOnClickListener{
            //Add text with drawable sticker
            val drawable = ContextCompat.getDrawable(this, R.drawable.ic_panorama_240dp)
            val color = ContextCompat.getColor(this, R.color.colorAccent)
            canvasEditorView.addDrawableTextSticker(drawable!!, "Canvas", color, null)
        }

        buttonBlack.setOnClickListener {
            buttonPlus.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_plus_black_24dp))
            buttonMinus.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_minus_black_24dp))
            canvasEditorView.setPaintColor(ContextCompat.getColor(this, R.color.colorBlack))
        }

        buttonYellow.setOnClickListener {
            buttonPlus.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_plus_yellow_24dp))
            buttonMinus.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_minus_yellow_24dp))
            canvasEditorView.setPaintColor(ContextCompat.getColor(this, R.color.colorYellow))
        }

        buttonPlus.setOnClickListener {
            strokeWidth += 10f
            canvasEditorView.setStrokeWidth(strokeWidth)
        }

        buttonMinus.setOnClickListener {
            strokeWidth -= 10f
            canvasEditorView.setStrokeWidth(strokeWidth)
        }

        buttonSave.setOnClickListener {
            val bitmap = canvasEditorView.downloadBitmap()
            showPreview(bitmap)
        }

        buttonUndo.setOnClickListener {
            canvasEditorView.undo()
        }

        buttonDelete.setOnClickListener {
            canvasEditorView.removeAll()
        }

        buttonRedo.setOnClickListener {
            canvasEditorView.redo()
        }

        buttonClose.setOnClickListener {
            viewImagePreview.visibility = View.GONE
        }
    }

    private fun showPreview(bitmap: Bitmap){
        viewImagePreview.visibility = View.VISIBLE
        imageView.setImageBitmap(bitmap)
    }
}
