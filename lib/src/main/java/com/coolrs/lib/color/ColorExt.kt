package com.coolrs.lib.color

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.Matrix3f
import android.renderscript.Matrix4f
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicColorMatrix
import com.coolrs.lib.ScriptC_histogram

private object ColorConstants {
    val SEPIA_MATRIX = Matrix3f(floatArrayOf(0.393f, 0.349f, 0.272f, 0.769f, 0.686f, 0.534f, 0.189f, 0.168f, 0.131f))
    val INVERT_MATRIX = Matrix4f(floatArrayOf(-1f, 0f, 0f, 0f, 0f, -1f, 0f, 0f, 0f, 0f, -1f, 0f, 1f, 1f, 1f, 0f))
    val BLACK_AND_WHITE_MATRIX = Matrix4f(floatArrayOf(1.5f, 1.5f, 1.5f, 0f, 1.5f, 1.5f, 1.5f, 0f, 1.5f, 1.5f, 1.5f, 0f, -1f, -1f, -1f, 0f))
    val POLAROID_MATRIX = Matrix4f(floatArrayOf(1.438f, -0.062f, -0.062f, 0f, -0.122f, 1.378f, -0.122f, 0f, -0.016f, -0.016f, 1.483f, 0f, -0.03f, 0.05f, -0.02f, 0f))
}

fun Bitmap?.grayscale(context: Context): Bitmap? {
    if (this == null) return null
    val rs = RenderScript.create(context)
    val allocIn = Allocation.createFromBitmap(rs, this)
    val allocOut = Allocation.createTyped(rs, allocIn.type)
    val script = ScriptIntrinsicColorMatrix.create(rs)
    script.setGreyscale()
    script.forEach(allocIn, allocOut)
    allocOut.copyTo(this)
    allocIn.destroy()
    rs.destroy()
    return this
}

fun Bitmap?.colorMatrix(context: Context, matrix: Matrix3f): Bitmap? {
    if (this == null) return null
    val rs = RenderScript.create(context)
    val allocIn = Allocation.createFromBitmap(rs, this)
    val allocOut = Allocation.createTyped(rs, allocIn.type)
    val script = ScriptIntrinsicColorMatrix.create(rs)
    script.setColorMatrix(matrix)
    script.forEach(allocIn, allocOut)
    allocOut.copyTo(this)
    allocIn.destroy()
    allocOut.destroy()
    rs.destroy()
    return this
}

fun Bitmap?.colorMatrix(context: Context, matrix: Matrix4f): Bitmap? {
    if (this == null) return null
    val rs = RenderScript.create(context)
    val allocIn = Allocation.createFromBitmap(rs, this)
    val allocOut = Allocation.createTyped(rs, allocIn.type)
    val script = ScriptIntrinsicColorMatrix.create(rs)
    script.setColorMatrix(matrix)
    script.forEach(allocIn, allocOut)
    allocOut.copyTo(this)
    allocIn.destroy()
    allocOut.destroy()
    rs.destroy()
    return this
}

fun Bitmap?.histogramEqualization(context: Context): Bitmap? {
    if (this == null) return null
    val rs = RenderScript.create(context)
    val allocIn = Allocation.createFromBitmap(rs, this)
    val allocOut = Allocation.createTyped(rs, allocIn.type)
    val script = ScriptC_histogram(rs)
    script._size = (width * height).toLong()
    script.forEach_createHistogram(allocIn, allocOut)
    script.invoke_parseHistogram()
    script.forEach_applyHistogram(allocOut, allocIn)
    allocIn.copyTo(this)
    allocIn.destroy()
    allocOut.destroy()
    rs.destroy()
    return this
}

fun Bitmap?.polaroid(context: Context): Bitmap? = colorMatrix(context, ColorConstants.POLAROID_MATRIX)

fun Bitmap?.invert(context: Context): Bitmap? = colorMatrix(context, ColorConstants.INVERT_MATRIX)

fun Bitmap?.blackAndWhite(context: Context): Bitmap? = colorMatrix(context, ColorConstants.BLACK_AND_WHITE_MATRIX)

fun Bitmap?.sepia(context: Context): Bitmap? = colorMatrix(context, ColorConstants.SEPIA_MATRIX)

fun Bitmap?.brightness(c: Context, r: Float, g: Float, b: Float): Bitmap? = colorMatrix(c, Matrix3f(floatArrayOf(r, 0f, 0f, 0f, g, 0f, 0f, 0f, b)))

fun Bitmap?.brightness(c: Context, v: Float): Bitmap? = brightness(c, v, v, v)

fun Bitmap?.saturate(c: Context, sr: Float, sg: Float, sb: Float, r: Float = 0.3086f, g: Float = 0.6094f, b: Float = 0.0820f): Bitmap? {
    return colorMatrix(c, Matrix3f(floatArrayOf((1.0f - sr) * r + sr, (1.0f - sr) * r, (1.0f - sr)*r, (1.0f - sg) * g, (1.0f - sg)* g + sg,
            (1.0f - sg) * g, (1.0f - sb) * b, (1.0f - sb) * b, (1.0f - sb) * b + sb)))
}

fun Bitmap?.saturate(c: Context, s: Float): Bitmap? = saturate(c, s, s, s)

fun Bitmap?.offset(c: Context, r: Float, g: Float, b: Float): Bitmap? =
        colorMatrix(c, Matrix4f(floatArrayOf(1.0f, 0f, 0f, 0f, 0f, 1.0f, 0f, 0f, 0f, 0f, 1.0f, 0.0f, r, g, b, 1.0f)))

fun Bitmap?.offset(c: Context, v: Float): Bitmap? = offset(c, v, v, v)
