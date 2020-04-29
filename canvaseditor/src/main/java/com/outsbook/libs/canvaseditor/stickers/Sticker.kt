package com.outsbook.libs.canvaseditor.stickers

import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.RectF
import android.graphics.drawable.Drawable
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

internal abstract class Sticker {
    val matrix = Matrix()
    var isFlippedHorizontally = false
    var isFlippedVertically = false

    private val matrixValues = FloatArray(9)
    private val unrotatedWrapperCorner = FloatArray(8)
    private val unrotatedPoint = FloatArray(2)
    private val boundPoints = FloatArray(8)
    private val mappedBounds = FloatArray(8)
    private val trappedRect = RectF()

    abstract val width: Int
    abstract val height: Int
    abstract val drawable: Drawable

    val mappedBoundPoints: FloatArray
        get() {
            val dst = FloatArray(8)
            getMappedPoints(dst, getBoundPoints())
            return dst
        }
    private val bound: RectF
        get() {
            val bound = RectF()
            getBound(bound)
            return bound
        }
    val mappedBound: RectF
        get() {
            val dst = RectF()
            getMappedBound(dst, bound)
            return dst
        }
    private val centerPoint: PointF
        get() {
            val center = PointF()
            getCenterPoint(center)
            return center
        }
    val mappedCenterPoint: PointF
        get() {
            val pointF = centerPoint
            getMappedCenterPoint(pointF, FloatArray(2), FloatArray(2))
            return pointF
        }
    val currentScale: Float
        get() = getMatrixScale(matrix)
    val currentHeight: Float
        get() = getMatrixScale(matrix) * height
    val currentWidth: Float
        get() = getMatrixScale(matrix) * width
    private val currentAngle: Float
        get() = getMatrixAngle(matrix)

    companion object {
        fun getMatrix(sticker: Sticker): Matrix {
            return sticker.matrix
        }
    }

    abstract fun draw(canvas: Canvas)
    abstract fun setDrawable(drawable: Drawable): Sticker
    abstract fun setAlpha(alpha: Int): Sticker

    fun setMatrix(matrix: Matrix?): Sticker {
        this.matrix.set(matrix)
        return this
    }

    fun setFlippedHorizontally(flippedHorizontally: Boolean): Sticker {
        isFlippedHorizontally = flippedHorizontally
        return this
    }

    fun setFlippedVertically(flippedVertically: Boolean): Sticker {
        isFlippedVertically = flippedVertically
        return this
    }

    private fun getBoundPoints(): FloatArray {
        val points = FloatArray(8)
        getBoundPoints(points)
        return points
    }

    fun getBoundPoints(points: FloatArray) {
        if (!isFlippedHorizontally) {
            if (!isFlippedVertically) {
                points[0] = 0f
                points[1] = 0f
                points[2] = width.toFloat()
                points[3] = 0f
                points[4] = 0f
                points[5] = height.toFloat()
                points[6] = width.toFloat()
                points[7] = height.toFloat()
            } else {
                points[0] = 0f
                points[1] = height.toFloat()
                points[2] = width.toFloat()
                points[3] = height.toFloat()
                points[4] = 0f
                points[5] = 0f
                points[6] = width.toFloat()
                points[7] = 0f
            }
        } else {
            if (!isFlippedVertically) {
                points[0] = width.toFloat()
                points[1] = 0f
                points[2] = 0f
                points[3] = 0f
                points[4] = width.toFloat()
                points[5] = height.toFloat()
                points[6] = 0f
                points[7] = height.toFloat()
            } else {
                points[0] = width.toFloat()
                points[1] = height.toFloat()
                points[2] = 0f
                points[3] = height.toFloat()
                points[4] = width.toFloat()
                points[5] = 0f
                points[6] = 0f
                points[7] = 0f
            }
        }
    }

    fun getMappedPoints(src: FloatArray): FloatArray {
        val dst = FloatArray(src.size)
        matrix.mapPoints(dst, src)
        return dst
    }

    fun getMappedPoints(dst: FloatArray, src: FloatArray) {
        matrix.mapPoints(dst, src)
    }

    private fun getBound(dst: RectF) {
        dst[0f, 0f, width.toFloat()] = height.toFloat()
    }

    private fun getMappedBound(dst: RectF, bound: RectF) {
        matrix.mapRect(dst, bound)
    }

    fun getCenterPoint(dst: PointF) {
        dst[width * 1f / 2] = height * 1f / 2
    }

    fun getMappedCenterPoint(dst: PointF, mappedPoints: FloatArray,
                             src: FloatArray) {
        getCenterPoint(dst)
        src[0] = dst.x
        src[1] = dst.y
        getMappedPoints(mappedPoints, src)
        dst[mappedPoints[0]] = mappedPoints[1]
    }

    private fun getMatrixScale(matrix: Matrix): Float {
        return sqrt(
            getMatrixValue(matrix, Matrix.MSCALE_X).toDouble().pow(2.0) + getMatrixValue(
                matrix,
                Matrix.MSKEW_Y
            ).toDouble().pow(2.0)
        ).toFloat()
    }

    private fun getMatrixAngle(matrix: Matrix): Float {
        return Math.toDegrees(-atan2(getMatrixValue(matrix, Matrix.MSKEW_X).toDouble(),
            getMatrixValue(matrix, Matrix.MSCALE_X).toDouble())
        ).toFloat()
    }

    private fun getMatrixValue(matrix: Matrix, valueIndex: Int): Float {
        matrix.getValues(matrixValues)
        return matrixValues[valueIndex]
    }

    fun contains(x: Float, y: Float): Boolean {
        return contains(floatArrayOf(x, y))
    }

    fun contains(point: FloatArray): Boolean {
        val tempMatrix = Matrix()
        tempMatrix.setRotate(-currentAngle)
        getBoundPoints(boundPoints)
        getMappedPoints(mappedBounds, boundPoints)
        tempMatrix.mapPoints(unrotatedWrapperCorner, mappedBounds)
        tempMatrix.mapPoints(unrotatedPoint, point)
        trapToRect(trappedRect, unrotatedWrapperCorner)
        return trappedRect.contains(unrotatedPoint[0], unrotatedPoint[1])
    }

    private fun trapToRect(r: RectF, array: FloatArray) {
        r[Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY] = Float.NEGATIVE_INFINITY
        var i = 1
        while (i < array.size) {
            val x = (array[i - 1] * 10).roundToInt() / 10f
            val y = (array[i] * 10).roundToInt() / 10f
            r.left = if (x < r.left) x else r.left
            r.top = if (y < r.top) y else r.top
            r.right = if (x > r.right) x else r.right
            r.bottom = if (y > r.bottom) y else r.bottom
            i += 2
        }
        r.sort()
    }
}