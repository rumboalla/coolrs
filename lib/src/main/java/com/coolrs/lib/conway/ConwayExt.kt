package com.coolrs.lib.conway

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.RenderScript
import com.coolrs.lib.ScriptC_conway

fun Bitmap?.conway(context: Context): Bitmap? {
    if (this == null) return null
    val rs = RenderScript.create(context)
    val allocIn = Allocation.createFromBitmap(rs, this)
    val allocOut = Allocation.createTyped(rs, allocIn.type)
    val script = ScriptC_conway(rs)
    script._width = width.toLong()
    script._height = height.toLong()
    script._in = allocIn
    script.forEach_conway(allocOut)
    allocOut.copyTo(this)
    allocIn.destroy()
    allocOut.destroy()
    rs.destroy()
    return this
}
