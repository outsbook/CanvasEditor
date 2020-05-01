package com.outsbook.examplejava;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.outsbook.libs.canvaseditor.CanvasEditorView;
import com.outsbook.libs.canvaseditor.listeners.CanvasEditorListener;

public class MainActivity extends AppCompatActivity {

    private CanvasEditorView canvasEditor;

    private ImageButton buttonSticker;
    private ImageButton buttonText;
    private ImageButton buttonStickerText;
    private ImageButton buttonBlack;
    private ImageButton buttonYellow;
    private ImageButton buttonPlus;
    private ImageButton buttonMinus;

    private ImageButton buttonSave;
    private ImageButton buttonUndo;
    private ImageButton buttonRedo;
    private ImageButton buttonDelete;

    private View viewImagePreview;
    private ImageButton buttonClose;
    private ImageView imageView;

    private Float strokeWidth = 20f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initValue();
        initClickListener();
        initCanvasEditorListener();
    }

    private void initView(){
        canvasEditor = findViewById(R.id.canvasEditor);

        buttonSticker = findViewById(R.id.buttonSticker);
        buttonText = findViewById(R.id.buttonText);
        buttonStickerText = findViewById(R.id.buttonStickerText);
        buttonBlack = findViewById(R.id.buttonBlack);
        buttonYellow = findViewById(R.id.buttonYellow);
        buttonPlus = findViewById(R.id.buttonPlus);
        buttonMinus = findViewById(R.id.buttonMinus);

        buttonSave = findViewById(R.id.buttonSave);
        buttonUndo = findViewById(R.id.buttonUndo);
        buttonRedo = findViewById(R.id.buttonRedo);
        buttonDelete = findViewById(R.id.buttonDelete);

        viewImagePreview = findViewById(R.id.viewImagePreview);
        buttonClose = findViewById(R.id.buttonClose);
        imageView = findViewById(R.id.imageView);
    }

    private void initValue(){
        buttonUndo.setImageAlpha(50);
        buttonRedo.setImageAlpha(50);
        //set stroke width
        canvasEditor.setStrokeWidth(strokeWidth);
        //set paint color
        canvasEditor.setPaintColor(ContextCompat.getColor(this, R.color.colorBlack));
    }

    private void initClickListener(){
        buttonSticker.setOnClickListener(v -> {
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.app_icon);
            if(drawable != null)
                canvasEditor.addDrawableSticker(drawable);
        });
        buttonText.setOnClickListener(v -> {
            String text = "Canvas";
            int color = ContextCompat.getColor(this, R.color.colorPrimary);
            canvasEditor.addTextSticker(text, color, null);
        });
        buttonStickerText.setOnClickListener(v -> {
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_panorama_240dp);
            String text = "Canvas";
            int textColor = ContextCompat.getColor(this, R.color.colorAccent);
            if(drawable != null)
                canvasEditor.addDrawableTextSticker(drawable, text, textColor, null);
        });
        buttonBlack.setOnClickListener(v -> {
            buttonPlus.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_plus_black_24dp));
            buttonMinus.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_minus_black_24dp));
            int color = ContextCompat.getColor(this, R.color.colorBlack);
            canvasEditor.setPaintColor(color);
        });
        buttonYellow.setOnClickListener(v -> {
            buttonPlus.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_plus_yellow_24dp));
            buttonMinus.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_minus_yellow_24dp));
            int color = ContextCompat.getColor(this, R.color.colorYellow);
            canvasEditor.setPaintColor(color);
        });
        buttonPlus.setOnClickListener(v -> {
            strokeWidth += 10f;
            canvasEditor.setStrokeWidth(strokeWidth);
        });
        buttonMinus.setOnClickListener(v -> {
            strokeWidth -= 10f;
            canvasEditor.setStrokeWidth(strokeWidth);
        });

        buttonSave.setOnClickListener(v -> {
            Bitmap bitmap = canvasEditor.downloadBitmap();
            imageView.setImageBitmap(bitmap);
            viewImagePreview.setVisibility(View.VISIBLE);
        });
        buttonUndo.setOnClickListener(v -> {
            canvasEditor.undo();
        });
        buttonRedo.setOnClickListener(v -> {
            canvasEditor.redo();
        });
        buttonDelete.setOnClickListener(v -> {
            canvasEditor.removeAll();
        });

        buttonClose.setOnClickListener(v -> {
            viewImagePreview.setVisibility(View.GONE);
        });
    }

    private void initCanvasEditorListener(){
        canvasEditor.setListener(new CanvasEditorListener() {
            @Override
            public void onEnableUndo(boolean isEnable) {
                // isEnable = true (undo list is not empty)
                // isEnable = false (undo list is empty)
                buttonUndo.setImageAlpha(isEnable? 255 : 50);
            }

            @Override
            public void onEnableRedo(boolean isEnable) {
                // isEnable = true (redo list is not empty)
                // isEnable = false (redo list is empty)
                buttonRedo.setImageAlpha(isEnable? 255 : 50);
            }

            @Override
            public void onTouchEvent(MotionEvent motionEvent) {
                //When the canvas touch
            }

            @Override
            public void onStickerActive() {
                //When a sticker change to active mode
            }

            @Override
            public void onStickerRemove() {
                //When a sticker remove from canvas
            }

            @Override
            public void onStickerDone() {
                //When the active sticker added to canvas
            }

            @Override
            public void onStickerZoomAndRotate() {
                //When the active sticker zoom or rotate
            }

            @Override
            public void onStickerFlip() {
                //When the active sticker flip
            }
        });
    }
}
