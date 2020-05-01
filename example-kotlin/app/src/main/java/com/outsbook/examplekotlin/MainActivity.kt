package com.outsbook.examplekotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.outsbook.libs.canvaseditor.listeners.CanvasEditorListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var strokeWidth: Float = 20f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initValue()
        initClickListener()
        initCanvasEditorListener()
    }

    private fun initValue(){
        buttonUndo.imageAlpha = 50
        buttonRedo.imageAlpha = 50
        //set stroke width
        canvasEditor.setStrokeWidth(strokeWidth)
        //set paint color
        canvasEditor.setPaintColor(ContextCompat.getColor(this, R.color.colorBlack))
    }

    private fun initClickListener(){
        buttonSticker.setOnClickListener{
            //Add drawable sticker
            val drawable = ContextCompat.getDrawable(this, R.drawable.app_icon)
            drawable?.let {
                canvasEditor.addDrawableSticker(it)
            }
        }

        buttonText.setOnClickListener{
            //Add text sticker
            val text = "Canvas"
            val textColor = ContextCompat.getColor(this, R.color.colorPrimary)
            canvasEditor.addTextSticker(text, textColor, null)
        }

        buttonStickerText.setOnClickListener{
            //Add text with drawable sticker
            val drawable = ContextCompat.getDrawable(this, R.drawable.ic_panorama_240dp)
            val text = "Canvas"
            val textColor = ContextCompat.getColor(this, R.color.colorAccent)
            drawable?.let{
                canvasEditor.addDrawableTextSticker(it, text, textColor, null)
            }
        }

        buttonBlack.setOnClickListener {
            buttonPlus.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_plus_black_24dp))
            buttonMinus.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_minus_black_24dp))
            val color = ContextCompat.getColor(this, R.color.colorBlack)
            canvasEditor.setPaintColor(color)
        }

        buttonYellow.setOnClickListener {
            buttonPlus.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_plus_yellow_24dp))
            buttonMinus.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_minus_yellow_24dp))
            val color = ContextCompat.getColor(this, R.color.colorYellow)
            canvasEditor.setPaintColor(color)
        }

        buttonPlus.setOnClickListener {
            strokeWidth += 10f
            canvasEditor.setStrokeWidth(strokeWidth)
        }

        buttonMinus.setOnClickListener {
            strokeWidth -= 10f
            canvasEditor.setStrokeWidth(strokeWidth)
        }

        buttonSave.setOnClickListener {
            val bitmap = canvasEditor.downloadBitmap()
            imageView.setImageBitmap(bitmap)
            viewImagePreview.visibility = View.VISIBLE
        }

        buttonUndo.setOnClickListener {
            canvasEditor.undo()
        }

        buttonDelete.setOnClickListener {
            canvasEditor.removeAll()
        }

        buttonRedo.setOnClickListener {
            canvasEditor.redo()
        }

        buttonClose.setOnClickListener {
            viewImagePreview.visibility = View.GONE
        }
    }

    private fun initCanvasEditorListener(){
        canvasEditor.setListener(object: CanvasEditorListener {
            override fun onEnableUndo(isEnable: Boolean) {
                // isEnable = true (undo list is not empty)
                // isEnable = false (undo list is empty)
                buttonUndo.imageAlpha = if(isEnable) 255 else 50
            }

            override fun onEnableRedo(isEnable: Boolean) {
                // isEnable = true (redo list is not empty)
                // isEnable = false (redo list is empty)
                buttonRedo.imageAlpha = if(isEnable) 255 else 50
            }

            override fun onTouchEvent(event: MotionEvent) {
                //When the canvas touch
            }

            override fun onStickerActive() {
                //When a sticker change to active mode
            }

            override fun onStickerRemove() {
                //When a sticker remove from canvas
            }

            override fun onStickerDone() {
                //When the active sticker added to canvas
            }

            override fun onStickerZoomAndRotate() {
                //When the active sticker zoom or rotate
            }

            override fun onStickerFlip() {
                //When the active sticker flip
            }
        })
    }
}
