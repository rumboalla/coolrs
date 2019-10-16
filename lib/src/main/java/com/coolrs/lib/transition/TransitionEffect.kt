package com.coolrs.lib.transition

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import com.coolrs.lib.Effect
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.InputStream

open class HorizontalTransitionEffect(
	private val image: ImageView,
	private val src: InputStream,
	private val dst: InputStream
) : Effect {

	protected var bitmap: Bitmap? = null
	protected var srcBitmap: Bitmap? = null
	protected var dstBitmap: Bitmap? = null
	protected var weight = 0f
	private var job: Job? = null

	override fun start() {
		job = GlobalScope.launch {
			srcBitmap = BitmapFactory.decodeStream(src)
			dstBitmap = BitmapFactory.decodeStream(dst)
			srcBitmap?.let { bitmap = Bitmap.createBitmap(it.width, it.height, it.config) }
			MainScope().launch { image.setImageBitmap(bitmap)  }

			var dir = 0
			while (true) {
				apply()

				if (dir == 0) {
					weight += 0.02f
					if (weight >= 1f) {
						weight = 1f
						dir = 1
					}
				} else {
					weight -= 0.02f
					if (weight <= 0f) {
						weight = 0f
						dir = 0
					}
				}

				image.postInvalidate()
				delay(16)
			}
		}
	}

	override fun stop() {
		runBlocking { job?.cancelAndJoin() }
		image.setImageBitmap(null)
		bitmap?.recycle()
		bitmap = null
		srcBitmap?.recycle()
		srcBitmap = null
		dstBitmap?.recycle()
		dstBitmap = null
		src.reset()
		dst.reset()
	}

	override fun name() = "Transition (Horizontal)"

	protected open fun apply() = bitmap.horizontalTransition(image.context, requireNotNull(srcBitmap), requireNotNull(dstBitmap), weight)

}

class VerticalTransitionEffect(private val image: ImageView, src: InputStream, dst: InputStream): HorizontalTransitionEffect(image, src, dst) {
	override fun name() = "Transition (Vertical)"
	override fun apply() = bitmap.verticalTransition(image.context, requireNotNull(srcBitmap), requireNotNull(dstBitmap), weight)
}

class BlendTransitionEffect(private val image: ImageView, src: InputStream, dst: InputStream): HorizontalTransitionEffect(image, src, dst) {
	override fun name() = "Transition (Blend)"
	override fun apply() = bitmap.blendTransition(image.context, requireNotNull(srcBitmap), requireNotNull(dstBitmap), weight)
}