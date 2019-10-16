package com.coolrs.lib.fire

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import com.coolrs.lib.ScriptC_fire

/**
 * fire: Last frame representation of fire.
 * divider indicates how fast will the fire die. Needs to be bigger than 4.0f.
 * palette: Palette containing 256 32 bit RGB colours.
 * randomize: If true it will add random pixels at the bottom of the image.
 */
fun Bitmap?.fire(
    context: Context,
    fire: ByteArray,
    palette: IntArray,
    divider: Float = 4.15f,
    randomize: Boolean = true
): Bitmap? {
    if (this == null) return null

    // Check arguments
    require(palette.size == 256) { "Palette size needs to be 256." }
    require(fire.size == width * height) { "Fire size needs to be Width * Height." }

    // Create Rs and buffers
    val rs = RenderScript.create(context)
    val allocIn = Allocation.createFromBitmap(rs, this)
    val allocOut = Allocation.createTyped(rs, allocIn.type)

    val allocPalette = Allocation.createSized(rs, Element.I32(rs), 256, Allocation.USAGE_SCRIPT)
    allocPalette.copyFrom(palette)

    val allocFire = Allocation.createSized(rs, Element.U8(rs), width * height, Allocation.USAGE_SCRIPT)
    allocFire.copyFrom(fire)

    val allocFire2 = Allocation.createSized(rs, Element.U8(rs), width * height, Allocation.USAGE_SCRIPT)
    allocFire2.copyFrom(fire)

    val script = ScriptC_fire(rs)
    script._width = width.toLong()
    script._height = height.toLong()
    script._in = allocIn
    script._divider = divider
    script.bind_palette(allocPalette)
    script.bind_buffer(allocFire)
    script.bind_buffer2(allocFire2)

    if (randomize) script.invoke_randomize()

    script.forEach_fire(allocOut)

    allocOut.copyTo(this)
    allocFire2.copyTo(fire)

    allocIn.destroy()
    allocOut.destroy()
    allocPalette.destroy()
    allocFire.destroy()
    allocFire2.destroy()
    rs.destroy()

    return this
}
