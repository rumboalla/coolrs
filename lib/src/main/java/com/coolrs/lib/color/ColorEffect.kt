package com.coolrs.lib.color

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import com.coolrs.lib.Effect
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.InputStream

open class ColorGrayscaleEffect(
	private val image: ImageView,
	private val src: InputStream
) : Effect {

	protected var bitmap: Bitmap? = null
	private var job: Job? = null

	override fun start() {
		job = GlobalScope.launch {
			bitmap = BitmapFactory.decodeStream(src)
			apply()
			MainScope().launch { image.setImageBitmap(bitmap)  }
		}
	}

	override fun stop() {
		runBlocking { job?.cancelAndJoin() }
		image.setImageBitmap(null)
		bitmap?.recycle()
		bitmap = null
		src.reset()
	}

	override fun name() = "Color (Grayscale)"

	protected open fun apply() = bitmap.grayscale(image.context)

}

class PolaroidColorEffect(private val image: ImageView, src: InputStream) : ColorGrayscaleEffect(image, src) {
	override fun name() = "Color (Polaroid)"
	override fun apply() = bitmap.polaroid(image.context)
}

class InvertColorEffect(private val image: ImageView, src: InputStream) : ColorGrayscaleEffect(image, src) {
	override fun name() = "Color (Invert)"
	override fun apply() = bitmap.invert(image.context)
}

class BlackAndWhiteColorEffect(private val image: ImageView, src: InputStream) : ColorGrayscaleEffect(image, src) {
	override fun name() = "Color (Black&White)"
	override fun apply() = bitmap.blackAndWhite(image.context)
}

class SepiaColorEffect(private val image: ImageView, src: InputStream) : ColorGrayscaleEffect(image, src) {
	override fun name() = "Color (Sepia)"
	override fun apply() = bitmap.sepia(image.context)
}

class SaturateColorEffect(private val image: ImageView, src: InputStream, private val s: Float) : ColorGrayscaleEffect(image, src) {
	override fun name() = "Color (Saturate)"
	override fun apply() = bitmap.saturate(image.context, s)
}

class BrightnessColorEffect(private val image: ImageView, src: InputStream, private val b: Float) : ColorGrayscaleEffect(image, src) {
	override fun name() = "Color (Brightness)"
	override fun apply() = bitmap.brightness(image.context, b)
}

class OffsetColorEffect(private val image: ImageView, src: InputStream, private val r: Float, private val g: Float, private val b: Float) : ColorGrayscaleEffect(image, src) {
	override fun name() = "Color (Offset)"
	override fun apply() = bitmap.offset(image.context, r, g, b)
}

class HistoEqColorEffect(private val image: ImageView, src: InputStream) : ColorGrayscaleEffect(image, src) {
	override fun name() = "Color (Histogram Equalization)"
	override fun apply() = bitmap.histogramEqualization(image.context)
}