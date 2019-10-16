package com.coolrs.lib.fire

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.widget.ImageView
import androidx.core.graphics.ColorUtils
import com.coolrs.lib.Effect
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

object FirePalette {

	val Fire: IntArray = IntArray(256).apply { forEachIndexed {i, _ ->
			val c = ColorUtils.HSLToColor(floatArrayOf(i / 3f / 256f * 360f, 1f, Math.min(1f, i * 2f / 256f)))
			this[i] = Color.argb(255, Color.blue(c), Color.green(c), Color.red(c))
		}
	}

	val Ice: IntArray = IntArray(256).apply { forEachIndexed {i, _ ->
			val c = ColorUtils.HSLToColor(floatArrayOf(i / 3f / 256f * 360f, 1f, Math.min(1f, i * 2f / 256f)))
			this [i] = Color.argb(255, Color.red(c), Color.green(c), Color.blue(c))
		}
	}

	val Poison: IntArray = IntArray(256).apply { forEachIndexed {i, _ ->
			val c = ColorUtils.HSLToColor(floatArrayOf(i / 3f / 256f * 360f, 1f, Math.min(1f, i * 2f / 256f)))
			this[i] = Color.argb(255, Color.blue(c), Color.red(c), Color.green(c))
		}
	}

	val Pink: IntArray = IntArray(256).apply { forEachIndexed {i, _ ->
			val c = ColorUtils.HSLToColor(floatArrayOf(i / 3f / 256f * 360f, 1f, Math.min(1f, i * 2f / 256f)))
			this[i] = Color.argb(255, Color.green(c), Color.blue(c), Color.red(c))
		}
	}

	val Purple: IntArray = IntArray(256).apply { forEachIndexed {i, _ ->
			val c = ColorUtils.HSLToColor(floatArrayOf(i / 3f / 256f * 360f, 1f, Math.min(1f, i * 2f / 256f)))
			this[i] = Color.argb(255, Color.red(c), Color.blue(c), Color.green(c))
		}
	}

	val Smoke: IntArray = IntArray(256).apply { forEachIndexed {i, _ ->
			val c = ColorUtils.HSLToColor(floatArrayOf(i / 3f / 256f * 360f, 1f, Math.min(1f, i * 2f / 256f)))
			val avg = (Color.red(c) + Color.blue(c) + Color.green(c)) / 3
			this[i] = Color.argb(255, avg, avg, avg)
		}
	}

	val Cool: IntArray = IntArray(256).apply { forEachIndexed {i, _ ->
			val c = ColorUtils.HSLToColor(floatArrayOf(i / 3f / 256f * 360f, 1f, Math.min(1f, i * 2f / 256f)))
			this[i] = Color.argb(255, c, c, c)
		}
	}

}


open class FireEffect(
	private val image: ImageView,
	private val width: Int = 128,
	private val height: Int = 128,
	val div: Float = 4.05f
) : Effect {

	private var bitmap: Bitmap? = null
	private var job: Job? = null
	private lateinit var fire: ByteArray
	private lateinit var palette: IntArray

	override fun start() {
		bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
		fire = ByteArray(width * height)
		palette = getPalette()
		bitmap = bitmap.fire(image.context, fire, palette, div, true)
		image.setImageBitmap(bitmap)
		job = GlobalScope.launch { loop() }
	}

	override fun stop() {
		runBlocking { job?.cancelAndJoin() }
		image.setImageBitmap(null)
		bitmap?.recycle()
		bitmap = null
		fire = ByteArray(0)
		palette = IntArray(0)
	}

	override fun name() = "Fire (Default)"

	private suspend fun loop() {
		while (true) {
			bitmap.fire(image.context, fire, palette, div, true)
			image.postInvalidate()
			delay(16)
		}
	}

	protected open fun getPalette(): IntArray = FirePalette.Fire

}

class IceFireEffect(image: ImageView, width: Int, height: Int, div: Float) : FireEffect(image, width, height, div) {
	override fun name() = "Fire (Ice)"
	override fun getPalette(): IntArray = FirePalette.Ice
}

class PoisonFireEffect(image: ImageView, width: Int, height: Int, div: Float) : FireEffect(image, width, height, div) {
	override fun name() = "Fire (Poison)"
	override fun getPalette(): IntArray = FirePalette.Poison
}

class PinkFireEffect(image: ImageView, width: Int, height: Int, div: Float) : FireEffect(image, width, height, div) {
	override fun name() = "Fire (Pink)"
	override fun getPalette(): IntArray = FirePalette.Pink
}

class PurpleFireEffect(image: ImageView, width: Int, height: Int, div: Float) : FireEffect(image, width, height, div) {
	override fun name() = "Fire (Purple)"
	override fun getPalette(): IntArray = FirePalette.Purple
}

class SmokeFireEffect(image: ImageView, width: Int, height: Int, div: Float) : FireEffect(image, width, height, div) {
	override fun name() = "Fire (Smoke)"
	override fun getPalette(): IntArray = FirePalette.Smoke
}

class CoolFireEffect(image: ImageView, width: Int, height: Int, div: Float) : FireEffect(image, width, height, div) {
	override fun name() = "Fire (Cool)"
	override fun getPalette(): IntArray = FirePalette.Cool
}


class TextFireEffect(
	private val image: ImageView,
	private val width: Int = 128,
	private val height: Int = 128,
	private val text: String,
	private val palette: IntArray = FirePalette.Fire,
	val div: Float = 4.05f
) : Effect {

	private var bitmap: Bitmap? = null
	private var job: Job? = null
	private lateinit var fire: ByteArray

	private fun drawText() {
		val b = Bitmap.createBitmap(width, height, Bitmap.Config.ALPHA_8)
		val paint = Paint().apply { textSize = 36f; color = Color.WHITE }
		val bounds = Rect()
		paint.getTextBounds(text,0, text.length, bounds)

		Canvas(b).drawText(text, (width - bounds.right - bounds.left) / 2f, (height - bounds.bottom - bounds.top) / 2f, paint)
		for(i in 0 until width) {
			for(j in 0 until height) {
				val p = b.getPixel(i, j)
				if (p != 0) fire[j * width + i] = Random.nextInt().toByte()
			}
		}
	}

	override fun start() {
		bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
		fire = ByteArray(width * height)
		bitmap = bitmap.fire(image.context, fire, palette, div, false)
		image.setImageBitmap(bitmap)
		job = GlobalScope.launch { loop() }
	}

	override fun stop() {
		runBlocking { job?.cancelAndJoin() }
		image.setImageBitmap(null)
		bitmap?.recycle()
		bitmap = null
		fire = ByteArray(0)
	}

	override fun name() = "TextFire ($text)"

	private suspend fun loop() {
		while (true) {
			drawText()
			bitmap.fire(image.context, fire, palette, div, false)
			image.postInvalidate()
			delay(16)
		}
	}
}
