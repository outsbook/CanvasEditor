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
Now you are ready for play with `CanvasEditor`