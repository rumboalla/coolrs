package com.coolrs.lib.transition

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.RenderScript
import com.coolrs.lib.ScriptC_transition

fun Bitmap?.blendTransition(context: Context, bitmap1: Bitmap, bitmap2: Bitmap, weight: Float): Bitmap? {
    if (this == null) return null
    val rs = RenderScript.create(context)
    val b1 = Allocation.createFromBitmap(rs, bitmap1)
    val b2 = Allocation.createFromBitmap(rs, bitmap2)
    val allocOut = Allocation.createTyped(rs, b1.type)
    val script = ScriptC_transition(rs)
    script._b1 = b1
    script._b2 = b2
    script._weight = weight
    script.forEach_blendTransition(allocOut)
    allocOut.copyTo(this)
    allocOut.destroy()
    b1.destroy()
    b2.destroy()
    rs.destroy()
    return this
}

fun Bitmap?.horizontalTransition(context: Context, bitmap1: Bitmap, bitmap2: Bitmap, weight: Float): Bitmap? {
    if (this == null) return null
    val rs = RenderScript.create(context)
    val b1 = Allocation.createFromBitmap(rs, bitmap1)
    val b2 = Allocation.createFromBitmap(rs, bitmap2)
    val allocOut = Allocation.createTyped(rs, b1.type)
    val script = ScriptC_transition(rs)
    script._b1 = b1
    script._b2 = b2
    script._width = width.toLong()
    script._height = height.toLong()
    script._weight = weight
    script.forEach_horizontalTransition(allocOut)
    allocOut.copyTo(this)
    allocOut.destroy()
    b1.destroy()
    b2.destroy()
    rs.destroy()
    return this
}

fun Bitmap?.verticalTransition(context: Context, bitmap1: Bitmap, bitmap2: Bitmap, weight: Float): Bitmap? {
    if (this == null) return null
    val rs = RenderScript.create(context)
    val b1 = Allocation.createFromBitmap(rs, bitmap1)
    val b2 = Allocation.createFromBitmap(rs, bitmap2)
    val allocOut = Allocation.createTyped(rs, b1.type)
    val script = ScriptC_transition(rs)
    script._b1 = b1
    script._b2 = b2
    script._width = width.toLong()
    script._height = height.toLong()
    script._weight = weight
    script.forEach_verticalTransition(allocOut)
    allocOut.copyTo(this)
    allocOut.destroy()
    b1.destroy()
    b2.destroy()
    rs.destroy()
    return this
}
