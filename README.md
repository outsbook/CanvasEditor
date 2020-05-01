# CanvasEditor
A Canvas/Image Editor library with easy support for canvas/image editing using paints, drawable sticker, and text sticker in Android. The lib source code writeen using Kotlin language.

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

## Setup the CanvasEditor
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
<img src = "https://github.com/outsbook/CanvasEditor/blob/master/screenshot/screenshot_1.png?raw=true" width="256px" />

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