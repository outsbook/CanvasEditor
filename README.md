# CanvasEditor
A Canvas/Image Editor library with easy support for canvas/image editing using paints, drawable sticker, and text sticker in Android. The lib source code writeen using Kotlin language.

## Index
|  Title | Description/Methods  |
| ------------ | ------------ |
|  [**Getting Started**](#getting-started) | Install the canvas editor library to your project |
|  [**Setup the Canvas Editor**](#setup-the-canvas-editor) | Setup the canvas editor to your project activity and activity layout |
| [**Drawing**](#drawing) | [1. setPaintColor(color: Int)](#1-setpaintcolorcolor-int)<br/> [2. setStrokeWidth(strokeWidth: Float)](#2-setstrokewidthstrokeWidth-float) |
| [**Drawable/Bitmap Sticker**](#drawablebitmap-sticker) | [1. addDrawableSticker(drawable: Drawable)](#1-adddrawablestickerdrawable-drawable)<br>[2. addBitmapSticker(bitmap: Bitmap)](#2-addbitmapstickerbitmap-bitmap)|
| [**Text Sticker**](#text-sticker) | [1. addTextSticker(text: String, textColor: Int, typeface: Typeface?)](#1-addtextStickertext-string-textcolor-int-typeface-typeface)<br>[2. addDrawableTextSticker(drawable: Drawable, text: String, textColor: Int, typeface: Typeface?)](#2-adddrawabletextstickerdrawable-drawable-text-string-textColor-int-typeface-typeface) |
| [**Active Sticker Methods**](#active-sticker-methods) | [1. removeActiveSticker()](#1-removeactivesticker)<br>[2. doneActiveSticker()](#2-doneactivesticker)<br>[3. flipActiveSticker()](#3-flipactivesticker)<br>[4. zoomAndRotateActiveSticker(motionEvent: MotionEvent)](#4-zoomandrotateactivestickermotionevent-motionevent) |
| [**Canvas Editor Methods**](#canvas-editor-methods) | [1. undo()](#1-undo)<br>[2. redo()](#2-redo)<br>[3. removeAll()](#3-removeall)<br>[4. downloadBitmap(): Bitmap](#4-downloadbitmap-bitmap) |
| [**Canvas Editor Callback**](#canvas-editor-callback) | Set the listener for access callback functions |

## Getting Started
To include the library in your project just simply add the dependencies. Choose one from Gradle, and Maven
#### Gradle
```groovy
implementation 'com.outsbook.libs:canvaseditor:1.0.0'
```
#### Maven
```xml
<dependency>
  <groupId>com.outsbook.libs</groupId>
  <artifactId>canvaseditor</artifactId>
  <version>1.0.0</version>
  <type>pom</type>
</dependency>
```

## Setup the Canvas Editor
#### Add the `CanvasEditorView` to your Activity/Fragment layout
```xml
<com.outsbook.libs.canvaseditor.CanvasEditorView
        android:id="@+id/canvasEditor"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
```
#### Get the `CanvasEditor` in your Activity
##### Kotlin
```kotlin
import com.outsbook.libs.canvaseditor.CanvasEditorView

class MainActivity : AppCompatActivity() {
    private lateinit var canvasEditor: CanvasEditorView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
	canvasEditor = findViewById(R.id.canvasEditor)
    }
}
```
##### Java
```java
import com.outsbook.libs.canvaseditor.CanvasEditorView;

public class MainActivity extends AppCompatActivity {
    private CanvasEditorView canvasEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        canvasEditor = findViewById(R.id.canvasEditor);
    }
}
```
##### Preview
![](https://github.com/outsbook/CanvasEditor/blob/master/screenshot/screenshot_1.png?raw=true)

Now you are ready to play with `CanvasEditor`

## Drawing
| # |  Method | Action  |
| ------------ | ------------ | ------------ |
| 1 |  [setPaintColor(color: Int)](#1-setpaintcolorcolor-int) |  Set the brush color to paint |
| 2 |  [setStrokeWidth(strokeWidth: Float)](#2-setstrokewidthstrokeWidth-float) |  Set the brush stroke width to paint |
#### 1. setPaintColor(color: Int)
##### Kotlin
```kotlin
val color = ContextCompat.getColor(this, R.color.colorBlack)
canvasEditor.setPaintColor(color)
```
##### Java
```java
int color = ContextCompat.getColor(this, R.color.colorBlack);
canvasEditor.setPaintColor(color);
```
##### Preview
![](https://github.com/outsbook/CanvasEditor/blob/master/screenshot/screenshot_2.png?raw=true)

#### 2. setStrokeWidth(strokeWidth: Float)
##### Kotlin
```kotlin
val strokeWidth = 20f
canvasEditor.setStrokeWidth(strokeWidth)
```
##### Java
```java
float strokeWidth = 20f;
canvasEditor.setStrokeWidth(strokeWidth);
```
##### Preview
![](https://github.com/outsbook/CanvasEditor/blob/master/screenshot/screenshot_3.png?raw=true)

## Drawable/Bitmap Sticker
| # |  Method | Action  |
| ------------ | ------------ | ------------ |
| 1 |  [addDrawableSticker(drawable: Drawable)](#1-adddrawablestickerdrawable-drawable) |  Add drawable sticker to the canvas editor |
| 2 |  [addBitmapSticker(bitmap: Bitmap)](#2-addbitmapstickerbitmap-bitmap) |  Add bitmap sticker to the canvas editor |
#### 1. addDrawableSticker(drawable: Drawable)
##### Kotlin
```kotlin
val drawable = ContextCompat.getDrawable(this, R.drawable.app_icon)
drawable?.let{
    canvasEditor.addDrawableSticker(drawable)
}
```
##### Java
```java
Drawable drawable = ContextCompat.getDrawable(this, R.drawable.app_icon);
if(drawable != null){
    canvasEditor.addDrawableSticker(drawable);
}
```
##### Preview
![](https://github.com/outsbook/CanvasEditor/blob/master/screenshot/screenshot_4.png?raw=true)

#### 2. addBitmapSticker(bitmap: Bitmap)

##### Kotlin
```kotlin
val bitmap = //get your bitmap
bitmap?.let{
    canvasEditor.addBitmapSticker(bitmap)
}
```
##### Java
```java
Bitmap bitmap = //get your bitmap
if(bitmap != null){
    canvasEditor.addBitmapSticker(drawable);
}
```
##### Preview
![](https://github.com/outsbook/CanvasEditor/blob/master/screenshot/screenshot_5.png?raw=true)

## Text Sticker
| # |  Method | Action  |
| ------------ | ------------ | ------------ |
| 1 |  [addTextSticker(text: String, textColor: Int, typeface: Typeface?)](#1-addtextStickertext-string-textcolor-int-typeface-typeface) |  Add text sticker to the canvas editor |
| 2 |  [addDrawableTextSticker(drawable: Drawable, text: String, textColor: Int, typeface: Typeface?)](#2-adddrawabletextstickerdrawable-drawable-text-string-textColor-int-typeface-typeface) |  Add text sticker with drawable background to the canvas editor |
#### 1. addTextSticker(text: String, textColor: Int, typeface: Typeface?)
##### Kotlin
```kotlin
val text = "Canvas"
val textColor = ContextCompat.getColor(this, R.color.colorPrimary)
canvasEditor.addTextSticker(text, textColor, null)
```
##### Java
```java
String text = "Canvas";
int color = ContextCompat.getColor(this, R.color.colorPrimary);
canvasEditor.addTextSticker(text, color, null);
```
##### Preview
![](https://github.com/outsbook/CanvasEditor/blob/master/screenshot/screenshot_6.png?raw=true)

#### 2. addDrawableTextSticker(drawable: Drawable, text: String, textColor: Int, typeface: Typeface?)
##### Kotlin
```kotlin
val drawable = ContextCompat.getDrawable(this, R.drawable.ic_panorama_240dp)
val text = "Canvas"
val textColor = ContextCompat.getColor(this, R.color.colorYellow)
drawable?.let{
    canvasEditor.addDrawableTextSticker(it, text, textColor, null)
}
```
##### Java
```java
Drawable drawable = ContextCompat.getDrawable(this, R.drawable.ic_panorama_240dp);
String text = "Canvas";
int textColor = ContextCompat.getColor(this, R.color.colorYellow);
if(drawable != null){
    canvasEditor.addDrawableTextSticker(drawable, text, textColor, null);
}
```
##### Preview
![](https://github.com/outsbook/CanvasEditor/blob/master/screenshot/screenshot_7.png?raw=true)

## Active Sticker Methods
| # |  Method | Action  |
| ------------ | ------------ | ------------ |
| 1 |  [removeActiveSticker()](#1-removeactivesticker) |  Remove active sticker from canvas editor |
| 2 |  [doneActiveSticker()](#2-doneactivesticker) |  Editing done of active sticker on canvas editor |
| 3 |  [flipActiveSticker()](#3-flipactivesticker) |  Flip active sticker on canvas editor |
| 4 |  [zoomAndRotateActiveSticker(motionEvent: MotionEvent)](#4-zoomandrotateactivestickermotionevent-motionevent) |  Zoom and rotate active sticker with motihn event on canvas editor|
#### 1. removeActiveSticker()
##### Kotlin
```kotlin
canvasEditor.removeActiveSticker()
```
##### Java
```java
canvasEditor.removeActiveSticker();
```
#### 2. doneActiveSticker()
##### Kotlin
```kotlin
canvasEditor.doneActiveSticker()
```
##### Java
```java
canvasEditor.doneActiveSticker();
```
#### 3. flipActiveSticker()
##### Kotlin
```kotlin
canvasEditor.flipActiveSticker()
```
##### Java
```java
canvasEditor.flipActiveSticker();
```
#### 4. zoomAndRotateActiveSticker(motionEvent: MotionEvent)
##### Kotlin
```kotlin
val motionEvent = //Set your motion event
canvasEditor.zoomAndRotateActiveSticker(motionEvent)
```
##### Java
```java
MotionEvent motionEvent = //Set your motion event
canvasEditor.zoomAndRotateActiveSticker(motionEvent);
```
## Canvas Editor Methods
| # |  Method | Action  |
| ------------ | ------------ | ------------ |
| 1 |  [undo()](#1-undo) |  Undo from canvas editor |
| 2 |  [redo()](#2-redo) |  Redo to canvas editor |
| 3 |  [removeAll()](#3-removeall) |  Delete everything from canvas editor |
| 4 |  [downloadBitmap(): Bitmap](#4-downloadbitmap-bitmap) |  Get the canvas as bitmap, you can play with the bitmap :)|
#### 1. undo()
##### Kotlin
```kotlin
canvasEditor.undo()
```
##### Java
```java
canvasEditor.undo();
```
#### 2. redo()
##### Kotlin
```kotlin
canvasEditor.redo()
```
##### Java
```java
canvasEditor.redo();
```
#### 3. removeAll()
##### Kotlin
```kotlin
canvasEditor.removeAll()
```
##### Java
```java
canvasEditor.removeAll();
```
#### 4. downloadBitmap(): Bitmap
##### Kotlin
```kotlin
val bitmap = canvasEditor.downloadBitmap()
```
##### Java
```java
Bitmap bitmap = canvasEditor.downloadBitmap();
```

## Canvas Editor Callback
##### Kotlin
```kotlin
import com.outsbook.libs.canvaseditor.CanvasEditorView
import com.outsbook.libs.canvaseditor.listeners.CanvasEditorListener

class MainActivity : AppCompatActivity() {
    private lateinit var canvasEditor: CanvasEditorView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        canvasEditor = findViewById(R.id.canvasEditor)

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
```
##### Java
```java
import com.outsbook.libs.canvaseditor.CanvasEditorView;
import com.outsbook.libs.canvaseditor.listeners.CanvasEditorListener;

public class MainActivity extends AppCompatActivity {
    private CanvasEditorView canvasEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        canvasEditor = findViewById(R.id.canvasEditor);

        canvasEditor.setListener(new CanvasEditorListener() {
            @Override
            public void onEnableUndo(boolean isEnable) {
                // isEnable = true (undo list is not empty)
                // isEnable = false (undo list is empty)
            }

            @Override
            public void onEnableRedo(boolean isEnable) {
                // isEnable = true (redo list is not empty)
                // isEnable = false (redo list is empty)
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
```