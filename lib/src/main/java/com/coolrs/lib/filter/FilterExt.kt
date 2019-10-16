package com.coolrs.lib.filter

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import com.coolrs.lib.ScriptC_convolve

private object FilterConstants {
    const val DEFAULT_BLUR_RADIUS = 5.0f
    const val BLUR = 1.0f / 9.0f
    const val MATRIX_SIZE = 9
    val SHARPEN_MATRIX = floatArrayOf(0.0f, -1.0f, 0.0f, -1.0f, 5.0f, -1.0f, 0.0f, -1.0f, 0.0f)
    val EDGE_MATRIX = floatArrayOf(-1.0f, -1.0f, -1.0f, -1.0f, 8.0f, -1.0f, -1.0f, -1.0f, -1.0f)
    val EMBOSS_MATRIX = floatArrayOf(-2.0f, -1.0f, 0.0f, -1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 2.0f)
    val BLOOM_MATRIX = floatArrayOf(0.0f, 20.0f / 7.0f, 0.0f, 20.0f / 7.0f, -59.0f / 7.0f, 20.0f / 7.0f, 1.0f / 7.0f, 13.0f / 7.0f, 0.0f)
    val BLUR_MATRIX = floatArrayOf(
		BLUR,
		BLUR,
		BLUR,
		BLUR,
		BLUR,
		BLUR,
		BLUR,
		BLUR,
		BLUR
	)
}

/**
 * Run the blur effect over the Bitmap. Original bitmap is modified and returned.
 */
fun Bitmap?.blur(context: Context, radius: Float = FilterConstants.DEFAULT_BLUR_RADIUS): Bitmap? {
    if (this == null) return null

    val renderScript = RenderScript.create(context)
    val inAlloc = Allocation.createFromBitmap(renderScript, this)
    val outAlloc = Allocation.createTyped(renderScript, inAlloc.type)

    val script = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))
    script.setRadius(radius)
    script.setInput(inAlloc)
    script.forEach(outAlloc)
    outAlloc.copyTo(this)

    renderScript.destroy()

    return this
}

fun Bitmap?.convolve(context: Context, kernel: FloatArray): Bitmap? {
    if (this == null) return null
    val rs = RenderScript.create(context)
    val allocIn = Allocation.createFromBitmap(rs, this)
    val allocOut = Allocation.createTyped(rs, allocIn.type)
    val allocKernel = Allocation.createSized(rs, Element.F32(rs), FilterConstants.MATRIX_SIZE, Allocation.USAGE_SCRIPT)
    allocKernel.copyFrom(kernel)
    val script = ScriptC_convolve(rs)
    script._width = width.toLong()
    script._height = height.toLong()
    script._in = allocIn
    script.bind_kernel(allocKernel)
    script.forEach_convolve(allocOut)
    allocOut.copyTo(this)
    allocIn.destroy()
    allocOut.destroy()
    allocKernel.destroy()
    rs.destroy()
    return this
}

fun Bitmap?.sharpen(context: Context): Bitmap? = convolve(context, FilterConstants.SHARPEN_MATRIX)

fun Bitmap?.edge(context: Context): Bitmap? = convolve(context, FilterConstants.EDGE_MATRIX)

fun Bitmap?.emboss(context: Context): Bitmap? = convolve(context, FilterConstants.EMBOSS_MATRIX)

fun Bitmap?.boxBlur(context: Context): Bitmap? = convolve(context, FilterConstants.BLUR_MATRIX)

fun Bitmap?.bloom(context: Context): Bitmap? = convolve(context, FilterConstants.BLOOM_MATRIX)
