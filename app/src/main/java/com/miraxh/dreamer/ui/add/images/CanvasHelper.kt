package com.miraxh.dreamer.ui.add.images

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.content.res.ResourcesCompat
import com.miraxh.dreamer.R
import com.miraxh.dreamer.util.STROKE_WIDTH
import java.io.File
import java.io.FileOutputStream
import java.util.*


class CanvasHelper(context: Context, attrs: AttributeSet) : View(context, attrs) {

    // Holds the path you are currently drawing.
    private var path = Path()

    private var drawColor = ResourcesCompat.getColor(resources, R.color.colorWhite, null)
    private val backgroundColor =
        ResourcesCompat.getColor(resources, R.color.colorPrimaryDark, null)
    private var strokeThickness = STROKE_WIDTH
    private lateinit var extraCanvas: Canvas
    private lateinit var extraBitmap: Bitmap
    private lateinit var frame: Rect
    private lateinit var titleCanvas: String

    // Set up the paint with which to draw.
    private var paint = Paint().apply {
        color = drawColor
        // Smooths out edges of what is drawn without affecting shape.
        isAntiAlias = true
        // Dithering affects how colors with higher-precision than the device are down-sampled.
        isDither = true
        style = Paint.Style.STROKE // default: FILL
        strokeJoin = Paint.Join.ROUND // default: MITER
        strokeCap = Paint.Cap.ROUND // default: BUTT
        strokeWidth = STROKE_WIDTH // default: Hairline-width (really thin)
    }

    /**
     * Don't draw every single pixel.
     * If the finger has has moved less than this distance, don't draw. scaledTouchSlop, returns
     * the distance in pixels a touch can wander before we think the user is scrolling.
     */
    private val touchTolerance = ViewConfiguration.get(context).scaledTouchSlop

    private var currentX = 0f
    private var currentY = 0f

    private var motionTouchEventX = 0f
    private var motionTouchEventY = 0f

    /**
     * Called whenever the view changes size.
     * Since the view starts out with no size, this is also called after
     * the view has been inflated and has a valid size.
     */
    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)

        if (::extraBitmap.isInitialized) extraBitmap.recycle()
        extraBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        extraCanvas = Canvas(extraBitmap)
        extraCanvas.drawColor(backgroundColor)

        // Calculate a rectangular frame around the picture.
        val inset = 40
        frame = Rect(inset, inset, width - inset, height - inset)
    }

    override fun onDraw(canvas: Canvas) {
        // Draw the bitmap that has the saved path.
        canvas.drawBitmap(extraBitmap, 0f, 0f, null)
        // Draw a frame around the canvas.
        //extraCanvas.drawRect(frame, paint)
    }

    /**
     * No need to call and implement MyCanvasView#performClick, because MyCanvasView custom view
     * does not handle click actions.
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        motionTouchEventX = event.x
        motionTouchEventY = event.y



        when (event.action) {
            MotionEvent.ACTION_DOWN -> touchStart()
            MotionEvent.ACTION_MOVE -> touchMove()
            MotionEvent.ACTION_UP -> touchUp()
        }
        return true
    }

    /**
     * The following methods factor out what happens for different touch events,
     * as determined by the onTouchEvent() when statement.
     * This keeps the when conditional block
     * concise and makes it easier to change what happens for each event.
     * No need to call invalidate because we are not drawing anything.
     */
    private fun touchStart() {
        path.reset()
        path.moveTo(motionTouchEventX, motionTouchEventY)
        currentX = motionTouchEventX
        currentY = motionTouchEventY
    }

    private fun touchMove() {
        val dx = Math.abs(motionTouchEventX - currentX)
        val dy = Math.abs(motionTouchEventY - currentY)
        if (dx >= touchTolerance || dy >= touchTolerance) {
            // QuadTo() adds a quadratic bezier from the last point,
            // approaching control point (x1,y1), and ending at (x2,y2).
            path.quadTo(
                currentX,
                currentY,
                (motionTouchEventX + currentX) / 2,
                (motionTouchEventY + currentY) / 2
            )
            currentX = motionTouchEventX
            currentY = motionTouchEventY
            // Draw the path in the extra bitmap to save it.
            extraCanvas.drawPath(path, paint)
        }
        // Invalidate() is inside the touchMove() under ACTION_MOVE because there are many other
        // types of motion events passed into this listener, and we don't want to invalidate the
        // view for those.
        invalidate()
    }

    private fun touchUp() {
        // Reset the path so it doesn't get drawn again.
        path.reset()
    }

    fun changeColor(color: Int) {
        when (color) {
            0 -> drawColor = backgroundColor
            1 -> drawColor = ResourcesCompat.getColor(resources, R.color.colorWhite, null)
            2 -> drawColor = ResourcesCompat.getColor(resources, R.color.color2, null)
            3 -> drawColor = ResourcesCompat.getColor(resources, R.color.color3, null)
            4 -> drawColor = ResourcesCompat.getColor(resources, R.color.color4, null)
            5 -> drawColor = ResourcesCompat.getColor(resources, R.color.color5, null)
            6 -> drawColor = ResourcesCompat.getColor(resources, R.color.color6, null)
            7 -> drawColor = ResourcesCompat.getColor(resources, R.color.color7, null)
        }
        setPaint()
    }

    fun changeThickness(thickness: Int) {
        when (thickness) {
            0 -> strokeThickness = 2f
            1 -> strokeThickness = 6f
            2 -> strokeThickness = 10f
            3 -> strokeThickness = 14f
            4 -> strokeThickness = 22f
            5 -> strokeThickness = 30f
            6 -> strokeThickness = 38f
            7 -> strokeThickness = 54f
            8 -> strokeThickness = 70f
            9 -> strokeThickness = 96f
            10 -> strokeThickness = 128f
        }
        setPaint()
    }

    fun resetCanvas() {
        extraCanvas.drawColor(backgroundColor)
        invalidate()
        path.reset()
    }

    fun setPaint() {
        paint = Paint().apply {
            color = drawColor
            // Smooths out edges of what is drawn without affecting shape.
            isAntiAlias = true
            // Dithering affects how colors with higher-precision than the device are down-sampled.
            isDither = true
            style = Paint.Style.STROKE // default: FILL
            strokeJoin = Paint.Join.ROUND // default: MITER
            strokeCap = Paint.Cap.ROUND // default: BUTT
            strokeWidth = strokeThickness // default: Hairline-width (really thin)
        }
    }

    @SuppressLint("WrongThread")
    fun saveImage(): String {
        var toRtn = "null"
        try {
            createUniqueName()
            val folderName = "images_files"
            val myDirectory =
                File(context?.getExternalFilesDir(null)?.absolutePath, folderName)
            if (!myDirectory.exists()) {
                myDirectory.mkdirs()
            }
            val filename: String =
                context?.getExternalFilesDir(null)?.absolutePath + "/$folderName/"
            Log.i("test_save_canvas", filename)
            val f = File(filename, "$titleCanvas.png")
            f.createNewFile()
            val out = FileOutputStream(f)
            extraBitmap.compress(Bitmap.CompressFormat.PNG, 90, out)
            toRtn = "$titleCanvas.png"
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return toRtn
    }

    private fun createUniqueName() {
        val cal = Calendar.getInstance()
        titleCanvas = cal.time.toString()

        //trimming della string
        titleCanvas = titleCanvas.replace(' ', '_').toLowerCase()
        titleCanvas = titleCanvas.replace(':', '_').toLowerCase()
        titleCanvas = titleCanvas.replace('+', '_').toLowerCase()
    }
}