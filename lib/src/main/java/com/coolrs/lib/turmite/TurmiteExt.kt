package com.coolrs.lib.turmite

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import com.coolrs.lib.ScriptC_turmite

/**
    Generic max 10 state, max 10 color turmite parser.

    Head contains 4 values with the state of the turmite: x, y, state, direction.

    For turmite string use values in triples (new color, direction to turn, new state). Examples:
        "140210320040"
        "140220380080"
        "182020180020080081"
        "121020010121"
        "111021180120"
        "121021110111"
        "120210040"
        "120220340440410", 5 color

    Direction values: 1 -> No turn (N), 2 -> Right (E), 4 -> U-Turn (S), 8 -> Left (W)

    Example:
        val turmite = "140210320040"
        val head = intArrayOf(31, 31, 0, 'N'.toInt())
        val bitmap = Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_8888).apply { eraseColor(Color.BLACK) }
        bitmap.turmite(requireContext(), head, turmite)
*/
fun Bitmap?.turmite(context: Context, head: IntArray, turmite: String, numColors: Int = 0): Bitmap? {
    if (this == null) return null

    val rs = RenderScript.create(context)
    val allocIn = Allocation.createFromBitmap(rs, this)
    val allocOut = Allocation.createTyped(rs, allocIn.type)

    val allocHead = Allocation.createSized(rs, Element.U32(rs), 4, Allocation.USAGE_SCRIPT)
    allocHead.copyFrom(head)

    val allocTurmite = Allocation.createSized(rs, Element.U8(rs), turmite.length, Allocation.USAGE_SCRIPT)
    allocTurmite.copyFrom(turmite.toByteArray(Charsets.US_ASCII))

    val script = ScriptC_turmite(rs)
    script._width = width.toLong()
    script._height = height.toLong()
    script._in = allocIn
    script._head = allocHead
    script._turmite_num_colors = if (numColors == 0) countColors(turmite) else numColors.toShort()
    script.bind_turmite_def(allocTurmite)
    script.forEach_turmite(allocOut)

    allocOut.copyTo(this)
    allocHead.copyTo(head)

    allocTurmite.destroy()
    allocIn.destroy()
    allocOut.destroy()
    allocHead.destroy()
    rs.destroy()

    return this
}

private fun countColors(turmite: String): Short = turmite.filterIndexed { i, _ -> i % 3 == 0 }.toList().distinct().count().toShort()
